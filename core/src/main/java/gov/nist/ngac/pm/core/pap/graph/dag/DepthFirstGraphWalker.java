package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;
import java.util.Collection;

/**
 * {@link GraphWalker} that recurses fully into each adjacent node before visiting it, so a node is
 * visited only after all of its adjacents have been.
 */
public class DepthFirstGraphWalker extends GraphWalker {

    public DepthFirstGraphWalker(AdjacencyRetriever adjacencyRetriever) {
        super(adjacencyRetriever);
    }

    @Override
    public void walk(long start) throws PMException {
        walkInternal(start);
    }

    private int walkInternal(long start) throws PMException {
        if (allPathsShortCircuit.evaluate(start)) {
            visitor.visit(start);
            return RETURN;
        } else if (singlePathShortCircuit.evaluate(start)) {
            visitor.visit(start);
            return CONTINUE;
        }

        Collection<Long> nodes = adjacencyRetriever.getAdjacent(start);
        int ret = WALK;
        for (long n : nodes) {
            int i = walkInternal(n);
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
}
