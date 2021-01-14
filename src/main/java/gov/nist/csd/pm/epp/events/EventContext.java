package gov.nist.csd.pm.epp.events;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.dag.searcher.DepthFirstSearcher;
import gov.nist.csd.pm.pip.graph.dag.searcher.Direction;
import gov.nist.csd.pm.pip.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;

public class EventContext {

    public static final String ASSIGN_TO_EVENT = "assign to";
    public static final String ASSIGN_EVENT = "assign";
    public static final String DEASSIGN_FROM_EVENT = "deassign from";
    public static final String DEASSIGN_EVENT = "deassign";
    public static final String CREATE_NODE_EVENT = "create node";
    public static final String DELETE_NODE_EVENT = "delete node";
    public static final String ACCESS_DENIED_EVENT = "access denied";

    private UserContext userCtx;
    private String event;
    private Node   target;

    public EventContext(UserContext userCtx, String event, Node target) {
        this.userCtx = userCtx;
        this.event = event;
        this.target = target;
    }

    public UserContext getUserCtx() {
        return userCtx;
    }

    public String getEvent() {
        return event;
    }

    public Node getTarget() {
        return target;
    }

    public boolean matchesPattern(EventPattern pattern, Graph graph) throws PMException {
        // if the pattern given is not an EventPattern it is not a built in event and does not match
        if (!pattern.getClass().equals(EventPattern.class)) {
            return false;
        } else if(pattern.getOperations() != null &&
                !pattern.getOperations().contains(event)) {
            return false;
        }

        Subject matchSubject = pattern.getSubject();
        PolicyClass matchPolicyClass = pattern.getPolicyClass();
        Target matchTarget = pattern.getTarget();

        return subjectMatches(graph, userCtx.getUser(), userCtx.getProcess(), matchSubject) &&
                pcMatches(userCtx.getUser(), userCtx.getProcess(), matchPolicyClass) &&
                targetMatches(graph, target, matchTarget);
    }

    private boolean subjectMatches(Graph graph, String user, String process, Subject matchSubject) throws PMException {
        if(matchSubject == null) {
            return true;
        }

        // any user
        if((matchSubject.getAnyUser() == null && matchSubject.getUser() == null && matchSubject.getProcess() == null) ||
                (matchSubject.getAnyUser() != null && matchSubject.getAnyUser().isEmpty())) {
            return true;
        }

        // get the current user node
        Node userNode = graph.getNode(user);

        if (checkAnyUser(graph, userNode, matchSubject.getAnyUser())) {
            return true;
        }

        if(matchSubject.getUser() != null && matchSubject.getUser().equals(userNode.getName())) {
            return true;
        }

        return matchSubject.getProcess() != null &&
                matchSubject.getProcess().getValue().equals(process);
    }

    private boolean checkAnyUser(Graph graph, Node userNode, List<String> anyUser) throws PMException {
        if (anyUser == null || anyUser.isEmpty()) {
            return true;
        }

        DepthFirstSearcher dfs = new DepthFirstSearcher(graph);

        // check each user in the anyUser list
        // there can be users and user attributes
        for (String u : anyUser) {
            Node anyUserNode = graph.getNode(u);

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

    private boolean targetMatches(Graph graph, Node target, Target matchTarget) throws PMException {
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
            Set<Node> containers = getContainersOf(graph, target.getName());
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

    private Set<Node> getContainersOf(Graph graph, String name) throws PMException {
        Set<Node> nodes = new HashSet<>();
        Set<String> parents = graph.getParents(name);
        for (String parent : parents) {
            nodes.add(graph.getNode(parent));
            nodes.addAll(getContainersOf(graph, parent));
        }
        return nodes;
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
}
