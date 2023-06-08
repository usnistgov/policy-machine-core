package gov.nist.csd.pm.common.graph.dag;

import gov.nist.csd.pm.common.exception.PMException;

public interface GraphWalker {

    /**
     * Traverse a graph starting at the start node.
     */
    void walk(String start) throws PMException;

}
