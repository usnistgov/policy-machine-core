package gov.nist.ngac.pm.core.pap.graph.dag;

/**
 * A {@link ShortCircuit} that never triggers.
 */
public class NoopShortCircuit implements ShortCircuit{
    @Override
    public boolean evaluate(long nodeId) {
        return false;
    }
}
