package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.events.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TxPolicyEventTracker {

    private final List<PolicyEvent> events;

    public TxPolicyEventTracker() {
        events = new ArrayList<>();
    }

    public List<PolicyEvent> getEvents() {
        return events.stream().sorted(Collections.reverseOrder()).toList();
    }

    public void trackPolicyEvent(PolicyEvent event) {
        this.events.add(event);
    }
}
