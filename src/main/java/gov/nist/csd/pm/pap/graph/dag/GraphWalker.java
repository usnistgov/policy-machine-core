package gov.nist.csd.pm.pap.graph.dag;

import gov.nist.csd.pm.pap.exception.PMException;

import java.util.List;

public interface GraphWalker {

    /**
     * Traverse the graph starting at the given node.
     * @param start The node to start traversing at.
     * @throws PMException If there is an exception in the PM.
     */
    void walk(String start) throws PMException;

    /**
     * Traverse the graph as if there was a node assigned to the given nodes.
     * @param firstLevel The node to start traversing at.
     * @throws PMException If there is an exception in the PM.
     */
    void walk(List<String> firstLevel) throws PMException;

}
