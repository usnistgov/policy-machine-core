package gov.nist.ngac.pm.core.pap.serialization;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;

/**
 * Serializes a policy to a string form that a matching {@link PolicyDeserializer} can load.
 */
public interface PolicySerializer {

    /**
     * Serializes the policy accessible through the given query. Only operations defined in PML are
     * included; Java-defined operations are not serialized.
     *
     * @param policyQuery used to retrieve the policy information
     * @return a string representation of the policy
     * @throws PMException if serialization fails
     */
    String serialize(PolicyQuery policyQuery) throws PMException;

}
