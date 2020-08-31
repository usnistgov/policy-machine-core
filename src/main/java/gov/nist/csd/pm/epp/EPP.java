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

    public EPP(PAP pap, PDP pdp, EPPOptions eppOptions) throws PMException {
        this.pap = pap;
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
    
    public PDP getPDP() {
        return pdp;
    }

    public void processEvent(EventContext eventCtx) throws PMException {
        List<Obligation> obligations = pap.getObligationsAdmin().getAll();
        for(Obligation obligation : obligations) {
            if (!obligation.isEnabled()) {
                continue;
            }

            UserContext definingUser = new UserContext(obligation.getUser());

            List<Rule> rules = obligation.getRules();
            for(Rule rule : rules) {
                if(!eventCtx.matchesPattern(rule.getEventPattern(), pap.getGraphAdmin())) {
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

        pap.getGraphAdmin().associate(subjectNode.getName(), targetNode.getName(), new OperationSet(operations));
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
        pap.getProhibitionsAdmin().add(builder.build());

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
                Graph graph = pap.getGraphAdmin();

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
                denySubject = pap.getGraphAdmin().getNode(subject.getName()).getName();
            } else {
                denySubject = pap.getGraphAdmin().getNode(NodeType.toNodeType(subject.getType()), subject.getProperties()).getName();
            }
        }

        return denySubject;
    }

    private void applyDeleteAction(UserContext definingUser, EventContext eventCtx, DeleteAction action) throws PMException {
        List<EvrNode> nodes = action.getNodes();
        if (nodes != null) {
            for (EvrNode evrNode : nodes) {
                Node node = toNode(definingUser, eventCtx, evrNode);
                pap.getGraphAdmin().deleteNode(node.getName());
            }
        }

        AssignAction assignAction = action.getAssignments();
        if (assignAction != null) {
            for (AssignAction.Assignment assignment : assignAction.getAssignments()) {
                Node what = toNode(definingUser, eventCtx, assignment.getWhat());
                Node where = toNode(definingUser, eventCtx, assignment.getWhere());
                pap.getGraphAdmin().deassign(what.getName(), where.getName());
            }
        }

        List<GrantAction> associations = action.getAssociations();
        if (associations != null){
            for (GrantAction grantAction : associations) {
                Node subject = toNode(definingUser, eventCtx, grantAction.getSubject());
                Node target = toNode(definingUser, eventCtx, grantAction.getTarget());
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

    private Node toNode(UserContext definingUser, EventContext eventCtx, EvrNode evrNode) throws PMException {
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
                pap.getGraphAdmin().createNode(what.getName(), NodeType.toNodeType(what.getType()), what.getProperties(), whereNode.getName());
            }
        }
    }

    private void createRule(String obligationLabel, EventContext eventCtx, Rule rule) throws PMException {
        // add the rule to the obligation
        Obligation obligation = pap.getObligationsAdmin().get(obligationLabel);
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

                pap.getGraphAdmin().assign(whatNode.getName(), whereNode.getName());
            }
        }
    }
}
