package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;

public abstract class SubjectPatternExpression extends SubjectPattern {

    @Override
    public abstract boolean matches(EventContextUser user, ExecutionContext ctx, PAP pap) throws PMException;
}
