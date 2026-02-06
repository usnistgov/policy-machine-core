package gov.nist.csd.pm.core.pap.operation.reqcap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.io.Serializable;

public class RequiredCapabilityFunc extends RequiredCapability {

    private final RequiredCapabilitiesFuncExecutor func;

    public RequiredCapabilityFunc(RequiredCapabilitiesFuncExecutor func) {
        this.func = func;
    }

    @Override
    public boolean isSatisfied(PAP pap, UserContext userCtx, Args args) throws PMException {
        return func.execute(pap.query(), userCtx, args);
    }

    @FunctionalInterface
    public interface RequiredCapabilitiesFuncExecutor extends Serializable {
        boolean execute(PolicyQuery policyQuery, UserContext userCtx, Args args) throws PMException;
    }
}
