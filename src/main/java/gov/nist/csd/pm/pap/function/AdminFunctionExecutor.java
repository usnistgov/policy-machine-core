package gov.nist.csd.pm.pap.function;

import gov.nist.csd.pm.common.exception.PMException;

import gov.nist.csd.pm.pap.function.arg.ActualArgs;

public interface AdminFunctionExecutor {

    <T> T executeAdminFunction(AdminFunction<T> adminFunction, ActualArgs args) throws PMException;

}
