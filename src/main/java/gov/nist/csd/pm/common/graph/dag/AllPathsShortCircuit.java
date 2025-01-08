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
    public boolean evaluate(String node) throws PMException {
        boolean e = this.shortCircuit.evaluate(node);
        if (!s) {
            s = e;
        }

        return s;
    }
}
