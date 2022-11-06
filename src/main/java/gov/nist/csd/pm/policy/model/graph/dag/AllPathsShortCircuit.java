package gov.nist.csd.pm.policy.model.graph.dag;

public class AllPathsShortCircuit implements ShortCircuit{

    private boolean s;
    private ShortCircuit shortCircuit;

    public AllPathsShortCircuit(ShortCircuit shortCircuit) {
        this.s = false;
        this.shortCircuit = shortCircuit;
    }

    @Override
    public boolean evaluate(String node) {
        boolean e = this.shortCircuit.evaluate(node);
        if (!s) {
            s = e;
        }

        return s;
    }
}
