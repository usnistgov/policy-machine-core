package gov.nist.csd.pm.policy.model.obligation.event.target;

import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;

public class OnTargets extends Target{

    public OnTargets(List<String> targets) {
        super(targets);
    }

    public OnTargets(String... targets) {
        super(targets);
    }

    @Override
    public boolean matches(String target, PolicyReviewer policyReviewer) throws PMException {
        return targets.contains(target);
    }
}
