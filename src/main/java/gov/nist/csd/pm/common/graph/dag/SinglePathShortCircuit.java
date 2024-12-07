package gov.nist.csd.pm.common.graph.dag;

import gov.nist.csd.pm.common.exception.PMException;

public class SinglePathShortCircuit implements ShortCircuit {

    private ShortCircuit shortCircuit;

    public SinglePathShortCircuit(ShortCircuit shortCircuit) {
        this.shortCircuit = shortCircuit;
    }

    @Override
    public boolean evaluate(String node) throws PMException {
        return shortCircuit.evaluate(node);
    }
}
