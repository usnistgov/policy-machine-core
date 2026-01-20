package gov.nist.csd.pm.core.pap.pml.operation;

import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;

public interface PMLOperation {

     void setCtx(ExecutionContext ctx);
     ExecutionContext getCtx();
}
