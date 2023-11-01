package gov.nist.csd.pm.policy.model.obligation.event.subject;

import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.review.GraphReview;

import java.util.List;

public class UsersSubject extends Subject {

    public UsersSubject(List<String> subjects) {
        super(subjects);
    }

    public UsersSubject(String... subjects) {
        super(subjects);
    }

    @Override
    public boolean matches(UserContext userCtx, GraphReview graphReview) {
        return subjects.contains(userCtx.getUser());
    }
}
