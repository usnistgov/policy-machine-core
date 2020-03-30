package gov.nist.csd.pm.pip.graph.dag.searcher;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.dag.propagator.Propagator;
import gov.nist.csd.pm.pip.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

import java.util.HashSet;
import java.util.Set;

public class DepthFirstSearcher implements Searcher{

    private Graph graph;
    private HashSet<String> visited;

    public DepthFirstSearcher(Graph graph) {
        this.graph = graph;
        this.visited = new HashSet<>();
    }

    @Override
    public void traverse(Node start, Propagator propagator, Visitor visitor, Direction direction) throws PMException {
        if(visited.contains(start.getName())) {
            return;
        }

        // mark the node as visited
        visited.add(start.getName());

        Set<String> nodes;
        if (direction == Direction.PARENTS) {
            nodes = graph.getParents(start.getName());
        } else {
            nodes = graph.getChildren(start.getName());
        }

        for(String n : nodes) {
            Node node = graph.getNode(n);

            // traverse from the parent node
            traverse(node, propagator, visitor, direction);

            // propagate from the node to the start node
            propagator.propagate(node, start);
        }

        // after processing the parents, visit the start node
        visitor.visit(start);
    }
}
