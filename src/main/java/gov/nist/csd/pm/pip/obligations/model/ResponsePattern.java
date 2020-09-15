package gov.nist.csd.pm.pip.obligations.model;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.model.actions.*;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.model.ContainerCondition;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponsePattern {

    private Condition    condition;
    private NegatedCondition negatedCondition;
    private List<Action> actions;

    public ResponsePattern() {
        this.actions = new ArrayList<>();
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public NegatedCondition getNegatedCondition() {
        return negatedCondition;
    }

    public void setNegatedCondition(NegatedCondition negatedCondition) {
        this.negatedCondition = negatedCondition;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void addAction(Action action) {
        this.actions.add(action);
    }

    public void apply(PDP pdp, PAP pap, FunctionEvaluator functionEvaluator, UserContext definingUser,
                      EventContext eventCtx, Rule rule, String obligationLabel) throws PMException {
        // check the response condition
        ResponsePattern responsePattern = rule.getResponsePattern();
        if(!checkCondition(functionEvaluator, definingUser, responsePattern.getCondition(), eventCtx, pdp) ||
                !checkNegatedCondition(functionEvaluator, definingUser, responsePattern.getNegatedCondition(), eventCtx, pdp)) {
            return;
        }

        for(Action action : rule.getResponsePattern().getActions()) {
            if(!checkCondition(functionEvaluator, definingUser, action.getCondition(), eventCtx, pdp)) {
                continue;
            } else if(!checkNegatedCondition(functionEvaluator, definingUser, action.getNegatedCondition(), eventCtx, pdp)) {
                continue;
            }

            applyAction(pdp, pap, functionEvaluator, definingUser, obligationLabel, eventCtx, action);
        }
    }

    private boolean checkCondition(FunctionEvaluator functionEvaluator, UserContext definingUser,
                                   Condition condition, EventContext eventCtx, PDP pdp) throws PMException {
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
    private boolean checkNegatedCondition(FunctionEvaluator functionEvaluator, UserContext definingUser,
                                          NegatedCondition condition, EventContext eventCtx, PDP pdp) throws PMException {
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

    private void applyAction(PDP pdp, PAP pap, FunctionEvaluator functionEvaluator, UserContext definingUser,
                             String label, EventContext eventCtx, Action action) throws PMException {
        if (action == null) {
            return;
        }

        if(action instanceof AssignAction) {
            applyAssignAction(pdp, pap, functionEvaluator, definingUser, eventCtx, (AssignAction) action);
        } else if(action instanceof CreateAction) {
            applyCreateAction(pdp, pap, functionEvaluator, definingUser, label, eventCtx, (CreateAction) action);
        } else if(action instanceof DeleteAction) {
            applyDeleteAction(pdp, pap, functionEvaluator, definingUser, eventCtx, (DeleteAction) action);
        } else if(action instanceof DenyAction) {
            applyDenyAction(pdp, pap, functionEvaluator, definingUser, eventCtx, (DenyAction) action);
        } else if(action instanceof GrantAction) {
            applyGrantAction(pdp, pap, functionEvaluator, definingUser, eventCtx, (GrantAction) action);
        } else if(action instanceof FunctionAction) {
            functionEvaluator.evalNode(definingUser, eventCtx, pdp, ((FunctionAction) action).getFunction());
        }
    }

    private void applyGrantAction(PDP pdp, PAP pap, FunctionEvaluator functionEvaluator,
                                  UserContext definingUser, EventContext eventCtx, GrantAction action) throws PMException {
        EvrNode subject = action.getSubject();
        List<String> operations = action.getOperations();
        EvrNode target = action.getTarget();

        Node subjectNode = toNode(pdp, pap, functionEvaluator, definingUser, eventCtx, subject);
        Node targetNode = toNode(pdp, pap, functionEvaluator, definingUser, eventCtx, target);

        pap.getGraphAdmin().associate(subjectNode.getName(), targetNode.getName(), new OperationSet(operations));
    }

    private void applyDenyAction(PDP pdp, PAP pap, FunctionEvaluator functionEvaluator,
                                 UserContext definingUser, EventContext eventCtx, DenyAction action) throws PMException {
        EvrNode subject = action.getSubject();
        List<String> operations = action.getOperations();
        DenyAction.Target target = action.getTarget();

        String denySubject = toDenySubject(pdp, pap, functionEvaluator, definingUser, eventCtx, subject);
        Map<String, Boolean> denyNodes = toDenyNodes(pdp, pap, functionEvaluator, definingUser, eventCtx, target);

        Prohibition.Builder builder = new Prohibition.Builder(action.getLabel(), denySubject, new OperationSet(operations))
                .setIntersection(target.isIntersection());

        for(String contName : denyNodes.keySet()) {
            builder.addContainer(contName, denyNodes.get(contName));
        }

        // add the prohibition to the PAP
        pap.getProhibitionsAdmin().add(builder.build());

        // TODO this complement is ignored in the current Prohibition object
        boolean complement = target.isComplement();
    }

    private Map<String, Boolean> toDenyNodes(PDP pdp, PAP pap, FunctionEvaluator functionEvaluator,
                                             UserContext definingUser, EventContext eventCtx, DenyAction.Target target) throws PMException {
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
                Graph graph = pap.getGraphAdmin();

                // get the node
                Node node = graph.getNode(container.getName());
                nodes.put(node.getName(), container.isComplement());
            }
        }

        return nodes;
    }

    private String toDenySubject(PDP pdp, PAP pap, FunctionEvaluator functionEvaluator,
                                 UserContext definingUser, EventContext eventCtx, EvrNode subject) throws PMException {
        String denySubject;

        if(subject.getFunction() != null) {
            Function function = subject.getFunction();
            denySubject = functionEvaluator.evalString(definingUser, eventCtx, pdp, function);
        } else if(subject.getProcess() != null) {
            denySubject = subject.getProcess().getValue();
        } else {
            if (subject.getName() != null) {
                denySubject = pap.getGraphAdmin().getNode(subject.getName()).getName();
            } else {
                denySubject = pap.getGraphAdmin().getNode(NodeType.toNodeType(subject.getType()), subject.getProperties()).getName();
            }
        }

        return denySubject;
    }

    private void applyDeleteAction(PDP pdp, PAP pap, FunctionEvaluator functionEvaluator, UserContext definingUser, EventContext eventCtx, DeleteAction action) throws PMException {
        List<EvrNode> nodes = action.getNodes();
        if (nodes != null) {
            for (EvrNode evrNode : nodes) {
                Node node = toNode(pdp, pap, functionEvaluator, definingUser, eventCtx, evrNode);
                pap.getGraphAdmin().deleteNode(node.getName());
            }
        }

        AssignAction assignAction = action.getAssignments();
        if (assignAction != null) {
            for (AssignAction.Assignment assignment : assignAction.getAssignments()) {
                Node what = toNode(pdp, pap, functionEvaluator, definingUser, eventCtx, assignment.getWhat());
                Node where = toNode(pdp, pap, functionEvaluator, definingUser, eventCtx, assignment.getWhere());
                pap.getGraphAdmin().deassign(what.getName(), where.getName());
            }
        }

        List<GrantAction> associations = action.getAssociations();
        if (associations != null){
            for (GrantAction grantAction : associations) {
                Node subject = toNode(pdp, pap, functionEvaluator, definingUser, eventCtx, grantAction.getSubject());
                Node target = toNode(pdp, pap, functionEvaluator, definingUser, eventCtx, grantAction.getTarget());
                pap.getGraphAdmin().dissociate(subject.getName(), target.getName());
            }
        }

        List<String> prohibitions = action.getProhibitions();
        if (prohibitions != null) {
            for (String label : prohibitions) {
                pap.getProhibitionsAdmin().delete(label);
            }
        }

        List<String> rules = action.getRules();
        if (rules != null) {
            for (String label : rules) {
                List<Obligation> obligations = pap.getObligationsAdmin().getAll();
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

    private Node toNode(PDP pdp, PAP pap, FunctionEvaluator functionEvaluator,
                        UserContext definingUser, EventContext eventCtx, EvrNode evrNode) throws PMException {
        Node node;
        if(evrNode.getFunction() != null) {
            node = functionEvaluator.evalNode(definingUser, eventCtx, pdp, evrNode.getFunction());
        } else {
            if (evrNode.getName() != null && !evrNode.getName().isEmpty()) {
                node = pap.getGraphAdmin().getNode(evrNode.getName());
            } else {
                node = pap.getGraphAdmin().getNode(NodeType.toNodeType(evrNode.getType()), evrNode.getProperties());
            }
        }
        return node;
    }

    private void applyCreateAction(PDP pdp, PAP pap, FunctionEvaluator functionEvaluator,
                                   UserContext definingUser, String label, EventContext eventCtx, CreateAction action) throws PMException {
        List<Rule> rules = action.getRules();
        if (rules != null) {
            for (Rule rule : rules) {
                createRule(pap, label, eventCtx, rule);
            }
        }

        List<CreateAction.CreateNode> createNodesList = action.getCreateNodesList();
        if (createNodesList != null) {
            for (CreateAction.CreateNode createNode : createNodesList) {
                EvrNode what = createNode.getWhat();
                EvrNode where = createNode.getWhere();
                Node whereNode = toNode(pdp, pap, functionEvaluator, definingUser, eventCtx, where);
                pap.getGraphAdmin().createNode(what.getName(), NodeType.toNodeType(what.getType()), what.getProperties(), whereNode.getName());
            }
        }
    }

    private void createRule(PAP pap, String obligationLabel, EventContext eventCtx, Rule rule) throws PMException {
        // add the rule to the obligation
        Obligation obligation = pap.getObligationsAdmin().get(obligationLabel);
        List<Rule> rules = obligation.getRules();
        rules.add(rule);
        obligation.setRules(rules);
    }

    private void applyAssignAction(PDP pdp, PAP pap, FunctionEvaluator functionEvaluator,
                                   UserContext definingUser, EventContext eventCtx, AssignAction action) throws PMException {
        List<AssignAction.Assignment> assignments = action.getAssignments();
        if (assignments != null) {
            for (AssignAction.Assignment assignment : assignments) {
                EvrNode what = assignment.getWhat();
                EvrNode where = assignment.getWhere();

                Node whatNode = toNode(pdp, pap, functionEvaluator, definingUser, eventCtx, what);
                Node whereNode = toNode(pdp, pap, functionEvaluator, definingUser, eventCtx, where);

                pap.getGraphAdmin().assign(whatNode.getName(), whereNode.getName());
            }
        }
    }
}
