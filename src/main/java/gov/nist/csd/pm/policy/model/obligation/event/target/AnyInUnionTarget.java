package gov.nist.csd.pm.policy.model.obligation.event.target;

import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;

public class AnyInUnionTarget extends Target{

    public AnyInUnionTarget(List<String> targets) {
        super(targets);
    }

    public AnyInUnionTarget(String... targets) {
        super(targets);
    }

    @Override
    public boolean matches(String target, PolicyReviewer policyReviewer) throws PMException {
        for (String container : getTargets()) {
            if (policyReviewer.isContained(target, container)) {
                return true;
            }
        }

        return false;
    }
}
