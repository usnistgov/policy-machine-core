package gov.nist.csd.pm.policy.serializer;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;

@FunctionalInterface
public interface PolicyDeserializer {

    void deserialize(Policy policyAuthor, String s) throws PMException;


}
