package gov.nist.ngac.pm.core.pap.store;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.tx.Transactional;
import gov.nist.ngac.pm.core.pap.modification.OperationsModification;
import gov.nist.ngac.pm.core.pap.operation.OperationKind;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Collection;
import java.util.Optional;

/**
 * The persistence layer for operations.
 */
public interface OperationsStore extends OperationsModification, Transactional {

    AccessRightSet getResourceAccessRights() throws PMException;

    Collection<String> getOperationNames() throws PMException;

    OperationKind getOperationKind(String name) throws PMException;

    boolean operationExists(String name) throws PMException;

    /**
     * The persisted PML text for an operation row. Empty if the row doesn't exist or is
     * {@link OperationKind#JAVA}, which has no PML body.
     * @param name The name of the operation.
     * @return The operation's PML text, or empty.
     * @throws PMException If there is an error in the PM.
     */
    Optional<String> getOperationPml(String name) throws PMException;

}
