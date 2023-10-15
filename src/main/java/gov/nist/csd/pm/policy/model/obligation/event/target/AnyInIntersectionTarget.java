package gov.nist.csd.pm.policy.model.obligation.event.target;

import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;

public class AnyInIntersectionTarget extends Target{

    public AnyInIntersectionTarget(List<String> targets) {
        super(targets);
    }

    public AnyInIntersectionTarget(String... targets) {
        super(targets);
    }

    @Override
    public boolean matches(String target, PolicyReviewer policyReviewer) throws PMException {
        for (String container : getTargets()) {
            if (!policyReviewer.isContained(target, container)) {
                return false;
            }
        }

        return true;
    }
}
