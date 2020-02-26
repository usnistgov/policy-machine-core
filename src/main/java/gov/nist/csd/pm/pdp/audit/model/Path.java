package gov.nist.csd.pm.pdp.audit.model;

import gov.nist.csd.pm.pip.graph.model.nodes.Node;

import java.util.*;

public class Path {
    private Set<String> operations;
    private List<Node> nodes;

    public Path() {
        operations = new HashSet<>();
        nodes = new ArrayList<>();
    }

    public Path(Set<String> operations, List<Node> nodes) {
        this.operations = operations;
        this.nodes = nodes;
    }

    public Set<String> getOperations() {
        return operations;
    }

    public void setOperations(Set<String> operations) {
        this.operations = operations;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public String toString() {
        if(nodes.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for(Node node : nodes) {
            sb.append(node.getName()).append("(").append(node.getType()).append(")").append("-");
        }

        return sb.deleteCharAt(sb.length()-1).append(" ops=").append(operations).toString();
    }
}
