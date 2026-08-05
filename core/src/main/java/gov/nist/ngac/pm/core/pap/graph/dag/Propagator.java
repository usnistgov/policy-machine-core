package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;

public interface Propagator {

    /**
     * Propagate information from the src node to the dst node.
     *
     * @param src The node that holds the information already.
     * @param dst The node to propagate the information to.
     */
    void propagate(long src, long dst) throws PMException;

}
