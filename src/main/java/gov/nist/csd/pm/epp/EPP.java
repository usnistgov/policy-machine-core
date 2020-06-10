package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.epp.functions.FunctionExecutor;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.dag.searcher.DepthFirstSearcher;
import gov.nist.csd.pm.pip.graph.dag.searcher.Direction;
import gov.nist.csd.pm.pip.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.model.*;
import gov.nist.csd.pm.pip.obligations.model.actions.*;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.model.ContainerCondition;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;

public class EPP {

    private PAP pap;
    private PDP pdp;
    private FunctionEvaluator functionEvaluator;

    public EPP(PDP pdp, PAP pap) throws PMException {
        this.pap = pap;
        this.pdp = pdp;
        this.functionEvaluator = new FunctionEvaluator();
    }

    public EPP(PDP pdp, PAP pap, EPPOptions eppOptions) throws PMException {
        this.pap = pap;
        this.pdp = pdp;
        this.functionEvaluator = new FunctionEvaluator();
        if (eppOptions != null) {
            for (FunctionExecutor executor : eppOptions.getExecutors()) {
                this.functionEvaluator.addFunctionExecutor(executor);
            }
        }
    }
    
    public PDP getPDP() {
        return pdp;
    }

    public void processEvent(EventContext eventCtx) throws PMException {
        List<Obligation> obligations = pap.getObligationsPAP().getAll();
        for(Obligation obligation : obligations) {
            if (!obligation.isEnabled()) {
                continue;
            }

            UserContext definingUser = new UserContext(obligation.getUser());

            List<Rule> rules = obligation.getRules();
            for(Rule rule : rules) {
                if(!eventMatches(eventCtx.getUserCtx(), eventCtx.getEvent(), eventCtx.getTarget(), rule.getEventPattern())) {
                    continue;
                }

                // check the response condition
                ResponsePattern responsePattern = rule.getResponsePattern();
                if(!checkCondition(definingUser, responsePattern.getCondition(), eventCtx, pdp)) {
                    continue;
                } else if(!checkNegatedCondition(definingUser, responsePattern.getNegatedCondition(), eventCtx, pdp)) {
                    continue;
                }

                for(Action action : rule.getResponsePattern().getActions()) {
                    if(!checkCondition(definingUser, action.getCondition(), eventCtx, pdp)) {
                        continue;
                    } else if(!checkNegatedCondition(definingUser, action.getNegatedCondition(), eventCtx, pdp)) {
                        continue;
                    }

                    applyAction(definingUser, obligation.getLabel(), eventCtx, action);
                }
            }
        }
    }

    private boolean checkCondition(UserContext definingUser, Condition condition, EventContext eventCtx, PDP pdp) throws PMException {
        if(condition == null) {
            return true;
        }

        List<Function> functions = condition.getCondition();
        for(Function f : functions) {
            boolean result = functionEvaluator.evalBool(definingUser, eventCtx, pdp, f);
            if(!result) {
                return false;
            }
        }

        return true;
    }

    /**
     * Return true if the condition is satisfied. A condition is satisfied if all the functions evaluate to false.
     */
    private boolean checkNegatedCondition(UserContext definingUser, NegatedCondition condition, EventContext eventCtx, PDP pdp) throws PMException {
        if(condition == null) {
            return true;
        }

        List<Function> functions = condition.getCondition();
        for(Function f : functions) {
            boolean result = functionEvaluator.evalBool(definingUser, eventCtx, pdp, f);
            if(result) {
                return false;
            }
        }

        return true;
    }

    private boolean eventMatches(UserContext userCtx, String event, Node target, EventPattern match) throws PMException {
        if(match.getOperations() != null &&
                !match.getOperations().contains(event)) {
            return false;
        }

        Subject matchSubject = match.getSubject();
        PolicyClass matchPolicyClass = match.getPolicyClass();
        Target matchTarget = match.getTarget();

        return subjectMatches(userCtx.getUser(), userCtx.getProcess(), matchSubject) &&
                pcMatches(userCtx.getUser(), userCtx.getProcess(), matchPolicyClass) &&
                targetMatches(target, matchTarget);
    }

