package gov.nist.csd.pm.policy.serializer;

import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.exceptions.PMException;

public interface PolicySerializer {

    String serialize(PolicyAuthor policyAuthor) throws PMException;

}
