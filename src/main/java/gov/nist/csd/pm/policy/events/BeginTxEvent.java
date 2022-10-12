package gov.nist.csd.pm.policy.events;

public class BeginTxEvent implements PolicyEvent {
    @Override
    public String getEventName() {
        return "begin_tx";
    }
}
