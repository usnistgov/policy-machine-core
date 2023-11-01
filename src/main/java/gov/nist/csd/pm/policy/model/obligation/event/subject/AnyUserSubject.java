package gov.nist.csd.pm.policy.model.obligation.event.subject;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.review.GraphReview;

import java.util.ArrayList;

public class AnyUserSubject extends Subject {
    public AnyUserSubject() {
        super(new ArrayList<>());
    }

    @Override
    public boolean matches(UserContext userCtx, GraphReview graphReview) throws PMException {
        return true;
    }
}
