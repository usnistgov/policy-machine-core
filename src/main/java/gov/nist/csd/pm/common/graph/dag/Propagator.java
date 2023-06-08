package gov.nist.csd.pm.common.graph.dag;

import gov.nist.csd.pm.common.exception.PMException;

public interface Propagator {

    /**
     * Propagate information from the src node to the dst node.
     *
     * @param src The node that holds the information already.
     * @param dst The node to propagate the information to.
     */
    void propagate(String src, String dst) throws PMException;

}
