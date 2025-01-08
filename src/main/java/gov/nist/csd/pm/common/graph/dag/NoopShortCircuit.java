package gov.nist.csd.pm.common.graph.dag;

public class NoopShortCircuit implements ShortCircuit{
    @Override
    public boolean evaluate(String node) {
        return false;
    }
}
