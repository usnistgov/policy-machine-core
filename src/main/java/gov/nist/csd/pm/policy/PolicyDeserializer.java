package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

public interface PolicyDeserializer {

    void deserialize(Policy policy, UserContext author, String input) throws PMException;

}
