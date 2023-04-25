package gov.nist.csd.pm.policy.serializer;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;

public interface PolicySerializer {

    String serialize(Policy policy) throws PMException;

}
