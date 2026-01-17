package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;

public interface FunctionExecutor {

    Object executeFunction(Function<?, ?> function, Args args) throws PMException;

}
