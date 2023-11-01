package gov.nist.csd.pm.policy.model.obligation.event.target;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.review.GraphReview;

import java.util.List;

public class OnTargets extends Target{

    public OnTargets(List<String> targets) {
        super(targets);
    }

    public OnTargets(String... targets) {
        super(targets);
    }

    @Override
    public boolean matches(String target, GraphReview graphReview) throws PMException {
        return targets.contains(target);
    }
}
