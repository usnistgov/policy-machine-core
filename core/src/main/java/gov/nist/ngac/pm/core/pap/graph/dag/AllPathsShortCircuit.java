package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * A {@link ShortCircuit} that latches, so once it triggers for any node it keeps returning true for every
 * node after that.
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
