package gov.nist.ngac.pm.core.pap.graph.dag;

import gov.nist.ngac.pm.core.common.exception.PMException;

/**
 * A predicate evaluated per node to decide whether to halt traversal early.
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
