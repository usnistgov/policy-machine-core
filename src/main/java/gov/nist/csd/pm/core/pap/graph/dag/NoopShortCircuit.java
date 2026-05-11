package gov.nist.csd.pm.core.pap.graph.dag;

public class NoopShortCircuit implements ShortCircuit{
    @Override
    public boolean evaluate(long nodeId) {
        return false;
    }
}
