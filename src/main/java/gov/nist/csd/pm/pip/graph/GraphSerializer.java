package gov.nist.csd.pm.pip.graph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.graph.model.relationships.Assignment;
import gov.nist.csd.pm.pip.graph.model.relationships.Association;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;

public class GraphSerializer {

    private GraphSerializer() {
    }

    /**
     * Given a Graph interface, serialize the graph to a json string.
     *
     * Here is an example of the format:
     * {
     *   "nodes": [
     *     {
     *       "id": 1,
     *       "name": "pc1",
     *       "type": "PC",
     *       "properties": {}
     *     },
     *     ...
     *   ],
     *   "assignments": [
     *     {
     *       "sourceID": 2,
     *       "targetID": 1
     *     },
     *     ...
     *   ],
     *   "associations": [
     *     {
     *       "operations": [
     *         "read",
     *         "write"
     *       ],
     *       "sourceID": 4,
     *       "targetID": 2
     *     }
     *   ]
     * }
     *
     *
     * @param graph the graph to serialize.
     * @return a json string representation of the given graph.
     * @throws PMException if there is an error accessing the graph.
     */
    public static String toJson(Graph graph) throws PMException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Collection<Node> nodes = graph.getNodes();
        HashSet<Assignment> jsonAssignments = new HashSet<>();
        HashSet<JsonAssociation> jsonAssociations = new HashSet<>();
        for (Node node : nodes) {
            Set<Long> parents = graph.getParents(node.getID());

            for (Long parent : parents) {
                jsonAssignments.add(new Assignment(node.getID(), parent));
            }

            Map<Long, OperationSet> associations = graph.getSourceAssociations(node.getID());
            for (long targetID : associations.keySet()) {
                OperationSet ops = associations.get(targetID);
                Node targetNode = graph.getNode(targetID);

                jsonAssociations.add(new JsonAssociation(node.getID(), targetNode.getID(), ops));
            }
        }

