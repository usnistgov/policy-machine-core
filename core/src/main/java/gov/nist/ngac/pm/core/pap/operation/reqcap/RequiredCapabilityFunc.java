package gov.nist.ngac.pm.core.pap.operation.reqcap;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;

/**
 * A {@link RequiredCapability} satisfied by custom logic instead of a declared set of privileges.
 */
public abstract class RequiredCapabilityFunc extends RequiredCapability {

    public RequiredCapabilityFunc() {
        super(new ArrayList<>());
    }

    @Override
    public abstract boolean isSatisfied(PAP pap, UserContext userCtx, Args args) throws PMException;
}
