package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.exceptions.PMException;

public interface PolicyEventEmitter {
    void addEventListener(PolicyEventListener listener, boolean sync) throws PMException;
    void removeEventListener(PolicyEventListener listener);
    void emitEvent(PolicyEvent event) throws PMException;
}
