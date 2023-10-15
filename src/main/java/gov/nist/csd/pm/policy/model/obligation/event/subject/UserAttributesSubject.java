package gov.nist.csd.pm.policy.model.obligation.event.subject;

import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.List;

public class UserAttributesSubject extends Subject {

    public UserAttributesSubject(List<String> subjects) {
        super(subjects);
    }

    public UserAttributesSubject(String... subjects) {
        super(subjects);
    }

    @Override
    public boolean matches(UserContext userCtx, PolicyReviewer policyReviewer) throws PMException {
        String user = userCtx.getUser();

        for (String subject : subjects) {
            if (policyReviewer.isContained(user, subject)) {
                return true;
            }
        }

        return false;
    }
}
