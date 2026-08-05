package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * Callback invoked by a {@link GraphWalker} for each node it visits.
 */
public interface Visitor {

    /**
     * Called when the walker visits the given node.
     *
     * @param node the visited node
     * @throws PMException if handling the visit fails
     */
    void visit(long node) throws PMException;
}
