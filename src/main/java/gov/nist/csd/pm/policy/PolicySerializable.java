package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.serializer.PolicyDeserializer;
import gov.nist.csd.pm.policy.serializer.PolicySerializer;

public interface PolicySerializable {

    String toString(PolicySerializer policySerializer) throws PMException;
    void fromString(String s, PolicyDeserializer policyDeserializer) throws PMException;

}
