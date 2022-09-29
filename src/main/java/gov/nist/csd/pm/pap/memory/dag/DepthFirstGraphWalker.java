package gov.nist.csd.pm.pap.memory.dag;

import gov.nist.csd.pm.policy.author.GraphAuthor;
import gov.nist.csd.pm.policy.author.GraphReader;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.dag.propagator.NoopPropagator;
import gov.nist.csd.pm.policy.model.graph.dag.propagator.Propagator;
import gov.nist.csd.pm.policy.model.graph.dag.visitor.NoopVisitor;
import gov.nist.csd.pm.policy.model.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import gov.nist.csd.pm.policy.model.graph.dag.walker.GraphWalker;

import java.util.List;

public class DepthFirstGraphWalker implements GraphWalker {

    private final GraphReader graph;
    private Direction direction;
    private Visitor visitor;
    private Propagator propagator;

    public DepthFirstGraphWalker(GraphReader graph) {
        this.graph = graph;
        this.visitor = new NoopVisitor();
        this.propagator = new NoopPropagator();
        this.direction = Direction.PARENTS;
    }

    public DepthFirstGraphWalker withVisitor(Visitor visitor) {
        this.visitor = visitor == null ? new NoopVisitor(): visitor;
        return this;
    }

    public DepthFirstGraphWalker withPropagator(Propagator propagator) {
        this.propagator = propagator == null ? new NoopPropagator(): propagator;
        return this;
    }

    public DepthFirstGraphWalker withDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public void walk(String start) throws PMException {
        List<String> nodes;
        if (direction == Direction.PARENTS) {
            nodes = graph.getParents(start);
        } else {
            nodes = graph.getChildren(start);
        }

        for(String n : nodes) {
            // traverse from the parent node
            walk(n);

            // propagate from the node to the start node
            propagator.propagate(n, start);
        }

        // after processing the parents, visit the start node
        visitor.visit(start);
    }
}
