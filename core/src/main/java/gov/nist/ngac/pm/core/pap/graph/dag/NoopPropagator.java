package gov.nist.ngac.pm.core.pap.graph.dag;

/**
 * {@link Propagator} that does nothing; the default used by {@link GraphWalker} when none is configured.
 */
public class NoopPropagator implements Propagator {

    @Override
    public void propagate(long node, long target) {

    }
}
