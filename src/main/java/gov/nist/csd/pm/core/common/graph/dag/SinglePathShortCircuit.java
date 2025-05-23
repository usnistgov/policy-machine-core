package gov.nist.csd.pm.core.common.graph.dag;

import gov.nist.csd.pm.core.common.exception.PMException;

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
