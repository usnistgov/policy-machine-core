package gov.nist.csd.pm.pip.graph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.graph.model.relationships.*;

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
        HashSet<Association> jsonAssociations = new HashSet<>();
        for (Node node : nodes) {
            Set<Long> parents = graph.getParents(node.getID());

            for (Long parent : parents) {
                jsonAssignments.add(new Assignment(node.getID(), parent));
            }

            Map<Long, Set<String>> associations = graph.getSourceAssociations(node.getID());
            for (long targetID : associations.keySet()) {
                Set<String> ops = associations.get(targetID);
                Node targetNode = graph.getNode(targetID);

                jsonAssociations.add(new Association(node.getID(), targetNode.getID(), ops));
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
    public static Graph fromJson(Graph graph, String json) throws PMException {
        JsonGraph jsonGraph = new Gson().fromJson(json, JsonGraph.class);

        Collection<Node> nodes = jsonGraph.getNodes();
        for (Node node : nodes) {
            graph.createNode(node.getID(), node.getName(), node.getType(), node.getProperties());
        }

        Set<Assignment> assignments = jsonGraph.getAssignments();
        for (Assignment assignment : assignments) {
            graph.assign(assignment.getSourceID(), assignment.getTargetID());
        }

        Set<Association> associations = jsonGraph.getAssociations();
        for (Association association : associations) {
            long uaID = association.getSourceID();
            long targetID = association.getTargetID();
            graph.associate(uaID, targetID, new OperationSet(association.getOperations()));
        }

        return graph;
    }

    private static class JsonGraph {
        Collection<Node> nodes;
        Set<Assignment>  assignments;
        Set<Association> associations;

        JsonGraph(Collection<Node> nodes, Set<Assignment> assignments, Set<Association> associations) {
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

        Set<Association> getAssociations() {
            return associations;
        }
    }

    public static String serialize(Graph graph) throws PMException {
        String s = "# nodes\n";

        Set<Node> search = graph.search(null, PC.toString(), null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";
        }

        s += "\n";
        search = graph.search(null, U.toString(), null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";
        }

        s += "\n";
        search = graph.search(null, UA.toString(), null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";
        }

        s += "\n";
        search = graph.search(null, OA.toString(), null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";
        }

        s += "\n";
        search = graph.search(null, O.toString(), null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";
        }

        Collection<Node> nodes = graph.getNodes();
        s += "\n# assignments\n";
        for (Node node : nodes) {
            Set<Long> parents = graph.getParents(node.getID());
            for (Long parentID : parents) {
                Node parentNode = graph.getNode(parentID);
                s += "assign " + node.getType() + ":" + node.getName() + " " + parentNode.getType() + ":" + parentNode.getName() + "\n";
            }
            s += "\n";
        }

        s += "\n# associations\n";
        for (Node node : nodes) {
            Map<Long, Set<String>> assocs = graph.getSourceAssociations(node.getID());
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

    static class NodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node o, Node t1) {
            return o.getType().compareTo(t1.getType());
        }
    }

    public static Graph deserialize(Graph graph, String str) throws PMException {
        Scanner sc = new Scanner(str);
        Random rand = new Random();
        Map<String, Long> ids = new HashMap<>();
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

                    Node node = graph.createNode(rand.nextLong(), name, NodeType.toNodeType(type), propsMap);
                    ids.put(node.getType() + ":" + node.getName(), node.getID());
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

                    graph.assign(childID, parentID);

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
                    graph.associate(uaID, targetID, new OperationSet(Arrays.asList(ops)));
                    break;
            }
        }

        return graph;
    }
}
