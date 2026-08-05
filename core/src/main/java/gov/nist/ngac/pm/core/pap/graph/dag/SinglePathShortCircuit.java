package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * Delegates directly to a wrapped {@link ShortCircuit} without latching — intended for the
 * {@link GraphWalker} single-path short circuit slot, where a trigger only halts the current path.
 */
public class SinglePathShortCircuit implements ShortCircuit {

    private final ShortCircuit shortCircuit;

    public SinglePathShortCircuit(ShortCircuit shortCircuit) {
        this.shortCircuit = shortCircuit;
    }

    @Override
    public boolean evaluate(long nodeId) throws PMException {
        return shortCircuit.evaluate(nodeId);
    }
}
