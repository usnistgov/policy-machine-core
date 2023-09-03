package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;

public interface PolicySerializer {

    String serialize(Policy policy) throws PMException;

}
