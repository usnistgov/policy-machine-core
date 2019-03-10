package gov.nist.csd.pm.audit.model;

import gov.nist.csd.pm.graph.model.nodes.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Path {

    private List<Edge> edges;

    public Path() {
        edges = new ArrayList<>();
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    public void insertEdge(Edge edge) {
        this.edges.add(0, edge);
    }

    public void addAll(Collection<Edge> edges) {
        this.edges.addAll(edges);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        boolean reachedAssoc = false;
        for(Edge edge : edges) {
            String source;
            String target;

            if(!reachedAssoc) {
                source = edge.source.getName();
                target = edge.target.getName();
            } else {
                target = edge.source.getName();
                source = edge.target.getName();
            }

            if(builder.length() == 0) {
                builder.append(source);
                builder.append("-");
            }
            if(edge.operations != null) {
                builder.append("[");
                builder.append(opsToStr(edge.operations));
                builder.append("]");
                builder.append("-");
                reachedAssoc = true;
            }
            builder.append(target);
            builder.append("-");
        }
        return builder.substring(0, builder.length()-1);
    }

    private String opsToStr(Set<String> ops) {
        StringBuilder builder = new StringBuilder();
        for(String op : ops) {
            if(builder.length() == 0) {
                builder.append(op);
            } else {
                builder.append(",").append(op);
            }
        }
        return builder.toString();
    }

    public static class Edge {
        private Node        source;
        private Node        target;
        private Set<String> operations;

        public Edge(Node source, Node target) {
            this.source = source;
            this.target = target;
        }

        public Edge(Node source, Node target, Set<String> operations) {
            this.source = source;
            this.target = target;
            this.operations = operations;
        }

        public Node getSource() {
            return source;
        }

        public Node getTarget() {
            return target;
        }

        public Set<String> getOperations() {
            return operations;
        }

        public boolean isAssociation() {
            return operations == null;
        }

        public String toString() {
            return "(" + source.getName() + "->" + target.getName() + (operations == null ? "" : operations) + ")";
        }
    }
}
