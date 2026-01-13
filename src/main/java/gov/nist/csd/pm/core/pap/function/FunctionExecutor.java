package gov.nist.csd.pm.core.pap.function;

import gov.nist.csd.pm.core.common.exception.PMException;

import java.util.Map;

public interface FunctionExecutor {

    <R> R executeFunction(Function<R> function, Map<String, Object> rawArgs) throws PMException;

}
