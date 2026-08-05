package gov.nist.ngac.pm.core.pap.pml.statement;

import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature;

public interface OperationDefinitionStatement {

    PMLOperationSignature getSignature();

    Operation<?> getOperation();

}
