package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.common.exception.PMException;

import gov.nist.csd.pm.core.pap.function.arg.Args;
import java.util.Map;

public interface AdminFunctionExecutor {

    <R, A extends Args> R executeAdminFunction(AdminFunction<R, A> adminFunction, Map<String, Object> args) throws PMException;

}
