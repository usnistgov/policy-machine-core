package gov.nist.csd.pm.core.pap.serialization;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;

public interface PolicySerializer {

    /**
     * Serialize the policy accessible by the PolicyQuery interface. FOr operations, only those defined using PML will
     * be serialized. Java or other implementations of Operations will not be included in the output.
     * @param policyQuery The PolicyQuery interface to retrieve the policy information.
     * @return A String representation of the policy.
     * @throws PMException If there is an error during serialization.
     */
    String serialize(PolicyQuery policyQuery) throws PMException;

}
