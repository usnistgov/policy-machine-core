package gov.nist.csd.pm.common.graph.dag;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.query.GraphQuery;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BreadthFirstGraphWalker implements GraphWalker {

    private GraphQuery graphQuery;
    private Direction direction;
    private Visitor visitor;
    private Propagator propagator;
    private ShortCircuit allPathsShortCircuit;
    private ShortCircuit singlePathShortCircuit;

    public BreadthFirstGraphWalker(GraphQuery graphQuery) {
        this.graphQuery = graphQuery;
        this.visitor = new NoopVisitor();
        this.propagator = new NoopPropagator();
        this.direction = Direction.DESCENDANTS;
        this.allPathsShortCircuit = new NoopShortCircuit();
        this.singlePathShortCircuit = new NoopShortCircuit();
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

    public BreadthFirstGraphWalker withAllPathShortCircuit(ShortCircuit shortCircuit) {
        this.allPathsShortCircuit = shortCircuit;
        return this;
    }

    public BreadthFirstGraphWalker withSinglePathShortCircuit(ShortCircuit shortCircuit) {
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
        visitor.visit(start);
        if (allPathsShortCircuit.evaluate(start)
                || singlePathShortCircuit.evaluate(start)){
            return;
        }

        walkInternal(start);
    }

    @Override
    public void walk(long[] firstLevel) throws PMException {
        for (long node : firstLevel) {
            walk(node);
        }
    }

    private boolean walkInternal(long start) throws PMException {
        long[] nextLevel = getNextLevel(start);
        Set<Long> skip = new HashSet<>();
        for (long n : nextLevel) {
            visitor.visit(n);
            if (allPathsShortCircuit.evaluate(n)){
                return true;
            } else if (singlePathShortCircuit.evaluate(n)){
                skip.add(n);
                continue;
            }

            propagator.propagate(n, start);
        }

        for (long n : nextLevel) {
            if (skip.contains(n)) {
                continue;
            }

            if (walkInternal(n)) {
                return true;
            }
        }

        return false;
    }

    protected long[] getNextLevel(long node) throws PMException {
        if (direction == Direction.DESCENDANTS) {
            return graphQuery.getAdjacentDescendants(node);
        } else {
            return graphQuery.getAdjacentAscendants(node);
        }
    }
}
