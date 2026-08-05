package gov.nist.ngac.pm.core.pap.graph.dag;

/**
 * {@link Visitor} that does nothing; the default used by {@link GraphWalker} when none is configured.
 */
public class NoopVisitor implements Visitor{

    @Override
    public void visit(long node) {

    }
}
