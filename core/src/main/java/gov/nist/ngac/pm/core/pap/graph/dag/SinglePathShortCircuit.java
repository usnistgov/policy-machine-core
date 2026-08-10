package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * A {@link ShortCircuit} that delegates to a wrapped one without latching.
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
