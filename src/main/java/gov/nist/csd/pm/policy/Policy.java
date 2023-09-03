package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

/**
 * General interface for managing a NGAC policy.
 */
public interface Policy {

    /**
     * Get the graph component of the policy.
     * @return The Graph implementation.
     */
    Graph graph();

    /**
     * Get the prohibitions component of the policy.
     * @return The Prohibitions implementation.
     */
    Prohibitions prohibitions();

    /**
     * Get the obligations component of the policy.
     * @return The Obligations implementation.
     */
    Obligations obligations();

    /**
     * Get the user defined pml component of the policy.
     * @return The UserDefinedPML implementation.
     */
    UserDefinedPML userDefinedPML();

    /**
     * Serialize the current policy state to a String.
     * @param serializer The PolicySerializer used to generate the output String.
     * @return The String representation of the current policy state.
     * @throws PMException If there is an error serializing the policy by the PolicySerializer.
     */
    String serialize(PolicySerializer serializer) throws PMException;

    /**
     * Deserialize the provided input into the current policy state. The user parameter will be used for any obligations
     * or user defined PML elements in the input string.
     *
     * @param author The UserContext describing the author of the deserialized policy elements.
     * @param input The string representation of the policy to deserialize.
     * @param policyDeserializer The PolicyDeserializer to apply the input string to the policy.
     * @throws PMException If there is an error deserializing the provided policy input by the PolicyDeserializer.
     */
    void deserialize(UserContext author, String input, PolicyDeserializer policyDeserializer) throws PMException;

    /**
     * Reset the current policy state.
     * @throws PMException If there is an error resetting the policy state.
     */
    void reset() throws PMException;
}
