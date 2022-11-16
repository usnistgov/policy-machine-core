package gov.nist.csd.pm.policy.model.graph.dag;

public class SinglePathShortCircuit implements ShortCircuit {

    private ShortCircuit shortCircuit;

    public SinglePathShortCircuit(ShortCircuit shortCircuit) {
        this.shortCircuit = shortCircuit;
    }

    @Override
    public boolean evaluate(String node) {
        return shortCircuit.evaluate(node);
    }
}
