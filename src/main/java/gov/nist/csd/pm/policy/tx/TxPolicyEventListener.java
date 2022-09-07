package gov.nist.csd.pm.policy.tx;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.events.PolicyEventListener;
import gov.nist.csd.pm.policy.events.PolicyEvent;

import java.util.ArrayList;
import java.util.List;

public class TxPolicyEventListener implements PolicyEventListener {

    private List<PolicyEvent> events;

    public TxPolicyEventListener() {
        events = new ArrayList<>();
    }

    public List<PolicyEvent> getEvents() {
        return events;
    }

    @Override
    public void handlePolicyEvent(PolicyEvent event) {
        this.events.add(event);
    }
}
