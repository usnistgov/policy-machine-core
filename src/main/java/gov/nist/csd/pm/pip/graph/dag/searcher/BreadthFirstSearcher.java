package gov.nist.csd.pm.pip.graph.dag.searcher;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.dag.propagator.Propagator;
import gov.nist.csd.pm.pip.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

import java.util.Set;

public class BreadthFirstSearcher implements Searcher{

    private Graph graph;

    public BreadthFirstSearcher(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void traverse(Node start, Propagator propagator, Visitor visitor) throws PMException {
        // visit the start node
        visitor.visit(start);

        Set<Node> parents = graph.getParents(start.getID());
        while(!parents.isEmpty()) {
            Node parent = parents.iterator().next();
            propagator.propagate(parent, start);
            visitor.visit(parent);
            parents.remove(parent.getID());
        }
    }
}
