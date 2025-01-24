package gov.nist.csd.pm.common.graph.dag;

import gov.nist.csd.pm.common.exception.PMException;

public class AllPathsShortCircuit implements ShortCircuit{

    private boolean s;
    private ShortCircuit shortCircuit;

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
