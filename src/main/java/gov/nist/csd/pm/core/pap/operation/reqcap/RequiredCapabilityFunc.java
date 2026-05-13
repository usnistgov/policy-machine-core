package gov.nist.csd.pm.core.pap.operation.reqcap;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.ResourceOperation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class RequiredCapabilityFunc extends RequiredCapability {

    public RequiredCapabilityFunc() {
        super(new ArrayList<>());
    }

    @Override
    public abstract boolean isSatisfied(PAP pap, UserContext userCtx, Args args) throws PMException;
}