        return gson.toJson(new JsonGraph(nodes, jsonAssignments, jsonAssociations));
    }

    /**
     * Given a json string, deserialize it into the provided Graph implementation.
     *
     * @param graph the graph to deserialize the json into.
     * @param json the json string to deserialize.
     * @return the provided Graph implementation with the data from the json string.
     * @throws PMException if there is an error converting the string to a Graph.
     */
    public static void fromJson(Graph graph, String json) throws PMException {
        JsonGraph jsonGraph = new Gson().fromJson(json, JsonGraph.class);

        Collection<Node> nodes = jsonGraph.getNodes();
        Map<Long, Node> nodesMap = new HashMap<>();
        for (Node node : nodes) {
            if (node.getType().equals(PC)) {
                graph.createPolicyClass(node.getID(), node.getName(), node.getProperties());
            } else {
                nodesMap.put(node.getID(), node);
            }
        }

        List<Assignment> assignments = new ArrayList<>(jsonGraph.getAssignments());
        while (!assignments.isEmpty()) {
            Assignment assignment = assignments.get(0);
            long sourceID = assignment.getSourceID();
            long targetID = assignment.getTargetID();

            if (graph.exists(targetID)) {
                // create the source if it does not exist
                if (!graph.exists(sourceID)) {
                    Node node = nodesMap.get(sourceID);
                    graph.createNode(node.getID(), node.getName(), node.getType(), node.getProperties(), targetID);
                    assignments.remove(assignment);
                } else {
                    graph.assign(sourceID, targetID);
                    assignments.remove(assignment);
                }
                continue;
            }
            // remove the current assignment and add it to the end to be processed later
            assignments.remove(0);
            assignments.add(assignment);
        }

        Set<JsonAssociation> associations = jsonGraph.getAssociations();
        for (JsonAssociation association : associations) {
            long uaID = association.getSourceID();
            long targetID = association.getTargetID();
            graph.associate(uaID, targetID, new OperationSet(association.getOperations()));
        }
    }

    private static class JsonGraph {
        Collection<Node> nodes;
        Set<Assignment>  assignments;
        Set<JsonAssociation> associations;

        JsonGraph(Collection<Node> nodes, Set<Assignment> assignments, Set<JsonAssociation> associations) {
            this.nodes = nodes;
            this.assignments = assignments;
            this.associations = associations;
        }

        Collection<Node> getNodes() {
            return nodes;
        }

        Set<Assignment> getAssignments() {
            return assignments;
        }

        Set<JsonAssociation> getAssociations() {
            return associations;
        }
    }

    private static class JsonAssociation {
        long sourceID;
        long targetID;
        Set<String> operations;

        public JsonAssociation(long sourceID, long targetID, Set<String> operations) {
            this.sourceID = sourceID;
            this.targetID = targetID;
            this.operations = operations;
        }

        public long getSourceID() {
            return sourceID;
        }

        public long getTargetID() {
            return targetID;
        }

        public Set<String> getOperations() {
            return operations;
        }
    }

    public static String serialize(Graph graph) throws PMException {
        String s = "# nodes\n";

        List<String> assignmentCmds = new ArrayList<>();

        Set<Node> search = graph.search(null, PC, null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";
        }

        s += "\n";
        search = graph.search(null, U, null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";

            Set<Long> parents = graph.getParents(node.getID());
            for (long parentID : parents) {
                Node parentNode = graph.getNode(parentID);
                assignmentCmds.add("assign " + node.getType() + ":" + node.getName() + " " + parentNode.getType() + ":" + parentNode.getName() + "\n");
            }
        }

        s += "\n";
        search = graph.search(null, UA, null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";
            Set<Long> parents = graph.getParents(node.getID());
            for (long parentID : parents) {
                Node parentNode = graph.getNode(parentID);
                assignmentCmds.add("assign " + node.getType() + ":" + node.getName() + " " + parentNode.getType() + ":" + parentNode.getName() + "\n");
            }
        }

        s += "\n";
        search = graph.search(null, O, null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";

            Set<Long> parents = graph.getParents(node.getID());
            for (long parentID : parents) {
                Node parentNode = graph.getNode(parentID);
                assignmentCmds.add("assign " + node.getType() + ":" + node.getName() + " " + parentNode.getType() + ":" + parentNode.getName() + "\n");
            }
        }

        s += "\n";
        search = graph.search(null, OA, null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";

            Set<Long> parents = graph.getParents(node.getID());
            for (long parentID : parents) {
                Node parentNode = graph.getNode(parentID);
                assignmentCmds.add("assign " + node.getType() + ":" + node.getName() + " " + parentNode.getType() + ":" + parentNode.getName() + "\n");
            }
        }

        Collection<Node> nodes = graph.getNodes();
        s += "\n# assignments\n";
        for (String cmd : assignmentCmds) {
            s += cmd + "\n";
        }

        s += "\n# associations\n";
        for (Node node : nodes) {
            Map<Long, OperationSet> assocs = graph.getSourceAssociations(node.getID());
            for (Long targetID : assocs.keySet()) {
                Node targetNode = graph.getNode(targetID);
                s += "assoc " +
                        node.getType() + ":" + node.getName() + " " +
                        targetNode.getType() + ":" + targetNode.getName() + " " +
                        assocs.get(targetID).toString() + "\n";
            }
            s += "\n";
        }

        return s;
    }

    public static Graph deserialize(Graph graph, String str) throws PMException {
        Scanner sc = new Scanner(str);
        Random rand = new Random();

        Map<String, Long> ids = new HashMap<>();
        Map<Long, Node> nodesMap = new HashMap<>();
        List<Assignment> assignments = new ArrayList<>();
        List<Association> associations = new ArrayList<>();
        List<Node> pcs = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            }

            String[] pieces = line.split(" ");
            switch (pieces[0]) {
                case "node":
                    if (pieces.length < 3) {
                        throw new PMException("invalid node command: " + line);
                    }
                    // node <type> <name> <props>
                    String type = pieces[1];

                    String name = pieces[2];
                    int i;
                    for (i = 3; i < pieces.length; i++) {
                        String piece = pieces[i];
                        if (piece.startsWith("{")) {
                            break;
                        }

                        name += " " + piece;
                    }

                    String props = "";
                    Map<String, String> propsMap = new HashMap<>();
                    if (i == pieces.length-1) {
                        props = pieces[i];
                        props = props.replaceAll("\\{", "").replaceAll("}", "");
                        String[] propsPieces = props.split(",");
                        for (String prop : propsPieces) {
                            String[] propPieces = prop.split("=");
                            if (propPieces.length != 2) {
                                throw new PMException("invalid property format: " + line);
                            }
                            propsMap.put(propPieces[0], propPieces[1]);
                        }
                    }

                    long id = rand.nextLong();
                    Node node = new Node(id, name, NodeType.valueOf(type), propsMap);
                    nodesMap.put(id, new Node(id, name, NodeType.valueOf(type), propsMap));
                    ids.put(node.getType() + ":" + node.getName(), node.getID());
                    if (node.getType().equals(PC)) {
                        pcs.add(node);
                    }
                    break;
                case "assign":
                    if (pieces.length < 3) {
                        throw new PMException("invalid assign command: " + line);
                    }

                    name = pieces[1];
                    for (i = 2; i < pieces.length; i++) {
                        String piece = pieces[i];
                        if (piece.contains(":")) {
                            break;
                        }

                        name += " " + piece;
                    }
                    long childID = ids.get(name);

                    name = pieces[i];
                    i++;
                    for (int j = i; j < pieces.length; j++) {
                        String piece = pieces[j];
                        name += " " + piece;
                    }
                    long parentID = ids.get(name);

                    assignments.add(new Assignment(childID, parentID));

                    break;
                case "assoc":
                    if (pieces.length < 4) {
                        throw new PMException("invalid assoc command: " + line);
                    }

                    name = pieces[1];
                    for (i = 2; i < pieces.length; i++) {
                        String piece = pieces[i];
                        if (piece.contains(":")) {
                            break;
                        }

                        name += " " + piece;
                    }
                    long uaID = ids.get(name);

                    name = pieces[i];
                    i++;
                    for (int j = i; j < pieces.length; j++) {
                        String piece = pieces[j];
                        if (piece.contains("[")) {
                            break;
                        }

                        name += " " + piece;
                    }
                    long targetID = ids.get(name);

                    String opsStr = line.substring(line.indexOf("[")+1, line.lastIndexOf("]"));
                    String[] ops = opsStr.split("(,\\s+)");

                    associations.add(new Association(uaID, targetID, new OperationSet(ops)));

                    break;
            }
        }

        // create policy class nodes
        for (Node node : pcs) {
            graph.createPolicyClass(node.getID(), node.getName(), node.getProperties());
        }

        while (!assignments.isEmpty()) {
            Assignment assignment = assignments.get(0);
            long sourceID = assignment.getSourceID();
            long targetID = assignment.getTargetID();

            if (graph.exists(targetID)) {
                // create the source if it does not exist
                if (!graph.exists(sourceID)) {
                    Node node = nodesMap.get(sourceID);
                    graph.createNode(node.getID(), node.getName(), node.getType(), node.getProperties(), targetID);
                    assignments.remove(assignment);
                } else {
                    graph.assign(sourceID, targetID);
                    assignments.remove(assignment);
                }
                continue;
            }
            // remove the current assignment and add it to the end to be processed later
            assignments.remove(0);
            assignments.add(assignment);
        }

        for (Association association : associations) {
            graph.associate(association.getSourceID(), association.getTargetID(), association.getOperations());
        }

        return graph;
    }
}
