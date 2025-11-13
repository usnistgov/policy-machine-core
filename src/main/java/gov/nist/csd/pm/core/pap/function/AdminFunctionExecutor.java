package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.common.exception.PMException;

import gov.nist.csd.pm.core.pap.function.arg.Args;
import java.util.Map;

public interface AdminFunctionExecutor {

    <R> R executeAdminFunction(AdminFunction<R> adminFunction, Map<String, Object> args) throws PMException;

}
