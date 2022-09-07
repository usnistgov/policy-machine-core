package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.exceptions.PMException;

public interface PolicyEventListener {

    void handlePolicyEvent(PolicyEvent event) throws PMException;

}
