package gov.nist.ngac.pm.core.pap.pml.operation;

import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;

/**
 * An {@link gov.nist.ngac.pm.core.pap.operation.Operation} defined in PML.
 */
public interface PMLOperation {

     PMLOperationSignature getSignature();
     void setCtx(ExecutionContext ctx);
     ExecutionContext getCtx();
}
