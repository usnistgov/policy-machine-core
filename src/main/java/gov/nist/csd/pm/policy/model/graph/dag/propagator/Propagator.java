package gov.nist.csd.pm.policy.model.graph.dag.propagator;

import gov.nist.csd.pm.policy.exceptions.PMException;

public interface Propagator {

    /**
     * Propagate information from the node to the target node. This can be from a parent to a child if direction is
     *
     * @param node the node that holds the information already.
     * @param target the node to propagate the information to.
     */
    void propagate(String node, String target) throws PMException;
}
