package gov.nist.csd.pm.pap.graph.dag;

public class NoopShortCircuit implements ShortCircuit{
    @Override
    public boolean evaluate(String node) {
        return false;
    }
}
