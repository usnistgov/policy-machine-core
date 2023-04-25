package gov.nist.csd.pm.pap.memory.dag;

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

import java.util.*;

public class BreadthFirstGraphWalker implements GraphWalker {

    private final Graph graph;
    private Direction direction;
    private Visitor visitor;
    private Propagator propagator;
    private ShortCircuit allPathsShortCircuit;
    private ShortCircuit singlePathShortCircuit;

    public BreadthFirstGraphWalker(Graph graph) {
        this.graph = graph;
        this.visitor = new NoopVisitor();
        this.propagator = new NoopPropagator();
        this.direction = Direction.PARENTS;
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
        List<String> nextLevel = getNextLevel(start);
        for (String n : nextLevel) {
            visitor.visit(n);
            if (allPathsShortCircuit.evaluate(n)){
                return true;
            } else if (singlePathShortCircuit.evaluate(n)){
                return false;
            }

            propagator.propagate(n, start);
        }

        for (String n : nextLevel) {
            if (walkInternal(n)) {
                return true;
            }
        }

        return false;
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
