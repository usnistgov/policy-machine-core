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
     * The persisted PML text for an operation. Empty if the operation doesn't exist or is a
     * {@link OperationKind#JAVA} operation, which has no PML body.
     *
     * @param name the operation's name
     * @return the operation's PML text, or empty
     * @throws PMException if the lookup fails
     */
    Optional<String> getOperationPml(String name) throws PMException;

}
