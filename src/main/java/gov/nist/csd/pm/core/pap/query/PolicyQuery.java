package gov.nist.csd.pm.core.pap.query;

/**
 * Interface for the querying of policy data and access controls state.
 */
public interface PolicyQuery {

    /**
     * @return The AccessQuery implementation.
     */
    AccessQuery access();

    /**
     * @return The GraphQuery implementation.
     */
    GraphQuery graph();

    /**
     * @return The ProhibitionsQuery implementation.
     */
    ProhibitionsQuery prohibitions();

    /**
     * @return The ObligationsQuery implementation.
     */
    ObligationsQuery obligations();

    /**
     * @return The OperationsQuery implementation.
     */
    OperationsQuery operations();
}
