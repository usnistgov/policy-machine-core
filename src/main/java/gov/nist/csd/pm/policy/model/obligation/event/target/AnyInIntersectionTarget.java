package gov.nist.csd.pm.policy.model.obligation.event.target;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.review.GraphReview;

import java.util.List;

public class AnyInIntersectionTarget extends Target{

    public AnyInIntersectionTarget(List<String> targets) {
        super(targets);
    }

    public AnyInIntersectionTarget(String... targets) {
        super(targets);
    }

    @Override
    public boolean matches(String target, GraphReview graphReview) throws PMException {
        for (String container : getTargets()) {
            if (!graphReview.isContained(target, container)) {
                return false;
            }
        }

        return true;
    }
}
