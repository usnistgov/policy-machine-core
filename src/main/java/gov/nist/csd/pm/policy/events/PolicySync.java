package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.exceptions.PMException;

public interface PolicySync {

    PolicySynchronizationEvent policySync() throws PMException;

}
