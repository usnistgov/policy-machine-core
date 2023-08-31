package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.events.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class TxPolicyEventTracker {

    private final List<PolicyEvent> events;

    public TxPolicyEventTracker() {
        events = new ArrayList<>();
    }

    public List<PolicyEvent> getEvents() {
        List<PolicyEvent> copy = new ArrayList<>(events);

        Collections.reverse(copy);

        return copy;
    }

    public void trackPolicyEvent(PolicyEvent event) {
        this.events.add(event);
    }
}
