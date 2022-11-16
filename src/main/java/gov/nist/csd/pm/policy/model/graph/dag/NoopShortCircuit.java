package gov.nist.csd.pm.policy.model.graph.dag;

public class NoopShortCircuit implements ShortCircuit{
    @Override
    public boolean evaluate(String node) {
        return false;
    }
}
