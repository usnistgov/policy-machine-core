package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;

public class ResetPolicyEvent implements PolicyEvent{
    @Override
    public String getEventName() {
        return "reset_policy";
    }

    @Override
    public void apply(Policy policy) throws PMException {
        policy.reset();
    }
}
