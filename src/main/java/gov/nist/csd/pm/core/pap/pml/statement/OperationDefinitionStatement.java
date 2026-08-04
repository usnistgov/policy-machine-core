package gov.nist.csd.pm.core.pap.pml.statement;

import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;

public interface OperationDefinitionStatement {

    PMLOperationSignature getSignature();

    Operation<?> getOperation();

}
