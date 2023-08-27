package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.policy.exceptions.PMException;

public interface EventListener {

    void processEvent(EventContext eventCtx) throws PMException;

}
