package gov.nist.csd.pm.policy.events;

public class CommitTxEvent implements PolicyEvent {

    public CommitTxEvent() { /* This object is used to notify a listener that a commit event happened */ }

    @Override
    public String getEventName() {
        return "commit_tx";
    }
}
