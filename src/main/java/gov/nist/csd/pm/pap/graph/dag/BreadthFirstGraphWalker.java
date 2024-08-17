package gov.nist.csd.pm.pap.graph.dag;

import gov.nist.csd.pm.pap.exception.PMException;
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
    public void walk(String start) throws PMException {
        visitor.visit(start);
        if (allPathsShortCircuit.evaluate(start)
                || singlePathShortCircuit.evaluate(start)){
            return;
        }

        walkInternal(start);
    }

    private boolean walkInternal(String start) throws PMException {
        Collection<String> nextLevel = getNextLevel(start);
        Set<String> skip = new HashSet<>();
        for (String n : nextLevel) {
            visitor.visit(n);
            if (allPathsShortCircuit.evaluate(n)){
                return true;
            } else if (singlePathShortCircuit.evaluate(n)){
                skip.add(n);
                continue;
            }

            propagator.propagate(n, start);
        }

        // remove skipped nodes
        nextLevel.removeIf(skip::contains);

        for (String n : nextLevel) {
            if (walkInternal(n)) {
                return true;
            }
        }

        return false;
    }

    protected Collection<String> getNextLevel(String node) throws PMException {
        if (direction == Direction.DESCENDANTS) {
            return graphQuery.getAdjacentDescendants(node);
        } else {
            return graphQuery.getAdjacentAscendants(node);
        }
    }
}
