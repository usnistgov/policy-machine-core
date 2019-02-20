package gov.nist.csd.pm.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.Graph;
import gov.nist.csd.pm.graph.MemGraph;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.graph.model.relationships.Assignment;
import gov.nist.csd.pm.graph.model.relationships.Association;

import java.util.*;

import static gov.nist.csd.pm.graph.model.nodes.NodeType.UA;

public class GraphSerializer {

    private GraphSerializer() {}

    public static String toJson(Graph graph) throws PMException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Collection<NodeContext> nodes = graph.getNodes();
        HashSet<Assignment> jsonAssignments = new HashSet<>();
        HashSet<Association> jsonAssociations = new HashSet<>();
        for(NodeContext node : nodes) {
            Set<Long> parents = graph.getParents(node.getID());

            for (Long parent : parents) {
                jsonAssignments.add(new Assignment(node.getID(), parent));
            }

            Map<Long, Set<String>> associations = graph.getSourceAssociations(node.getID());
            for (long targetID : associations.keySet()) {
                Set<String> ops = associations.get(targetID);
                NodeContext targetNode = graph.getNode(targetID);

                jsonAssociations.add(new Association(node.getID(), targetNode.getID(), ops));
            }
        }

        return gson.toJson(new JsonGraph(nodes, jsonAssignments, jsonAssociations));
    }

    public static Graph fromJson(String json) throws PMException {
        Graph graph = new MemGraph();
        JsonGraph jsonGraph = new Gson().fromJson(json, JsonGraph.class);

        Collection<NodeContext> nodes = graph.getNodes();
        HashMap<Long, NodeContext> nodesMap = new HashMap<>();
        for(NodeContext node : nodes) {
            long newNodeID = graph.createNode(node);
            nodesMap.put(node.getID(), node.id(newNodeID));
        }

        Set<Assignment> assignments = jsonGraph.getAssignments();
        for(Assignment assignment : assignments) {
            NodeContext childCtx = nodesMap.get(assignment.getSourceID());
            NodeContext parentCtx = nodesMap.get(assignment.getTargetID());
            graph.assign(childCtx, parentCtx);
        }

        Set<Association> associations = jsonGraph.getAssociations();
        for(Association association : associations) {
            long uaID = association.getSourceID();
            long targetID = association.getTargetID();
            NodeContext targetNode = nodesMap.get(targetID);
            graph.associate(new NodeContext(nodesMap.get(uaID).getID(), UA), new NodeContext(targetNode.getID(), targetNode.getType()), association.getOperations());
        }

        return graph;
    }

    static class JsonGraph {
        Collection<NodeContext>     nodes;
        Set<Assignment>  assignments;
        Set<Association> associations;

        public JsonGraph(Collection<NodeContext> nodes, Set<Assignment> assignments, Set<Association> associations) {
            this.nodes = nodes;
            this.assignments = assignments;
            this.associations = associations;
        }

        public Collection<NodeContext> getNodes() {
            return nodes;
        }

        public Set<Assignment> getAssignments() {
            return assignments;
        }

        public Set<Association> getAssociations() {
            return associations;
        }
    }
}
