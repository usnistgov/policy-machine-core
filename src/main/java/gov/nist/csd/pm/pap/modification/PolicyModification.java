package gov.nist.csd.pm.pap.modification;

/**
 * General interface for managing a NGAC policy.
 */
public interface PolicyModification {

    /**
     * @return The GraphModification implementation.
     */
    GraphModification graph();

    /**
     * @return The ProhibitionsModification implementation.
     */
    ProhibitionsModification prohibitions();

    /**
     * @return The ObligationsModification implementation.
     */
    ObligationsModification obligations();

    /**
     * @return The ObligationsModification implementation.
     */
    OperationsModification operations();

    /**
     * @return The RoutinesModification implementation.
     */
    RoutinesModification routines();

}
