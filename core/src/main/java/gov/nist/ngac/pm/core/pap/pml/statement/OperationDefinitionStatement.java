package gov.nist.ngac.pm.core.pap.pml.statement;

import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature;

/**
 * A PML statement that defines a callable operation, exposing its signature and the operation itself so
 * it can be registered in scope.
 */
public interface OperationDefinitionStatement {

    PMLOperationSignature getSignature();

    Operation<?> getOperation();

}
