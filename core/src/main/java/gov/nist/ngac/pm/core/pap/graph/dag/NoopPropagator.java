package gov.nist.ngac.pm.core.pap.graph.dag;

/**
 * A {@link Propagator} that does nothing.
 */
public class NoopPropagator implements Propagator {

    @Override
    public void propagate(long node, long target) {

    }
}
