package gov.nist.csd.pm.policy.model.graph.dag.walker.dfs;

import gov.nist.csd.pm.policy.Graph;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.dag.NoopShortCircuit;
import gov.nist.csd.pm.policy.model.graph.dag.ShortCircuit;
import gov.nist.csd.pm.policy.model.graph.dag.propagator.NoopPropagator;
import gov.nist.csd.pm.policy.model.graph.dag.propagator.Propagator;
import gov.nist.csd.pm.policy.model.graph.dag.visitor.NoopVisitor;
import gov.nist.csd.pm.policy.model.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import gov.nist.csd.pm.policy.model.graph.dag.walker.GraphWalker;

import java.util.List;

public class DepthFirstGraphWalker implements GraphWalker {

    private final Graph graph;
    private Direction direction;
    private Visitor visitor;
    private Propagator propagator;
    private ShortCircuit allPathsShortCircuit;
    private ShortCircuit singlePathShortCircuit;

    public DepthFirstGraphWalker(Graph graph) {
        this.graph = graph;
        this.visitor = new NoopVisitor();
        this.propagator = new NoopPropagator();
        this.direction = Direction.PARENTS;
        this.allPathsShortCircuit = new NoopShortCircuit();
        this.singlePathShortCircuit = new NoopShortCircuit();
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

    public DepthFirstGraphWalker withAllPathShortCircuit(ShortCircuit shortCircuit) {
        this.allPathsShortCircuit = shortCircuit;
        return this;
    }

    public DepthFirstGraphWalker withSinglePathShortCircuit(ShortCircuit shortCircuit) {
        this.singlePathShortCircuit = shortCircuit;
        return this;
    }

    @Override
    public void walk(String start) throws PMException {
        // start traversal
        walkInternal(start);
    }

    private int walkInternal(String start) throws PMException {
        if (allPathsShortCircuit.evaluate(start)) {
            visitor.visit(start);
            return RETURN;
        } else if (singlePathShortCircuit.evaluate(start)){
            visitor.visit(start);
            return CONTINUE;
        }

        List<String> nodes = getNextLevel(start);
        int ret = WALK;
        for(String n : nodes) {
            int i = walkInternal(n);

            // propagate to the next level
            propagator.propagate(n, start);

            if (i == RETURN) {
                ret = i;
                break;
            }
        }

        visitor.visit(start);

        return ret;
    }

    private static final int WALK = 0;
    private static final int CONTINUE = 1;
    private static final int RETURN = 2;


    private List<String> getNextLevel(String node) throws PMException {
        if (direction == Direction.PARENTS) {
            return graph.getParents(node);
        } else {
            return graph.getChildren(node);
        }
    }
}
