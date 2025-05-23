package gov.nist.csd.pm.core.common.graph.dag;

public class NoopShortCircuit implements ShortCircuit{
    @Override
    public boolean evaluate(long nodeId) {
        return false;
    }
}
