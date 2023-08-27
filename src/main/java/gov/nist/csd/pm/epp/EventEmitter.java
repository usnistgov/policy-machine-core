package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

public interface EventEmitter {

    void addEventListener(EventListener listener);
    void removeEventListener(EventListener listener);
    void emitEvent(EventContext event) throws PMException;

}