    private boolean subjectMatches(String user, String process, Subject matchSubject) throws PMException {
        if(matchSubject == null) {
            return true;
        }

        // any user
        if((matchSubject.getAnyUser() == null && matchSubject.getUser() == null && matchSubject.getProcess() == null) ||
                (matchSubject.getAnyUser() != null && matchSubject.getAnyUser().isEmpty())) {
            return true;
        }

        // get the current user node
        Node userNode = pap.getGraphPAP().getNode(user);

        if (checkAnyUser(userNode, matchSubject.getAnyUser())) {
            return true;
        }

        if(matchSubject.getUser() != null && matchSubject.getUser().equals(userNode.getName())) {
            return true;
        }

        return matchSubject.getProcess() != null &&
                matchSubject.getProcess().getValue().equals(process);
    }

    private boolean checkAnyUser(Node userNode, List<String> anyUser) throws PMException {
        if (anyUser == null || anyUser.isEmpty()) {
            return true;
        }

        DepthFirstSearcher dfs = new DepthFirstSearcher(pap.getGraphPAP());

        // check each user in the anyUser list
        // there can be users and user attributes
        for (String u : anyUser) {
            Node anyUserNode = pap.getGraphPAP().getNode(u);

            // if the node in anyUser == the user than return true
            if (anyUserNode.getName().equals(userNode.getName())) {
                return true;
            }

            // if the anyUser is not an UA, move to the next one
            if (anyUserNode.getType() != UA) {
                continue;
            }

            Set<String> nodes = new HashSet<>();
            Visitor visitor = node -> {
                if (node.getName().equals(userNode.getName())) {
                    nodes.add(node.getName());
                }
            };
            dfs.traverse(userNode, (c, p) -> {}, visitor, Direction.PARENTS);

            if (nodes.contains(anyUserNode.getName())) {
                return true;
            }
        }

        return false;
    }

    private boolean pcMatches(String user, String process, PolicyClass matchPolicyClass) {
        // not yet implemented
        return true;
    }

    private boolean targetMatches(Node target, Target matchTarget) throws PMException {
        if(matchTarget == null) {
            return true;
        }

        if (matchTarget.getPolicyElements() == null &&
                matchTarget.getContainers() == null) {
            return true;
        }

        if(matchTarget.getContainers() != null) {
            if (matchTarget.getContainers().isEmpty()) {
                return true;
            }

            // check that target is contained in any container
            Set<Node> containers = getContainersOf(target.getName());
            for(EvrNode evrContainer : matchTarget.getContainers()) {
                for(Node contNode : containers) {
                    if (nodesMatch(evrContainer, contNode)) {
                        return true;
                    }
                }
            }

            return false;
        } else if(matchTarget.getPolicyElements() != null) {
            if (matchTarget.getPolicyElements().isEmpty()) {
                return true;
            }

            // check that target is in the list of policy elements
            for(EvrNode evrNode : matchTarget.getPolicyElements()) {
                if(nodesMatch(evrNode, target)) {
                    return true;
                }
            }

            return false;
        }

        return false;
    }

