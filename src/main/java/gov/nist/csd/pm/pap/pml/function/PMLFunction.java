package gov.nist.csd.pm.pap.pml.function;

import gov.nist.csd.pm.pap.pml.context.ExecutionContext;

public interface PMLFunction {

     void setCtx(ExecutionContext ctx);
     ExecutionContext getCtx();

}
