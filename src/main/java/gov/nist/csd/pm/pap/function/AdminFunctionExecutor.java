package gov.nist.csd.pm.pap.function;

import gov.nist.csd.pm.common.exception.PMException;

import gov.nist.csd.pm.pap.function.arg.Args;
import java.util.Map;

public interface AdminFunctionExecutor {

    <R, A extends Args> R executeAdminFunction(AdminFunction<R, A> adminFunction, Map<String, Object> args) throws PMException;

}
