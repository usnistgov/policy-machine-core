package gov.nist.csd.pm.pip.graph.dag.propagator;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

public interface Propagator {

    /**
     * Propagate information from the parent node to the child node.
     *
     * @param parentNode the parent with information to propagate to the child.
     * @param childNode the child that will receive any information being propagated by the parent.
     */
    void propagate(Node parentNode, Node childNode) throws PMException;
}
