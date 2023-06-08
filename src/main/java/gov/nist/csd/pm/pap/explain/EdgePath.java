package gov.nist.csd.pm.pap.explain;

import gov.nist.csd.pm.common.graph.relationship.Relationship;

import java.util.ArrayList;
import java.util.List;

public class EdgePath {
    private List<Relationship> edges;

    public EdgePath() {
        this.edges = new ArrayList<>();
    }

    public EdgePath(List<Relationship> edges) {
        this.edges = edges;
    }

    public List<Relationship> getEdges() {
        return edges;
    }

    public void addEdge(Relationship e) {
        this.edges.add(e);
    }

    public String toString() {
        return edges.toString();
    }

    public List<String> toPath() {
        List<String> nodes = new ArrayList<>();
        for (Relationship edge : edges) {
            nodes.add(edge.getSource());
        }

        Relationship last = edges.get(edges.size() - 1);
        nodes.add(last.getTarget());

        return nodes;
    }
}

