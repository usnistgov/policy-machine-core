package gov.nist.csd.pm.pap.graph.dag;

import gov.nist.csd.pm.pap.exception.PMException;

public interface GraphWalker {

    /**
     * Traverse a graph starting at the start node.
     */
    void walk(String start) throws PMException;

}
