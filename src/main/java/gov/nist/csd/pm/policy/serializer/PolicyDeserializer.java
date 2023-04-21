package gov.nist.csd.pm.policy.serializer;

import gov.nist.csd.pm.policy.exceptions.PMException;

@FunctionalInterface
public interface PolicyDeserializer {

    void deserialize(PolicyAuthor policyAuthor, String s) throws PMException;


}
