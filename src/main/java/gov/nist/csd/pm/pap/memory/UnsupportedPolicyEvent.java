package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

public class UnsupportedPolicyEvent extends PMException {
    public UnsupportedPolicyEvent(PolicyEvent event) {
        super("policy event \"" + event.getEventName() + "\" is not supported by in memory transactions");
    }
}
