package gov.nist.csd.pm.pip.graph.dag.searcher;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.dag.propagator.Propagator;
import gov.nist.csd.pm.pip.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BreadthFirstSearcher implements Searcher{

    private Graph graph;

    public BreadthFirstSearcher(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void traverse(Node start, Propagator propagator, Visitor visitor, Direction direction) throws PMException {
        // set up a queue to ensure FIFO
        Queue<Node> queue = new LinkedList<>();
        // set up a set to ensure nodes are only visited once
        Set<Node> seen = new HashSet<>();
        queue.add(start);
        seen.add(start);

        while (!queue.isEmpty()) {
            Node node = queue.poll();

            // visit the current node
            visitor.visit(node);

            Set<String> nextLevel = getNextLevel(node.getName(), direction);
            for (String s : nextLevel) {
                Node n = graph.getNode(s);

                // if this node has already been seen, we don't need to se it again
                if (seen.contains(n)) {
                    continue;
                }

                // add the node to the queue and the seen set
                queue.add(n);
                seen.add(n);

                // propagate from the nextLevel to the current node
                propagator.propagate(node, n);
            }
        }
    }

    private Set<String> getNextLevel(String node, Direction direction) throws PMException {
        if (direction == Direction.PARENTS) {
            return graph.getParents(node);
        } else {
            return graph.getChildren(node);
        }
    }
}
