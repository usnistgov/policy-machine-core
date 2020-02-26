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

        Set<Long> parents = graph.getParents(start.getID());
        while(!parents.isEmpty()) {
            Long parent = parents.iterator().next();
            Node parentNode = graph.getNode(parent);

            propagator.propagate(parentNode, start);
            visitor.visit(parentNode);
            parents.remove(parent);
        }
    }
}
