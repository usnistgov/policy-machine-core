package gov.nist.csd.pm.pip.memory;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;

public class MemGraphSerializer implements GraphSerializer {

    private MemGraph graph;

    public MemGraphSerializer(MemGraph graph) {
        this.graph = graph;
    }

    @Override
    public String serialize() throws PMException {
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

    @Override
    public Graph deserialize(String s) throws PMException {
        Scanner sc = new Scanner(s);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            }

            String[] pieces = line.split(" ");
            String cmd = pieces[0];
            switch (cmd) {
                case "node":
                    if (pieces.length < 3) {
                        throw new PMException("invalid node command: " + line);
                    }
                    // node <type> <name> <props>
                    String type = pieces[1];
                    String name = pieces[2];

                    Map<String, String> propsMap = new HashMap<>();
                    if (pieces.length > 3) {
                        String props = pieces[3].replaceAll("\\{", "").replaceAll("}", "");
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
                    if (node.getType().equals(PC)) {
                        graph.createPolicyClass(node.getName(), node.getProperties());
                    } else {
                        graph.graph.addVertex(name);
                        graph.nodes.put(name, node);
                    }
                    break;
                case "assign":
                    if (pieces.length != 3) {
                        throw new PMException("invalid assign command (expected only 3 tokens got " + pieces.length + ": " + line);
                    }

                    String child = pieces[1];
                    String parent = pieces[2];

                    graph.assign(child, parent);
                    break;
                case "assoc":
                    if (pieces.length < 4) {
                        throw new PMException("invalid assoc command: " + line);
                    }

                    String ua = pieces[1];
                    String target = pieces[2];

                    String opsStr = line.substring(line.indexOf("[")+1, line.lastIndexOf("]"));
                    String[] ops = opsStr.split("(,\\s+)");

                    graph.associate(ua, target, new OperationSet(ops));
                    break;
                default:
                    throw new PMException("invalid command: " + cmd);
            }
        }

        return graph;
    }
}
