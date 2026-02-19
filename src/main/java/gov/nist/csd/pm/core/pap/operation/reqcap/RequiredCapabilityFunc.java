package gov.nist.csd.pm.core.pap.operation.reqcap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;

public class RequiredCapabilityFunc extends RequiredCapability {

    private final RequiredCapabilityFuncExecutor funcExecutor;

    public RequiredCapabilityFunc(RequiredCapabilityFuncExecutor func) {
        super(new ArrayList<>());
        this.funcExecutor = func;
    }

    @Override
    public boolean isSatisfied(PAP pap, UserContext userCtx, Args args) throws PMException {
        return funcExecutor.isSatisfied(pap, userCtx, args);
    }

    @FunctionalInterface
    public interface RequiredCapabilityFuncExecutor {
        boolean isSatisfied(PAP pap, UserContext userCtx, Args args);
    }
}
