package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.epp.functions.FunctionExecutor;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.model.*;
import gov.nist.csd.pm.pip.obligations.model.actions.*;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;

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

    public void processEvent(EventContext eventCtx, long userID, long processID) throws PMException {
        System.out.println("user " + userID + " (procID=" + processID + ") performed " + eventCtx.getEvent() + " on " + eventCtx.getTarget().getName());
        List<Obligation> obligations = pap.getObligationsPAP().getAll();
        for(Obligation obligation : obligations) {
            if (!obligation.isEnabled()) {
                continue;
            }

            List<Rule> rules = obligation.getRules();
            for(Rule rule : rules) {
                if(!eventMatches(userID, processID, eventCtx.getEvent(), eventCtx.getTarget(), rule.getEventPattern())) {
                    continue;
                }

                // check the response condition
                ResponsePattern responsePattern = rule.getResponsePattern();
                Condition condition = responsePattern.getCondition();
                if(!checkCondition(condition, eventCtx, userID, processID, pdp)) {
                    continue;
                }

                for(Action action : rule.getResponsePattern().getActions()) {
                    if(!checkCondition(action.getCondition(), eventCtx, userID, processID, pdp)) {
                        continue;
                    }

                    applyAction(obligation.getLabel(), eventCtx, userID, processID, action);
                }
            }
        }
    }

    private boolean checkCondition(Condition condition, EventContext eventCtx, long userID, long processID, PDP pdp) throws PMException {
        if(condition == null) {
            return true;
        }

        List<Function> functions = condition.getCondition();
        for(Function f : functions) {
            boolean result = functionEvaluator.evalBool(eventCtx, userID, processID, pdp, f);
            if(!result) {
                return false;
            }
        }

        return true;
    }

    private boolean eventMatches(long userID, long processID, String event, Node target, EventPattern match) throws PMException {
        if(match.getOperations() != null &&
                !match.getOperations().contains(event)) {
            return false;
        }

        Subject matchSubject = match.getSubject();
        PolicyClass matchPolicyClass = match.getPolicyClass();
        Target matchTarget = match.getTarget();

        return subjectMatches(userID, processID, matchSubject) &&
                pcMatches(userID, matchPolicyClass) &&
                targetMatches(target, matchTarget);
    }

    private boolean subjectMatches(long userID, long processID, Subject matchSubject) throws PMException {
        if(matchSubject == null) {
            return true;
        }

        // any user
        if((matchSubject.getAnyUser() == null && matchSubject.getUser() == null && matchSubject.getProcess() == null) ||
                (matchSubject.getAnyUser() != null && matchSubject.getAnyUser().isEmpty())) {
            return true;
        }

        Node node = pap.getGraphPAP().getNode(userID);
        if(matchSubject.getAnyUser() != null && matchSubject.getAnyUser().contains(node.getName())) {
            return true;
        }

        if(matchSubject.getUser() != null && matchSubject.getUser().equals(node.getName())) {
            return true;
        }

        if(matchSubject.getProcess() != null &&
                matchSubject.getProcess().getValue() == processID) {
            return true;
        }

        return false;
    }

    private boolean pcMatches(long userID, PolicyClass matchPolicyClass) {
        // TODO ignoring this for now as it will be inefficient to find all the PCs a user is under
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
            Set<Node> containers = getContainersOf(target.getID());
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

    private Set<Node> getContainersOf(long id) throws PMException {
        Set<Node> nodes = new HashSet<>();
        Set<Node> parents = pap.getGraphPAP().getParents(id);
        for (Node parent : parents) {
            nodes.add(pap.getGraphPAP().getNode(parent.getID()));
            nodes.addAll(getContainersOf(parent.getID()));
        }
        return nodes;
    }

    private void applyAction(String label, EventContext eventCtx, long userID, long processID, Action action) throws PMException {
        if(action instanceof AssignAction) {
            applyAssignAction(eventCtx, userID, processID, (AssignAction) action);
        } else if(action instanceof CreateAction) {
            applyCreateAction(label, eventCtx, userID, processID, (CreateAction) action);
        } else if(action instanceof DeleteAction) {
            applyDeleteAction(eventCtx, userID, processID, (DeleteAction) action);
        } else if(action instanceof DenyAction) {
            applyDenyAction(eventCtx, userID, processID, (DenyAction) action);
        } else if(action instanceof GrantAction) {
            applyGrantAction(eventCtx, userID, processID, (GrantAction) action);
        } else if(action instanceof FunctionAction) {
            functionEvaluator.evalNode(eventCtx, userID, processID, pdp, ((FunctionAction) action).getFunction());
        }
    }

    private void applyGrantAction(EventContext eventCtx, long userID, long processID, GrantAction action) throws PMException {
        EvrNode subject = action.getSubject();
        List<String> operations = action.getOperations();
        EvrNode target = action.getTarget();

        Node subjectNode = toNode(eventCtx, userID, processID, subject);
        Node targetNode = toNode(eventCtx, userID, processID, target);

        pap.getGraphPAP().associate(subjectNode.getID(), targetNode.getID(), new OperationSet(operations));
    }

    private void applyDenyAction(EventContext eventCtx, long userID, long processID, DenyAction action) throws PMException {
        EvrNode subject = action.getSubject();
        List<String> operations = action.getOperations();
        DenyAction.Target target = action.getTarget();

        Prohibition.Subject denySubject = toDenySubject(eventCtx, userID, processID, subject);
        List<Prohibition.Node> denyNodes = toDenyNodes(eventCtx, userID, processID, target);

        Prohibition prohibition = new Prohibition();
        prohibition.setSubject(denySubject);
        prohibition.setName(action.getLabel());
        prohibition.setOperations(new HashSet<>(operations));
        prohibition.setIntersection(target.isIntersection());
        for(Prohibition.Node node : denyNodes) {
            prohibition.addNode(node);
        }

        // add the prohibition to the PAP
        pap.getProhibitionsPAP().add(prohibition);

        // TODO this complement is ignored in the current Prohibition object
        boolean complement = target.isComplement();
    }

    private List<Prohibition.Node> toDenyNodes(EventContext eventCtx, long userID, long processID, DenyAction.Target target) throws PMException {
        List<Prohibition.Node> nodes = new ArrayList<>();
        List<DenyAction.Target.Container> containers = target.getContainers();
        for(DenyAction.Target.Container container : containers) {
            if(container.getFunction() != null) {
                Function function = container.getFunction();
                Object result = functionEvaluator.evalObject(eventCtx, userID, processID, pdp, function);

                if(!(result instanceof Prohibition.Node)) {
                    throw new PMException("expected function to return a Prohibition.Node but got " + result.getClass().getName());
                }

                nodes.add((Prohibition.Node) result);
            } else {
                Graph graph = pap.getGraphPAP();

                // get the subject node
                Set<Node> search = graph.search(container.getName(), container.getType(), null);
                if(search.isEmpty()) {
                    throw new PMException("no nodes matched subject with name '" + container.getName() + "' and type '" + container.getType() + "'");
                }
                Node node = search.iterator().next();

                nodes.add(new Prohibition.Node(node.getID(), container.isComplement()));
            }
        }

        return nodes;
    }

    private Prohibition.Subject toDenySubject(EventContext eventCtx, long userID, long processID, EvrNode subject) throws PMException {
        Prohibition.Subject denySubject;

        if(subject.getFunction() != null) {
            Function function = subject.getFunction();
            denySubject = functionEvaluator.evalProhibitionSubject(eventCtx, userID, processID, pdp, function);
        } else if(subject.getProcess() != null) {
            denySubject = new Prohibition.Subject(subject.getProcess().getValue(), Prohibition.Subject.Type.PROCESS);
        } else {
            Set<Node> nodes = getNodes(subject.getName(), subject.getType(), subject.getProperties());
            if(nodes.isEmpty()) {
                throw new PMException("non existing subject for deny " + subject.getName());
            }

            // only one object is used, so grab the first one
            Node node = nodes.iterator().next();
            denySubject = new Prohibition.Subject(node.getID(),
                    node.getType() == UA ? Prohibition.Subject.Type.USER_ATTRIBUTE : Prohibition.Subject.Type.USER);
        }

        return denySubject;
    }

    private Set<Node> getNodes(String name, String type, Map<String, String> properties) throws PMException {
        Graph graph = pap.getGraphPAP();

        Set<Node> search = graph.search(name, type, null);
        if(properties != null) {
            search.removeIf((n) -> {
                for (String k : properties.keySet()) {
                    if(n.getProperties() == null ||
                            !n.getProperties().containsKey(k) ||
                            !n.getProperties().get(k).equals(properties.get(k))) {
                        return true;
                    }
                }
                return false;
            });
        }

        return search;
    }

    private Node getNode(String name, String type, Map<String, String> properties) throws PMException {
        Graph graph = pap.getGraphPAP();

        if (properties == null) {
            properties = new HashMap<>();
        }

        Set<Node> search = graph.search(name, type, properties);
        if (search.isEmpty()) {
            throw new PMException(String.format("no subject node could be found with name %s, type %s, and properties %s",
                    name, type, properties));
        }

        return search.iterator().next();
    }

    private void applyDeleteAction(EventContext eventCtx, long userID, long processID, DeleteAction action) throws PMException {
        List<EvrNode> nodes = action.getNodes();
        for (EvrNode evrNode : nodes) {
            Node node = toNode(eventCtx, userID, processID, evrNode);
            pdp.getPAP().getGraphPAP().deleteNode(node.getID());
        }

        AssignAction assignAction = action.getAssignments();
        for (AssignAction.Assignment assignment : assignAction.getAssignments()) {
            Node what = toNode(eventCtx, userID, processID, assignment.getWhat());
            Node where = toNode(eventCtx, userID, processID, assignment.getWhere());
            pdp.getPAP().getGraphPAP().deassign(what.getID(), where.getID());
        }

        List<GrantAction> associations = action.getAssociations();
        for (GrantAction grantAction : associations) {
            Node subject = toNode(eventCtx, userID, processID, grantAction.getSubject());
            Node target = toNode(eventCtx, userID, processID, grantAction.getTarget());
            pdp.getPAP().getGraphPAP().dissociate(subject.getID(), target.getID());
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

    private Node toNode(EventContext eventCtx, long userID, long processID, EvrNode evrNode) throws PMException {
        Node node;
        if(evrNode.getFunction() != null) {
            node = functionEvaluator.evalNode(eventCtx, userID, processID, pdp, evrNode.getFunction());
        } else {
            node = getNode(evrNode.getName(), evrNode.getType(), evrNode.getProperties());
        }
        return node;
    }

    private void applyCreateAction(String label, EventContext eventCtx, long userID, long processID, CreateAction action) throws PMException {
        for (Rule rule : action.getRules()) {
            createRule(label, eventCtx, rule);
        }

        for (CreateAction.CreateNode createNode : action.getCreateNodesList()) {
            EvrNode what = createNode.getWhat();
            EvrNode where = createNode.getWhere();
            Node whereNode = toNode(eventCtx, userID, processID, where);
            pap.getGraphPAP().createNode(new Random().nextLong(), what.getName(), NodeType.toNodeType(what.getType()), what.getProperties(), whereNode.getID());
        }
    }

    private void createRule(String obligationLabel, EventContext eventCtx, Rule rule) {
        // add the rule to the obligation
        Obligation obligation = pap.getObligationsPAP().get(obligationLabel);
        List<Rule> rules = obligation.getRules();
        rules.add(rule);
        obligation.setRules(rules);
    }

    private void applyAssignAction(EventContext eventCtx, long userID, long processID, AssignAction action) throws PMException {
        for (AssignAction.Assignment assignment : action.getAssignments()) {
            EvrNode what = assignment.getWhat();
            EvrNode where = assignment.getWhere();

            Node whatNode = toNode(eventCtx, userID, processID, what);
            Node whereNode = toNode(eventCtx, userID, processID, where);

            pap.getGraphPAP().assign(whatNode.getID(), whereNode.getID());
        }
    }
}
