package gov.nist.csd.pm.pap.store;

import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.events.PolicySync;
import gov.nist.csd.pm.policy.tx.Transactional;

public abstract class PolicyStore implements PolicySync, Transactional, PolicyAuthor {

}
