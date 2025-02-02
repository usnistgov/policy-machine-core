package gov.nist.csd.pm.common.graph.dag;

import gov.nist.csd.pm.common.exception.PMException;

import java.util.Collection;

public interface GraphWalker {

    /**
     * Traverse the graph starting at the given node.
     * @param start The node to start traversing at.
     * @throws PMException If there is an exception in the PM.
     */
    void walk(long start) throws PMException;

    /**
     * Traverse the graph as if there was a node assigned to the given nodes.
     * @param firstLevel The node to start traversing at.
     * @throws PMException If there is an exception in the PM.
     */
    void walk(Collection<Long> firstLevel) throws PMException;

}
