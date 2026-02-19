package gov.nist.csd.pm.core.pap.obligation.event.subject;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.epp.EventContextUser;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;

public abstract class SubjectPatternExpression extends SubjectPattern {

    @Override
    public abstract boolean matches(EventContextUser value, PolicyQuery pap) throws PMException;
}
