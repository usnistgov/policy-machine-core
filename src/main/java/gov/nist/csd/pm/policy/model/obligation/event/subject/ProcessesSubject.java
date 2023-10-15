package gov.nist.csd.pm.policy.model.obligation.event.subject;

import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.List;

public class ProcessesSubject extends Subject {

    public ProcessesSubject(List<String> subjects) {
        super(subjects);
    }

    public ProcessesSubject(String... subjects) {
        super(subjects);
    }

    @Override
    public boolean matches(UserContext userCtx, PolicyReviewer policyReviewer) throws PMException {
        return subjects.contains(userCtx.getProcess());
    }
}
