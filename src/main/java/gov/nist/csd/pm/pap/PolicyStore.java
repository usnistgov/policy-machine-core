package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.events.PolicySync;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.tx.Transactional;

public abstract class PolicyStore implements Policy, PolicySync, Transactional {

}
