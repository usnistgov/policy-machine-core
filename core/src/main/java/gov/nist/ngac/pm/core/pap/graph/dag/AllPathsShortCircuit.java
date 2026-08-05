package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * Wraps a {@link ShortCircuit} so that once it evaluates true for any node, it latches and keeps
 * returning true for every later node — intended for the {@link GraphWalker} all-paths short circuit
 * slot, where a single trigger anywhere should halt the entire walk.
 */
public class AllPathsShortCircuit implements ShortCircuit{

    private boolean s;
    private final ShortCircuit shortCircuit;

    public AllPathsShortCircuit(ShortCircuit shortCircuit) {
        this.s = false;
        this.shortCircuit = shortCircuit;
    }

    @Override
    public boolean evaluate(long nodeId) throws PMException {
        boolean e = this.shortCircuit.evaluate(nodeId);
        if (!s) {
            s = e;
        }

        return s;
    }
}
