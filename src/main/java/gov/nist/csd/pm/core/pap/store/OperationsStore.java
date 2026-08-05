package gov.nist.csd.pm.core.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.tx.Transactional;
import gov.nist.csd.pm.core.pap.modification.OperationsModification;
import gov.nist.csd.pm.core.pap.operation.OperationKind;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Collection;
import java.util.Optional;

public interface OperationsStore extends OperationsModification, Transactional {

    AccessRightSet getResourceAccessRights() throws PMException;

    Collection<String> getOperationNames() throws PMException;

    OperationKind getOperationKind(String name) throws PMException;

    boolean operationExists(String name) throws PMException;

    /**
     * The persisted PML text for an operation row, for {@code OperationsQuerier} to recompile into an
     * {@link gov.nist.csd.pm.core.pap.operation.Operation}. Empty if the row doesn't exist, or if
     * {@link #getOperationKind} for this name is {@link OperationKind#NATIVE} -- a NATIVE row has no body
     * here; resolve it through the {@code NativeOperationRegistry} instead.
     * @param name The name of the operation.
     * @return The operation's PML text, or empty.
     * @throws PMException If there is an error in the PM.
     */
    Optional<String> getOperationPml(String name) throws PMException;

}
