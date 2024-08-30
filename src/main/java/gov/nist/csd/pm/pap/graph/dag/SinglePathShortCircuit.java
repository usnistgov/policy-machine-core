package gov.nist.csd.pm.pap.graph.dag;

import gov.nist.csd.pm.pap.exception.PMException;

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
