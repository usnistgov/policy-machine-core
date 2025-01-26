package gov.nist.csd.pm.common.graph.dag;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.query.GraphQuery;

import java.util.Collection;

public class DepthFirstGraphWalker implements GraphWalker {

    private final GraphQuery graphQuery;
    private Direction direction;
    private Visitor visitor;
    private Propagator propagator;
    private ShortCircuit allPathsShortCircuit;
    private ShortCircuit singlePathShortCircuit;

    public DepthFirstGraphWalker(GraphQuery graphQuery) {
        this.graphQuery = graphQuery;
        this.visitor = new NoopVisitor();
        this.propagator = new NoopPropagator();
        this.direction = Direction.DESCENDANTS;
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

    public GraphQuery getGraphQuery() {
        return graphQuery;
    }

    public Direction getDirection() {
        return direction;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public Propagator getPropagator() {
        return propagator;
    }

    public ShortCircuit getAllPathsShortCircuit() {
        return allPathsShortCircuit;
    }

    public ShortCircuit getSinglePathShortCircuit() {
        return singlePathShortCircuit;
    }

    @Override
    public void walk(long start) throws PMException {
        walkInternal(start);
    }

    @Override
    public void walk(long[] firstLevel) throws PMException {
        for (long node : firstLevel) {
            walkInternal(node);
        }
    }

    private int walkInternal(long start) throws PMException {
        if (allPathsShortCircuit.evaluate(start)) {
            visitor.visit(start);
            return RETURN;
        } else if (singlePathShortCircuit.evaluate(start)){
            visitor.visit(start);
            return CONTINUE;
        }

        Collection<String> nodes = getNextLevel(start);
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

    protected static final int WALK = 0;
    protected static final int CONTINUE = 1;
    protected static final int RETURN = 2;

    protected long[] getNextLevel(long node) throws PMException {
        if (direction == Direction.DESCENDANTS) {
            return graphQuery.getAdjacentDescendants(node);
        } else {
            return graphQuery.getAdjacentAscendants(node);
        }
    }
}
