package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

public class DeleteProhibitionEvent implements PolicyEvent {

    private final Prohibition prohibition;

    public DeleteProhibitionEvent(Prohibition prohibition) {
        this.prohibition = prohibition;
    }

    public Prohibition getProhibition() {
        return prohibition;
    }

    @Override
    public String getEventName() {
        return "delete_prohibition";
    }
}
