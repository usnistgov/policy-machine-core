package gov.nist.csd.pm.core.common.graph.dag;

import gov.nist.csd.pm.core.common.exception.PMException;

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
