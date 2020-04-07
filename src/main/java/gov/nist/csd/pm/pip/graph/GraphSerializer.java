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
     *       "name": "pc1",
     *       "type": "PC",
     *       "properties": {}
     *     },
     *     ...
     *   ],
     *   "assignments": [
     *     {
     *       "source": "ua",
     *       "target": "pc"
     *     },
     *     ...
     *   ],
     *   "associations": [
     *     {
     *       "operations": [
     *         "read",
     *         "write"
     *       ],
     *       "source": "ua",
     *       "target": "oa"
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
            Set<String> parents = graph.getParents(node.getName());

            for (String parent : parents) {
                jsonAssignments.add(new Assignment(node.getName(), parent));
            }

            Map<String, OperationSet> associations = graph.getSourceAssociations(node.getName());
            for (String target : associations.keySet()) {
                OperationSet ops = associations.get(target);
                Node targetNode = graph.getNode(target);

                jsonAssociations.add(new JsonAssociation(node.getName(), targetNode.getName(), ops));
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
        Map<String, Node> nodesMap = new HashMap<>();
        for (Node node : nodes) {
            if (node.getType().equals(PC)) {
                graph.createPolicyClass(node.getName(), node.getProperties());
            } else {
                nodesMap.put(node.getName(), node);
            }
        }

        List<Assignment> assignments = new ArrayList<>(jsonGraph.getAssignments());
        while (!assignments.isEmpty()) {
            Assignment assignment = assignments.get(0);
            String source = assignment.getSource();
            String target = assignment.getTarget();

            if (graph.exists(target)) {
                // create the source if it does not exist
                if (!graph.exists(source)) {
                    Node node = nodesMap.get(source);
                    graph.createNode(node.getName(), node.getType(), node.getProperties(), target);
                    assignments.remove(assignment);
                } else {
                    graph.assign(source, target);
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
            String ua = association.getSource();
            String target = association.getTarget();
            graph.associate(ua, target, new OperationSet(association.getOperations()));
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
        String source;
        String target;
        Set<String> operations;

        public JsonAssociation(String source, String target, Set<String> operations) {
            this.source = source;
            this.target = target;
            this.operations = operations;
        }

        public String getSource() {
            return source;
        }

        public String getTarget() {
            return target;
        }

        public Set<String> getOperations() {
            return operations;
        }
    }

    public static String serialize(Graph graph) throws PMException {
        String s = "# nodes\n";

        List<String> assignmentCmds = new ArrayList<>();

        Set<Node> search = graph.search(PC, null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";
        }

        s += "\n";
        search = graph.search(U, null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";

            Set<String> parents = graph.getParents(node.getName());
            for (String parent : parents) {
                Node parentNode = graph.getNode(parent);
                assignmentCmds.add("assign " + node.getName() + " " + parentNode.getName() + "\n");
            }
        }

        s += "\n";
        search = graph.search(UA, null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";
            Set<String> parents = graph.getParents(node.getName());
            for (String parent : parents) {
                Node parentNode = graph.getNode(parent);
                assignmentCmds.add("assign " + node.getName() + " " + parentNode.getName() + "\n");
            }
        }

        s += "\n";
        search = graph.search(O, null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";

            Set<String> parents = graph.getParents(node.getName());
            for (String parent : parents) {
                Node parentNode = graph.getNode(parent);
                assignmentCmds.add("assign " + node.getName() + " " + parentNode.getName() + "\n");
            }
        }

        s += "\n";
        search = graph.search(OA, null);
        for (Node node : search) {
            s += "node " + node.getType() + " " + node.getName() + " " +
                    (node.getProperties().isEmpty() ? "" : node.getProperties().toString().replaceAll(", ", ",")) + "\n";

            Set<String> parents = graph.getParents(node.getName());
            for (String parent : parents) {
                Node parentNode = graph.getNode(parent);
                assignmentCmds.add("assign " + node.getName() + " " + parentNode.getName() + "\n");
            }
        }

        Collection<Node> nodes = graph.getNodes();
        s += "\n# assignments\n";
        for (String cmd : assignmentCmds) {
            s += cmd + "\n";
        }

        s += "\n# associations\n";
        for (Node node : nodes) {
            Map<String, OperationSet> assocs = graph.getSourceAssociations(node.getName());
            for (String target : assocs.keySet()) {
                Node targetNode = graph.getNode(target);
                s += "assoc " + node.getName() + " " + targetNode.getName() + " " +
                        assocs.get(target).toString() + "\n";
            }
            s += "\n";
        }

        return s;
    }

    /**
     * Convert a string with the more readable configuration format generated by serialize above to a Graph. All names
     * must be unique and given the nature of the data format there must be no spaces.
     * @param graph the graph to load the configuration into.
     * @param str the configuration.
     * @return The graph with the loaded configuration.
     */
    public static Graph deserialize(Graph graph, String str) throws PMException {
        Scanner sc = new Scanner(str);

        Map<String, Node> nodesMap = new HashMap<>();
        Map<String, List<String>> assignments = new HashMap<>();
        List<Association> associations = new ArrayList<>();
        List<Node> pcs = new ArrayList<>();

        // load any existing nodes
        Set<String> policyClasses = graph.getPolicyClasses();
        for (String name : policyClasses) {
            pcs.add(graph.getNode(name));
        }

        Set<Node> nodes = graph.getNodes();
        for (Node node : nodes) {
            nodesMap.put(node.getName(), node);
        }

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

                    Node node = new Node(name, NodeType.valueOf(type), propsMap);
                    nodesMap.put(name, new Node(name, NodeType.valueOf(type), propsMap));
                    if (node.getType().equals(PC)) {
                        pcs.add(node);
                    }
                    break;
                case "assign":
                    if (pieces.length != 3) {
                        throw new PMException("invalid assign command (expected only 3 tokens got " + pieces.length + ": " + line);
                    }

                    String child = pieces[1];
                    String parent = pieces[2];

                    List<String> children = assignments.getOrDefault(parent, new ArrayList<>());
                    children.add(child);
                    assignments.put(parent, children);
                    break;
                case "assoc":
                    if (pieces.length < 4) {
                        throw new PMException("invalid assoc command: " + line);
                    }

                    String ua = pieces[1];
                    String target = pieces[2];

                    String opsStr = line.substring(line.indexOf("[")+1, line.lastIndexOf("]"));
                    String[] ops = opsStr.split("(,\\s+)");

                    associations.add(new Association(ua, target, new OperationSet(ops)));
                    break;
            }
        }

        // create policy class nodes
        for (Node node : pcs) {
            if (graph.getPolicyClasses().contains(node.getName())) {
                continue;
            }

            graph.createPolicyClass(node.getName(), node.getProperties());
        }

        for (Node node : pcs) {
            createPolicyGraph(graph, nodesMap, node.getName(), assignments);
        }

        for (Association association : associations) {
            graph.associate(association.getSource(), association.getTarget(), association.getOperations());
        }

        return graph;
    }



    // 1 create all policy classes
    // 2 get all assignments where a pc is the parent
    // 3 create all those nodes with pc as the initial parent
    // 4 get all the children of the newly created node and create them with the new node as a parent
    // repeat until all assignments are done
    // remove a parent entry after each iteration
    private static void createPolicyGraph(Graph graph, Map<String, Node> nodes, String parent, Map<String, List<String>> assignments) throws PMException {
        List<String> children = assignments.getOrDefault(parent, new ArrayList<>());
        if (assignments.isEmpty() || children.isEmpty()) {
            return;
        }

        for (String child : children) {
            if (!graph.exists(child)) {
                Node childNode = nodes.get(child);
                graph.createNode(childNode.getName(), childNode.getType(), childNode.getProperties(), parent);
            } else {
                if (!graph.isAssigned(child, parent)) {
                    graph.assign(child, parent);
                }
            }

            // do the same with the child as the parent
            createPolicyGraph(graph, nodes, child, assignments);

            assignments.remove(child);
        }

        assignments.remove(parent);
    }


}
