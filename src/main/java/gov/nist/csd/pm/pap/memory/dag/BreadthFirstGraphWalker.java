package gov.nist.csd.pm.pap.memory.dag;

import gov.nist.csd.pm.policy.author.GraphAuthor;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.dag.propagator.NoopPropagator;
import gov.nist.csd.pm.policy.model.graph.dag.propagator.Propagator;
import gov.nist.csd.pm.policy.model.graph.dag.visitor.NoopVisitor;
import gov.nist.csd.pm.policy.model.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import gov.nist.csd.pm.policy.model.graph.dag.walker.GraphWalker;

import java.util.*;

public class BreadthFirstGraphWalker implements GraphWalker {

    private final GraphAuthor graph;
    private Direction direction;
    private Visitor visitor;
    private Propagator propagator;

    public BreadthFirstGraphWalker(GraphAuthor graph) {
        this.graph = graph;
        this.visitor = new NoopVisitor();
        this.propagator = new NoopPropagator();
        this.direction = Direction.PARENTS;
    }

    public BreadthFirstGraphWalker withVisitor(Visitor visitor) {
        this.visitor = visitor == null ? new NoopVisitor(): visitor;
        return this;
    }

    public BreadthFirstGraphWalker withPropagator(Propagator propagator) {
        this.propagator = propagator == null ? new NoopPropagator(): propagator;
        return this;
    }

    public BreadthFirstGraphWalker withDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public void walk(String start) throws PMException {
        // set up a queue to ensure FIFO
        Queue<String> queue = new LinkedList<>();
        // set up a set to ensure nodes are only visited once
        Set<String> seen = new HashSet<>();
        queue.add(start);
        seen.add(start);

        while (!queue.isEmpty()) {
            String node = queue.poll();

            // visit the current node
            visitor.visit(node);

            List<String> nextLevel = getNextLevel(node, direction);
            for (String n : nextLevel) {
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

    private List<String> getNextLevel(String node, Direction direction) throws PMException {
        if (direction == Direction.PARENTS) {
            return graph.getParents(node);
        } else {
            return graph.getChildren(node);
        }
    }
}
