package gov.nist.csd.pm.graph.dag.searcher;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.graph.dag.propagator.Propagator;
import gov.nist.csd.pm.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.graph.model.nodes.Node;

public interface Searcher {

    /**
     * Traverse a graph starting at the start node. User the Propagator to propagate information during traversal,
     * and the Visitor to handle visiting nodes.
     *
     * @param start the node to start a search at.
     * @param propagator the handler for propagating information during traversal.
     * @param visitor the handler for visiting nodes during traversal.
     * @throws PMException if there is an exception traversing a graph.
     */
    void traverse(Node start, Propagator propagator, Visitor visitor) throws PMException;

}
