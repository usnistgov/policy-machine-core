package gov.nist.csd.pm.common.graph.dag;

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
