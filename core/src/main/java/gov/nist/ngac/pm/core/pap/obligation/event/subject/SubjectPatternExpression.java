package gov.nist.ngac.pm.core.pap.obligation.event.subject;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.obligation.event.EventContextUser;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;

/**
 * Base class for the concrete subject pattern expressions.
 */
public abstract class SubjectPatternExpression extends SubjectPattern {

    @Override
    public abstract boolean matches(EventContextUser user, ExecutionContext ctx, PAP pap) throws PMException;
}
