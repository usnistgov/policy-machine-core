package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.epp.functions.FunctionExecutor;

import java.util.Arrays;
import java.util.List;

public class EPPOptions {

    private List<FunctionExecutor> executors;

    public EPPOptions(FunctionExecutor ... executors) {
        this.executors = Arrays.asList(executors);
    }

    public List<FunctionExecutor> getExecutors() {
        return executors;
    }
}
