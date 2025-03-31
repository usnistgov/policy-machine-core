package gov.nist.csd.pm.pap.function;

import gov.nist.csd.pm.common.exception.PMException;

import gov.nist.csd.pm.pap.function.arg.Args;

public interface AdminFunctionExecutor {

    <T> T executeAdminFunction(AdminFunction<T> adminFunction, Args args) throws PMException;

}
