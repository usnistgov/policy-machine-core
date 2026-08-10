package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * Callback used to carry information from a visited node back to the node it was reached from.
 */
public interface Propagator {

    /**
     * Propagates information from the source node to the destination node.
     *
     * @param src the node the information is propagated from
     * @param dst the node the information is propagated to
     */
    void propagate(long src, long dst) throws PMException;

}
