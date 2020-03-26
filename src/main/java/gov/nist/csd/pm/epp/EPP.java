package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.epp.functions.FunctionExecutor;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
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

    public EPP(PDP pdp) throws PMException {
        this.pap = pdp.getPAP();
        this.pdp = pdp;
        this.functionEvaluator = new FunctionEvaluator();
    }

    public EPP(PDP pdp, EPPOptions eppOptions) throws PMException {
        this.pap = pdp.getPAP();
        this.pdp = pdp;
        this.functionEvaluator = new FunctionEvaluator();
        if (eppOptions != null) {
            for (FunctionExecutor executor : eppOptions.getExecutors()) {
                this.functionEvaluator.addFunctionExecutor(executor);
            }
        }
    }

    public PAP getPAP() {
        return pap;
    }

    public void setPAP(PAP pap) {
        this.pap = pap;
    }

    public PDP getPDP() {
        return pdp;
    }

    public void setPDP(PDP pdp) {
        this.pdp = pdp;
    }

    public void processEvent(EventContext eventCtx, String user, String process) throws PMException {
        System.out.println("user " + user + " (procID=" + process + ") performed " + eventCtx.getEvent() + " on " + eventCtx.getTarget().getName());
        List<Obligation> obligations = pap.getObligationsPAP().getAll();
        for(Obligation obligation : obligations) {
            if (!obligation.isEnabled()) {
                continue;
            }

            List<Rule> rules = obligation.getRules();
            for(Rule rule : rules) {
                if(!eventMatches(user, process, eventCtx.getEvent(), eventCtx.getTarget(), rule.getEventPattern())) {
                    continue;
                }

                // check the response condition
                ResponsePattern responsePattern = rule.getResponsePattern();
                Condition condition = responsePattern.getCondition();
                if(!checkCondition(condition, eventCtx, user, process, pdp)) {
                    continue;
                }

                for(Action action : rule.getResponsePattern().getActions()) {
                    if(!checkCondition(action.getCondition(), eventCtx, user, process, pdp)) {
                        continue;
                    }

                    applyAction(obligation.getLabel(), eventCtx, user, process, action);
                }
            }
        }
    }

    private boolean checkCondition(Condition condition, EventContext eventCtx, String user, String process, PDP pdp) throws PMException {
        if(condition == null) {
            return true;
        }

        List<Function> functions = condition.getCondition();
        for(Function f : functions) {
            boolean result = functionEvaluator.evalBool(eventCtx, user, process, pdp, f);
            if(!result) {
                return false;
            }
        }

        return true;
    }

    private boolean eventMatches(String user, String process, String event, Node target, EventPattern match) throws PMException {
        if(match.getOperations() != null &&
                !match.getOperations().contains(event)) {
            return false;
        }

        Subject matchSubject = match.getSubject();
        PolicyClass matchPolicyClass = match.getPolicyClass();
        Target matchTarget = match.getTarget();

        return subjectMatches(user, process, matchSubject) &&
                pcMatches(user, matchPolicyClass) &&
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

        // check if any element in any user is actually a UA and check if the current user is contained in it
        boolean uInUa = false;
        for (String u : matchSubject.getAnyUser()) {
            Node node = pap.getGraphPAP().getNode(u);
            if (node.getType().equals(UA) && isNodeContainedIn(userNode, node)) {
                uInUa = true;
                break;
            }
        }

        if(matchSubject.getAnyUser() != null && (matchSubject.getAnyUser().contains(userNode.getName()) || uInUa)) {
            return true;
        }

        if(matchSubject.getUser() != null && matchSubject.getUser().equals(userNode.getName())) {
            return true;
        }

        return matchSubject.getProcess() != null &&
                matchSubject.getProcess().getValue().equals(process);
    }

    private boolean isNodeContainedIn(Node childNode, Node parentNode) throws PMException {
        DepthFirstSearcher dfs = new DepthFirstSearcher(pdp.getPAP().getGraphPAP());
        Set<String> nodes = new HashSet<>();
        Visitor visitor = node -> {
            if (node.getName().equals(childNode.getName())) {
                nodes.add(node.getName());
            }
        };
        dfs.traverse(childNode, (c, p) -> {}, visitor, Direction.PARENTS);

        return nodes.contains(parentNode.getName());
    }

    private boolean pcMatches(String user, PolicyClass matchPolicyClass) {
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

    private void applyAction(String label, EventContext eventCtx, String user, String process, Action action) throws PMException {
        if(action instanceof AssignAction) {
            applyAssignAction(eventCtx, user, process, (AssignAction) action);
        } else if(action instanceof CreateAction) {
            applyCreateAction(label, eventCtx, user, process, (CreateAction) action);
        } else if(action instanceof DeleteAction) {
            applyDeleteAction(eventCtx, user, process, (DeleteAction) action);
        } else if(action instanceof DenyAction) {
            applyDenyAction(eventCtx, user, process, (DenyAction) action);
        } else if(action instanceof GrantAction) {
            applyGrantAction(eventCtx, user, process, (GrantAction) action);
        } else if(action instanceof FunctionAction) {
            functionEvaluator.evalNode(eventCtx, user, process, pdp, ((FunctionAction) action).getFunction());
        }
    }

    private void applyGrantAction(EventContext eventCtx, String user, String process, GrantAction action) throws PMException {
        EvrNode subject = action.getSubject();
        List<String> operations = action.getOperations();
        EvrNode target = action.getTarget();

        Node subjectNode = toNode(eventCtx, user, process, subject);
        Node targetNode = toNode(eventCtx, user, process, target);

        pap.getGraphPAP().associate(subjectNode.getName(), targetNode.getName(), new OperationSet(operations));
    }

    private void applyDenyAction(EventContext eventCtx, String user, String process, DenyAction action) throws PMException {
        EvrNode subject = action.getSubject();
        List<String> operations = action.getOperations();
        DenyAction.Target target = action.getTarget();

        String denySubject = toDenySubject(eventCtx, user, process, subject);
        Map<String, Boolean> denyNodes = toDenyNodes(eventCtx, user, process, target);

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

    private Map<String, Boolean> toDenyNodes(EventContext eventCtx, String user, String process, DenyAction.Target target) throws PMException {
        Map<String, Boolean> nodes = new HashMap<>();
        List<DenyAction.Target.Container> containers = target.getContainers();
        for(DenyAction.Target.Container container : containers) {
            if(container.getFunction() != null) {
                Function function = container.getFunction();
                Object result = functionEvaluator.evalObject(eventCtx, user, process, pdp, function);

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

    private String toDenySubject(EventContext eventCtx, String user, String process, EvrNode subject) throws PMException {
        String denySubject;

        if(subject.getFunction() != null) {
            Function function = subject.getFunction();
            denySubject = functionEvaluator.evalString(eventCtx, user, process, pdp, function);
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

    private void applyDeleteAction(EventContext eventCtx, String user, String process, DeleteAction action) throws PMException {
        List<EvrNode> nodes = action.getNodes();
        for (EvrNode evrNode : nodes) {
            Node node = toNode(eventCtx, user, process, evrNode);
            pdp.getPAP().getGraphPAP().deleteNode(node.getName());
        }

        AssignAction assignAction = action.getAssignments();
        for (AssignAction.Assignment assignment : assignAction.getAssignments()) {
            Node what = toNode(eventCtx, user, process, assignment.getWhat());
            Node where = toNode(eventCtx, user, process, assignment.getWhere());
            pdp.getPAP().getGraphPAP().deassign(what.getName(), where.getName());
        }

        List<GrantAction> associations = action.getAssociations();
        for (GrantAction grantAction : associations) {
            Node subject = toNode(eventCtx, user, process, grantAction.getSubject());
            Node target = toNode(eventCtx, user, process, grantAction.getTarget());
            pdp.getPAP().getGraphPAP().dissociate(subject.getName(), target.getName());
        }

        List<String> prohibitions = action.getProhibitions();
        for (String label : prohibitions) {
            pdp.getPAP().getProhibitionsPAP().delete(label);
        }

        List<String> rules = action.getRules();
        for (String label : rules) {
            List<Obligation> obligations = pdp.getPAP().getObligationsPAP().getAll();
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

    private Node toNode(EventContext eventCtx, String user, String process, EvrNode evrNode) throws PMException {
        Node node;
        if(evrNode.getFunction() != null) {
            node = functionEvaluator.evalNode(eventCtx, user, process, pdp, evrNode.getFunction());
        } else {
            if (evrNode.getName() != null && !evrNode.getName().isEmpty()) {
                node = pap.getGraphPAP().getNode(evrNode.getName());
            } else {
                node = pap.getGraphPAP().getNode(NodeType.toNodeType(evrNode.getType()), evrNode.getProperties());
            }
        }
        return node;
    }

    private void applyCreateAction(String label, EventContext eventCtx, String user, String process, CreateAction action) throws PMException {
        for (Rule rule : action.getRules()) {
            createRule(label, eventCtx, rule);
        }

        for (CreateAction.CreateNode createNode : action.getCreateNodesList()) {
            EvrNode what = createNode.getWhat();
            EvrNode where = createNode.getWhere();
            Node whereNode = toNode(eventCtx, user, process, where);
            pap.getGraphPAP().createNode(what.getName(), NodeType.toNodeType(what.getType()), what.getProperties(), whereNode.getName());
        }
    }

    private void createRule(String obligationLabel, EventContext eventCtx, Rule rule) {
        // add the rule to the obligation
        Obligation obligation = pap.getObligationsPAP().get(obligationLabel);
        List<Rule> rules = obligation.getRules();
        rules.add(rule);
        obligation.setRules(rules);
    }

    private void applyAssignAction(EventContext eventCtx, String user, String process, AssignAction action) throws PMException {
        for (AssignAction.Assignment assignment : action.getAssignments()) {
            EvrNode what = assignment.getWhat();
            EvrNode where = assignment.getWhere();

            Node whatNode = toNode(eventCtx, user, process, what);
            Node whereNode = toNode(eventCtx, user, process, where);

            pap.getGraphPAP().assign(whatNode.getName(), whereNode.getName());
        }
    }
}
