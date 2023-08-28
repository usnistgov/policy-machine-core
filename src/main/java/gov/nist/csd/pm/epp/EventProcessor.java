package gov.nist.csd.pm.epp;

import gov.nist.csd.pm.policy.exceptions.PMException;

public interface EventProcessor {

    void processEvent(EventContext eventCtx) throws PMException;

}
