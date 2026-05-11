package gov.nist.csd.pm.core.pap.graph.dag;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.dag.AdjacencyRetriever;
import gov.nist.csd.pm.core.common.graph.dag.GraphWalker;
import gov.nist.csd.pm.core.common.graph.dag.NoopPropagator;
import gov.nist.csd.pm.core.common.graph.dag.NoopShortCircuit;
import gov.nist.csd.pm.core.common.graph.dag.NoopVisitor;
import gov.nist.csd.pm.core.common.graph.dag.Propagator;
import gov.nist.csd.pm.core.common.graph.dag.ShortCircuit;
import gov.nist.csd.pm.core.common.graph.dag.Visitor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BreadthFirstGraphWalker extends GraphWalker {

    public BreadthFirstGraphWalker(AdjacencyRetriever adjacencyRetriever) {
        super(adjacencyRetriever);
    }

    @Override
    public void walk(long start) throws PMException {
        visitor.visit(start);
        if (allPathsShortCircuit.evaluate(start) || singlePathShortCircuit.evaluate(start)) {
            return;
        }

        walkInternal(start);
    }

    private boolean walkInternal(long start) throws PMException {
        Collection<Long> nextLevel = adjacencyRetriever.getAdjacent(start);
        Set<Long> skip = new HashSet<>();
        for (long n : nextLevel) {
            visitor.visit(n);
            if (allPathsShortCircuit.evaluate(n)) {
                return true;
            } else if (singlePathShortCircuit.evaluate(n)) {
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
}
