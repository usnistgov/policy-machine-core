package gov.nist.csd.pm.pap.modification;

/**
 * General interface for managing a NGAC policy.
 */
public interface PolicyModification {

    /**
     * Get the graph component of the policy.
     * @return The GraphModification implementation.
     */
    GraphModification graph();

    /**
     * Get the prohibitions component of the policy.
     * @return The ProhibitionsModification implementation.
     */
    ProhibitionsModification prohibitions();

    /**
     * Get the obligations component of the policy.
     * @return The ObligationsModification implementation.
     */
    ObligationsModification obligations();

    /**
     * Get the operations component of the policy.
     * @return The ObligationsModification implementation.
     */
    OperationsModification operations();

    /**
     * Get the routines component of the policy.
     * @return The RoutinesModification implementation.
     */
    RoutinesModification routines();

}
