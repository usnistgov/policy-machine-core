package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.io.Serializable;

public class BeginTxEvent implements PolicyEvent, Serializable {
    @Override
    public String getEventName() {
        return "begin_tx";
    }

    @Override
    public void apply(Policy policy) throws PMException {

    }
}
