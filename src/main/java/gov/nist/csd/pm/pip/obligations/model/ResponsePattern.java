package gov.nist.csd.pm.pip.obligations.model;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.actions.*;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
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

    public void apply(Graph graph, Prohibitions prohibitions, Obligations obligations,
                      FunctionEvaluator functionEvaluator, EventContext eventCtx, Rule rule, String obligationLabel) throws PMException {
        // check the response condition
        ResponsePattern responsePattern = rule.getResponsePattern();
        if(!checkCondition(graph, prohibitions, obligations, functionEvaluator, responsePattern.getCondition(), eventCtx) ||
                !checkNegatedCondition(graph, prohibitions, obligations, functionEvaluator, responsePattern.getNegatedCondition(), eventCtx)) {
            return;
        }

        for(Action action : rule.getResponsePattern().getActions()) {
            if(!checkCondition(graph, prohibitions, obligations, functionEvaluator, action.getCondition(), eventCtx)) {
                continue;
            } else if(!checkNegatedCondition(graph, prohibitions, obligations, functionEvaluator, action.getNegatedCondition(), eventCtx)) {
                continue;
            }

            applyAction(graph, prohibitions, obligations, functionEvaluator, obligationLabel, eventCtx, action);
        }
    }

    private boolean checkCondition(Graph graph, Prohibitions prohibitions, Obligations obligations,
                                   FunctionEvaluator functionEvaluator, Condition condition, EventContext eventCtx) throws PMException {
        if(condition == null) {
            return true;
        }

        List<Function> functions = condition.getCondition();
        for(Function f : functions) {
            boolean result = functionEvaluator.evalBool(graph, prohibitions, obligations, eventCtx, f);
            if(!result) {
                return false;
            }
        }

        return true;
    }

    /**
     * Return true if the condition is satisfied. A condition is satisfied if all the functions evaluate to false.
     */
    private boolean checkNegatedCondition(Graph graph, Prohibitions prohibitions, Obligations obligations,
                                          FunctionEvaluator functionEvaluator, NegatedCondition condition, EventContext eventCtx) throws PMException {
        if(condition == null) {
            return true;
        }

        List<Function> functions = condition.getCondition();
        for(Function f : functions) {
            boolean result = functionEvaluator.evalBool(graph, prohibitions, obligations, eventCtx, f);
            if(result) {
                return false;
            }
        }

        return true;
    }

    private void applyAction(Graph graph, Prohibitions prohibitions, Obligations obligations, FunctionEvaluator functionEvaluator,
                             String label, EventContext eventCtx, Action action) throws PMException {
        if (action == null) {
            return;
        }

        if(action instanceof AssignAction) {
            applyAssignAction(graph, prohibitions, obligations, functionEvaluator, eventCtx, (AssignAction) action);
        } else if(action instanceof CreateAction) {
            applyCreateAction(graph, prohibitions, obligations, functionEvaluator, label, eventCtx, (CreateAction) action);
        } else if(action instanceof DeleteAction) {
            applyDeleteAction(graph, prohibitions, obligations, functionEvaluator, eventCtx, (DeleteAction) action);
        } else if(action instanceof DenyAction) {
            applyDenyAction(graph, prohibitions, obligations, functionEvaluator, eventCtx, (DenyAction) action);
        } else if(action instanceof GrantAction) {
            applyGrantAction(graph, prohibitions, obligations, functionEvaluator, eventCtx, (GrantAction) action);
        } else if(action instanceof FunctionAction) {
            functionEvaluator.evalObject(graph, prohibitions, obligations, eventCtx, ((FunctionAction) action).getFunction());
        }
    }

    private void applyGrantAction(Graph graph, Prohibitions prohibitions, Obligations obligations,
                                  FunctionEvaluator functionEvaluator, EventContext eventCtx, GrantAction action) throws PMException {
        EvrNode subject = action.getSubject();
        List<String> operations = action.getOperations();
        EvrNode target = action.getTarget();

        Node subjectNode = toNode(graph, prohibitions, obligations, functionEvaluator, eventCtx, subject);
        Node targetNode = toNode(graph, prohibitions, obligations, functionEvaluator, eventCtx, target);

        graph.associate(subjectNode.getName(), targetNode.getName(), new OperationSet(operations));
    }

    private void applyDenyAction(Graph graph, Prohibitions prohibitions, Obligations obligations,
                                 FunctionEvaluator functionEvaluator, EventContext eventCtx, DenyAction action) throws PMException {
        EvrNode subject = action.getSubject();
        List<String> operations = action.getOperations();
        DenyAction.Target target = action.getTarget();

        String denySubject = toDenySubject(graph, prohibitions, obligations, functionEvaluator, eventCtx, subject);
        Map<String, Boolean> denyNodes = toDenyNodes(graph, prohibitions, obligations, functionEvaluator, eventCtx, target);

        Prohibition.Builder builder = new Prohibition.Builder(action.getLabel(), denySubject, new OperationSet(operations))
                .setIntersection(target.isIntersection());

        for(String contName : denyNodes.keySet()) {
            builder.addContainer(contName, denyNodes.get(contName));
        }

        // add the prohibition to the PAP
        prohibitions.add(builder.build());

        // TODO this complement is ignored in the current Prohibition object
        boolean complement = target.isComplement();
    }

    private Map<String, Boolean> toDenyNodes(Graph graph, Prohibitions prohibitions, Obligations obligations,
                                             FunctionEvaluator functionEvaluator, EventContext eventCtx, DenyAction.Target target) throws PMException {
        Map<String, Boolean> nodes = new HashMap<>();
        List<DenyAction.Target.Container> containers = target.getContainers();
        for(DenyAction.Target.Container container : containers) {
            if(container.getFunction() != null) {
                Function function = container.getFunction();
                Object result = functionEvaluator.evalObject(graph, prohibitions, obligations, eventCtx, function);

                if(!(result instanceof ContainerCondition)) {
                    throw new PMException("expected function to return a ContainerCondition but got " + result.getClass().getName());
                }

                ContainerCondition cc = (ContainerCondition) result;
                nodes.put(cc.getName(), cc.isComplement());
            } else {
                // get the node
                Node node = graph.getNode(container.getName());
                nodes.put(node.getName(), container.isComplement());
            }
        }

        return nodes;
    }

    private String toDenySubject(Graph graph, Prohibitions prohibitions, Obligations obligations,
                                 FunctionEvaluator functionEvaluator, EventContext eventCtx, EvrNode subject) throws PMException {
        String denySubject;

        if(subject.getFunction() != null) {
            Function function = subject.getFunction();
            denySubject = functionEvaluator.evalString(graph, prohibitions, obligations, eventCtx, function);
        } else if(subject.getProcess() != null) {
            denySubject = subject.getProcess().getValue();
        } else {
            if (subject.getName() != null) {
                denySubject = graph.getNode(subject.getName()).getName();
            } else {
                denySubject = graph.getNode(NodeType.toNodeType(subject.getType()), subject.getProperties()).getName();
            }
        }

        return denySubject;
    }

    private void applyDeleteAction(Graph graph, Prohibitions prohibitions, Obligations obligations,
                                   FunctionEvaluator functionEvaluator, EventContext eventCtx, DeleteAction action) throws PMException {
        List<EvrNode> nodes = action.getNodes();
        if (nodes != null) {
            for (EvrNode evrNode : nodes) {
                Node node = toNode(graph, prohibitions, obligations, functionEvaluator, eventCtx, evrNode);
                graph.deleteNode(node.getName());
            }
        }

        AssignAction assignAction = action.getAssignments();
        if (assignAction != null) {
            for (AssignAction.Assignment assignment : assignAction.getAssignments()) {
                Node what = toNode(graph, prohibitions, obligations, functionEvaluator, eventCtx, assignment.getWhat());
                Node where = toNode(graph, prohibitions, obligations, functionEvaluator, eventCtx, assignment.getWhere());
                graph.deassign(what.getName(), where.getName());
            }
        }

        List<GrantAction> associations = action.getAssociations();
        if (associations != null){
            for (GrantAction grantAction : associations) {
                Node subject = toNode(graph, prohibitions, obligations, functionEvaluator, eventCtx, grantAction.getSubject());
                Node target = toNode(graph, prohibitions, obligations, functionEvaluator, eventCtx, grantAction.getTarget());
                graph.dissociate(subject.getName(), target.getName());
            }
        }

        List<String> actionProhibitions = action.getProhibitions();
        if (prohibitions != null) {
            for (String label : actionProhibitions) {
                prohibitions.delete(label);
            }
        }

        List<String> rules = action.getRules();
        if (rules != null) {
            for (String label : rules) {
                List<Obligation> allObligations = obligations.getAll();
                for (Obligation obligation : allObligations) {
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

    private Node toNode(Graph graph, Prohibitions prohibitions, Obligations obligations,
                        FunctionEvaluator functionEvaluator, EventContext eventCtx, EvrNode evrNode) throws PMException {
        Node node;
        if(evrNode.getFunction() != null) {
            node = functionEvaluator.evalNode(graph, prohibitions, obligations, eventCtx, evrNode.getFunction());
        } else {
            if (evrNode.getName() != null && !evrNode.getName().isEmpty()) {
                node = graph.getNode(evrNode.getName());
            } else {
                node = graph.getNode(NodeType.toNodeType(evrNode.getType()), evrNode.getProperties());
            }
        }
        return node;
    }

    private void applyCreateAction(Graph graph, Prohibitions prohibitions, Obligations obligations,
                                   FunctionEvaluator functionEvaluator, String label, EventContext eventCtx, CreateAction action) throws PMException {
        List<Rule> rules = action.getRules();
        if (rules != null) {
            for (Rule rule : rules) {
                createRule(graph, prohibitions, obligations, label, eventCtx, rule);
            }
        }

        List<CreateAction.CreateNode> createNodesList = action.getCreateNodesList();
        if (createNodesList != null) {
            for (CreateAction.CreateNode createNode : createNodesList) {
                EvrNode what = createNode.getWhat();
                EvrNode where = createNode.getWhere();
                Node whereNode = toNode(graph, prohibitions, obligations, functionEvaluator, eventCtx, where);
                graph.createNode(what.getName(), NodeType.toNodeType(what.getType()), what.getProperties(), whereNode.getName());
            }
        }
    }

    private void createRule(Graph graph, Prohibitions prohibitions, Obligations obligations,
                            String obligationLabel, EventContext eventCtx, Rule rule) throws PMException {
        // add the rule to the obligation
        Obligation obligation = obligations.get(obligationLabel);
        List<Rule> rules = obligation.getRules();
        rules.add(rule);
        obligation.setRules(rules);
        obligations.update(obligationLabel, obligation);
    }

    private void applyAssignAction(Graph graph, Prohibitions prohibitions, Obligations obligations,
                                   FunctionEvaluator functionEvaluator, EventContext eventCtx, AssignAction action) throws PMException {
        List<AssignAction.Assignment> assignments = action.getAssignments();
        if (assignments != null) {
            for (AssignAction.Assignment assignment : assignments) {
                EvrNode what = assignment.getWhat();
                EvrNode where = assignment.getWhere();

                Node whatNode = toNode(graph, prohibitions, obligations, functionEvaluator, eventCtx, what);
                Node whereNode = toNode(graph, prohibitions, obligations, functionEvaluator, eventCtx, where);

                graph.assign(whatNode.getName(), whereNode.getName());
            }
        }
    }
}
