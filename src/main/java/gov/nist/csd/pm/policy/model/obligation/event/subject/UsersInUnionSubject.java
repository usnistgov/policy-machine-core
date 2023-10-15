package gov.nist.csd.pm.policy.model.obligation.event.subject;

import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.List;

public class UsersInUnionSubject extends Subject{
    public UsersInUnionSubject(List<String> users) {
        super(users);
    }

    @Override
    public boolean matches(UserContext userCtx, PolicyReviewer policyReviewer) throws PMException {
        for (String subject : subjects) {
            if (policyReviewer.isContained(userCtx.getUser(), subject)) {
                return true;
            }
        }

        return false;
    }
}
