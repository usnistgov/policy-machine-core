package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.tx.Transactional;
import gov.nist.csd.pm.pap.admin.AdminPolicy;
import gov.nist.csd.pm.pap.modification.PolicyModification;
import gov.nist.csd.pm.pap.query.PolicyQuery;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.serialization.PolicyDeserializer;
import gov.nist.csd.pm.pap.serialization.PolicySerializer;

/**
 * An interface for the administrative functions of NGAC policy points (i.e. PAP , PDP).
 */
public interface PolicyPoint extends Transactional {

    /**
     * Return the modification component of the Policy Point.
     * @return The modification component of the Policy Point.
     */
    PolicyModification modify();

    /**
     * Return the query component of the Policy Point.
     * @return The query component of the Policy Point.
     */
    PolicyQuery query();

    /**
     * Reset the policy while keeping the PM admin policy nodes.
     * @throws PMException If there is an error resetting.
     */
    void reset() throws PMException;

    /**
     * Serialize the current policy state with the given PolicySerializer.
     *
     * @param serializer The PolicySerializer used to generate the output String.
     * @return The string representation of the policy.
     * @throws PMException If there is an error during the serialization process.
     */
    String serialize(PolicySerializer serializer) throws PMException;

    /**
     * Deserialize the given input string into the current policy state. The user defined in the UserContext needs to exist
     * in the graph created if any obligations are created. If the user does not exist before an obligation is created
     * an exception will be thrown.
     *
     * @param author The UserContext describing the author of the deserialized policy elements.
     * @param input The string representation of the policy to deserialize.
     * @param policyDeserializer The PolicyDeserializer to apply the input string to the policy.
     * @throws PMException If there is an error deserializing the given inputs string.
     */
    void deserialize(UserContext author, String input, PolicyDeserializer policyDeserializer) throws PMException;

}
