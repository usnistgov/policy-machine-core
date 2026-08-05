package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * A predicate a {@link GraphWalker} evaluates per node to decide whether to halt traversal early; whether
 * a true result stops the whole walk or just the current path depends on which short-circuit slot it's
 * plugged into.
 */
public interface ShortCircuit {

    /**
     * Evaluates whether traversal should halt at the given node.
     *
     * @param nodeId the node being visited
     * @return whether to short-circuit
     * @throws PMException if evaluation fails
     */
    boolean evaluate(long nodeId) throws PMException;

}
