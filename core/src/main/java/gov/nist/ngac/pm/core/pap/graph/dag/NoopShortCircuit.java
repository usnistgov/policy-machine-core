package gov.nist.ngac.pm.core.pap.graph.dag;

/**
 * {@link ShortCircuit} that never triggers; the default used by {@link GraphWalker} when none is
 * configured.
 */
public class NoopShortCircuit implements ShortCircuit{
    @Override
    public boolean evaluate(long nodeId) {
        return false;
    }
}
