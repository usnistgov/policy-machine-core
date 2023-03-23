package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.model.obligation.Obligation;

public class DeleteObligationEvent implements PolicyEvent {

    private final Obligation obligation;

    public DeleteObligationEvent(Obligation obligation) {
        this.obligation = obligation;
    }

    public Obligation getLabel() {
        return obligation;
    }

    @Override
    public String getEventName() {
        return "delete_obligation";
    }
}
