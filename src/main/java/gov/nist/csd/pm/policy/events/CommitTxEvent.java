package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.io.Serializable;

public class CommitTxEvent implements PolicyEvent, Serializable {

    public CommitTxEvent() { /* This object is used to notify a listener that a commit event happened */ }

    @Override
    public String getEventName() {
        return "commit_tx";
    }

    @Override
    public void apply(Policy policy) throws PMException {

    }
}
