package gov.nist.csd.pm.core.pdp.adjudication;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;

public abstract class Adjudicator {

    protected PAP pap;
    protected UserContext userCtx;

    public Adjudicator(PAP pap, UserContext userCtx) {
        this.pap = pap;
        this.userCtx = userCtx;
    }

    protected void check(UserContext userCtx, TargetContext targetCtx, AccessRightSet required) throws PMException {
        AccessRightSet computed = pap.query().access().computePrivileges(userCtx, targetCtx);

        if (!computed.containsAll(required) || (required.isEmpty() && computed.isEmpty())) {
            throw UnauthorizedException.of(pap.query().graph(), userCtx, targetCtx, computed, required);
        }
    }
}
