package gov.nist.ngac.pm.core.pap.store;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.tx.Transactional;

/**
 * The full persistence layer for a policy, aggregating the graph, prohibitions, obligations, and operations
 * stores.
 */
public interface PolicyStore extends Transactional {

    /**
     * @return the graph store
     */
    GraphStore graph();

    /**
     * @return the prohibitions store
     */
    ProhibitionsStore prohibitions();

    /**
     * @return the obligations store
     */
    ObligationsStore obligations();

    /**
     * @return the operations store
     */
    OperationsStore operations();

    /**
     * Clears all persisted policy data.
     *
     * @throws PMException if the reset fails
     */
    void reset() throws PMException;

}
