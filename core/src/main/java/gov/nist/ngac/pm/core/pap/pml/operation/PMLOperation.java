package gov.nist.ngac.pm.core.pap.pml.operation;

import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;

/**
 * An {@link gov.nist.ngac.pm.core.pap.operation.Operation} defined in PML, carrying the
 * {@link ExecutionContext} it's currently executing under so its body can access scope and the PAP.
 */
public interface PMLOperation {

     PMLOperationSignature getSignature();
     void setCtx(ExecutionContext ctx);
     ExecutionContext getCtx();
}