    private boolean nodesMatch(EvrNode evrNode, Node node) {
        if(!evrNode.getName().equals(node.getName())) {
            return false;
        }

        if(!evrNode.getType().equals(node.getType().toString())) {
            return false;
        }

        for (String k : evrNode.getProperties().keySet()) {
            String v = evrNode.getProperties().get(k);
            if(node.getProperties().containsKey(k)) {
                if(!node.getProperties().get(k).equals(v)) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }

    private Set<Node> getContainersOf(String name) throws PMException {
        Set<Node> nodes = new HashSet<>();
        Set<String> parents = pap.getGraphPAP().getParents(name);
        for (String parent : parents) {
            nodes.add(pap.getGraphPAP().getNode(parent));
            nodes.addAll(getContainersOf(parent));
        }
        return nodes;
    }

    private void applyAction(UserContext definingUser, String label, EventContext eventCtx, Action action) throws PMException {
        if (action == null) {
            return;
        }

        if(action instanceof AssignAction) {
            applyAssignAction(definingUser, eventCtx, (AssignAction) action);
        } else if(action instanceof CreateAction) {
            applyCreateAction(definingUser, label, eventCtx, (CreateAction) action);
        } else if(action instanceof DeleteAction) {
            applyDeleteAction(definingUser, eventCtx, (DeleteAction) action);
        } else if(action instanceof DenyAction) {
            applyDenyAction(definingUser, eventCtx, (DenyAction) action);
        } else if(action instanceof GrantAction) {
            applyGrantAction(definingUser, eventCtx, (GrantAction) action);
        } else if(action instanceof FunctionAction) {
            functionEvaluator.evalNode(definingUser, eventCtx, pdp, ((FunctionAction) action).getFunction());
        }
    }

    private void applyGrantAction(UserContext definingUser, EventContext eventCtx, GrantAction action) throws PMException {
        EvrNode subject = action.getSubject();
        List<String> operations = action.getOperations();
        EvrNode target = action.getTarget();

        Node subjectNode = toNode(definingUser, eventCtx, subject);
        Node targetNode = toNode(definingUser, eventCtx, target);

        pap.getGraphPAP().associate(subjectNode.getName(), targetNode.getName(), new OperationSet(operations));
    }

    private void applyDenyAction(UserContext definingUser, EventContext eventCtx, DenyAction action) throws PMException {
        EvrNode subject = action.getSubject();
        List<String> operations = action.getOperations();
        DenyAction.Target target = action.getTarget();

        String denySubject = toDenySubject(definingUser, eventCtx, subject);
        Map<String, Boolean> denyNodes = toDenyNodes(definingUser, eventCtx, target);

        Prohibition.Builder builder = new Prohibition.Builder(action.getLabel(), denySubject, new OperationSet(operations))
                .setIntersection(target.isIntersection());

        for(String contName : denyNodes.keySet()) {
            builder.addContainer(contName, denyNodes.get(contName));
        }

        // add the prohibition to the PAP
        pap.getProhibitionsPAP().add(builder.build());

        // TODO this complement is ignored in the current Prohibition object
        boolean complement = target.isComplement();
    }

    private Map<String, Boolean> toDenyNodes(UserContext definingUser, EventContext eventCtx, DenyAction.Target target) throws PMException {
        Map<String, Boolean> nodes = new HashMap<>();
        List<DenyAction.Target.Container> containers = target.getContainers();
        for(DenyAction.Target.Container container : containers) {
            if(container.getFunction() != null) {
                Function function = container.getFunction();
                Object result = functionEvaluator.evalObject(definingUser, eventCtx, pdp, function);

                if(!(result instanceof ContainerCondition)) {
                    throw new PMException("expected function to return a ContainerCondition but got " + result.getClass().getName());
                }

                ContainerCondition cc = (ContainerCondition) result;
                nodes.put(cc.getName(), cc.isComplement());
            } else {
                Graph graph = pap.getGraphPAP();

                // get the node
                Node node = graph.getNode(container.getName());
                nodes.put(node.getName(), container.isComplement());
            }
        }

        return nodes;
    }

    private String toDenySubject(UserContext definingUser, EventContext eventCtx, EvrNode subject) throws PMException {
        String denySubject;

        if(subject.getFunction() != null) {
            Function function = subject.getFunction();
            denySubject = functionEvaluator.evalString(definingUser, eventCtx, pdp, function);
        } else if(subject.getProcess() != null) {
            denySubject = subject.getProcess().getValue();
        } else {
            if (subject.getName() != null) {
                denySubject = pap.getGraphPAP().getNode(subject.getName()).getName();
            } else {
                denySubject = pap.getGraphPAP().getNode(NodeType.toNodeType(subject.getType()), subject.getProperties()).getName();
            }
        }

        return denySubject;
    }

    private void applyDeleteAction(UserContext definingUser, EventContext eventCtx, DeleteAction action) throws PMException {
        List<EvrNode> nodes = action.getNodes();
        if (nodes != null) {
            for (EvrNode evrNode : nodes) {
                Node node = toNode(definingUser, eventCtx, evrNode);
                pap.getGraphPAP().deleteNode(node.getName());
            }
        }

        AssignAction assignAction = action.getAssignments();
        if (assignAction != null) {
            for (AssignAction.Assignment assignment : assignAction.getAssignments()) {
                Node what = toNode(definingUser, eventCtx, assignment.getWhat());
                Node where = toNode(definingUser, eventCtx, assignment.getWhere());
                pap.getGraphPAP().deassign(what.getName(), where.getName());
            }
        }

        List<GrantAction> associations = action.getAssociations();
        if (associations != null){
            for (GrantAction grantAction : associations) {
                Node subject = toNode(definingUser, eventCtx, grantAction.getSubject());
                Node target = toNode(definingUser, eventCtx, grantAction.getTarget());
                pap.getGraphPAP().dissociate(subject.getName(), target.getName());
            }
        }

        List<String> prohibitions = action.getProhibitions();
        if (prohibitions != null) {
            for (String label : prohibitions) {
                pap.getProhibitionsPAP().delete(label);
            }
        }

        List<String> rules = action.getRules();
        if (rules != null) {
            for (String label : rules) {
                List<Obligation> obligations = pap.getObligationsPAP().getAll();
                for (Obligation obligation : obligations) {
                    List<Rule> oblRules = obligation.getRules();
                    for (Rule rule : oblRules) {
                        if (rule.getLabel().equals(label)) {
                            oblRules.remove(rule);
                        }
                    }
                }
            }
        }
    }

    private Node toNode(UserContext definingUser, EventContext eventCtx, EvrNode evrNode) throws PMException {
        Node node;
        if(evrNode.getFunction() != null) {
            node = functionEvaluator.evalNode(definingUser, eventCtx, pdp, evrNode.getFunction());
        } else {
            if (evrNode.getName() != null && !evrNode.getName().isEmpty()) {
                node = pap.getGraphPAP().getNode(evrNode.getName());
            } else {
                node = pap.getGraphPAP().getNode(NodeType.toNodeType(evrNode.getType()), evrNode.getProperties());
            }
        }
        return node;
    }

    private void applyCreateAction(UserContext definingUser, String label, EventContext eventCtx, CreateAction action) throws PMException {
        List<Rule> rules = action.getRules();
        if (rules != null) {
            for (Rule rule : rules) {
                createRule(label, eventCtx, rule);
            }
        }

        List<CreateAction.CreateNode> createNodesList = action.getCreateNodesList();
        if (createNodesList != null) {
            for (CreateAction.CreateNode createNode : createNodesList) {
                EvrNode what = createNode.getWhat();
                EvrNode where = createNode.getWhere();
                Node whereNode = toNode(definingUser, eventCtx, where);
                pap.getGraphPAP().createNode(what.getName(), NodeType.toNodeType(what.getType()), what.getProperties(), whereNode.getName());
            }
        }
    }

    private void createRule(String obligationLabel, EventContext eventCtx, Rule rule) {
        // add the rule to the obligation
        Obligation obligation = pap.getObligationsPAP().get(obligationLabel);
        List<Rule> rules = obligation.getRules();
        rules.add(rule);
        obligation.setRules(rules);
    }

    private void applyAssignAction(UserContext definingUser, EventContext eventCtx, AssignAction action) throws PMException {
        List<AssignAction.Assignment> assignments = action.getAssignments();
        if (assignments != null) {
            for (AssignAction.Assignment assignment : assignments) {
                EvrNode what = assignment.getWhat();
                EvrNode where = assignment.getWhere();

                Node whatNode = toNode(definingUser, eventCtx, what);
                Node whereNode = toNode(definingUser, eventCtx, where);

                pap.getGraphPAP().assign(whatNode.getName(), whereNode.getName());
            }
        }
    }
}
