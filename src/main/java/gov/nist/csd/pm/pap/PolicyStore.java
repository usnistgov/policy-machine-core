package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicyDeserializer;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.tx.Transactional;

/**
 * PolicyStore is an abstract class that outlines the expected behavior of a backend implementation. It is expected that
 * any subclass calls {@link AdminPolicyNode#verify(AdminPolicy.Verifier, GraphStore)} in the constructor to verify the setup
 * of the admin policy.
 */
public abstract class PolicyStore implements Policy, Transactional {

    @Override
    public abstract GraphStore graph();

    @Override
    public abstract ProhibitionsStore prohibitions();

    @Override
    public abstract ObligationsStore obligations();

    @Override
    public abstract UserDefinedPMLStore userDefinedPML();

    /**
     * Reset the underlying policy. This method should call {@link AdminPolicyNode#verify(AdminPolicy.Verifier, GraphStore)}
     * to initialize the admin policy elements after reset.
     *
     * @throws PMException If there is an error during the reset or admin initialization process
     */
    @Override
    public abstract void reset() throws PMException;

    /**
     * Serialize the current policy state with the given PolicySerializer.]
     *
     * @param serializer The PolicySerializer used to generate the output String.
     * @return The string representation of the policy.
     * @throws PMException If there is an error during the serialization process.
     */
    @Override
    public String serialize(PolicySerializer serializer) throws PMException {
        return serializer.serialize(this);
    }

    /**
     * Deserialize the given input string into the current policy state. The user defined in the UserContext needs to exist
     * in the graph created if any obligations are created. If the user does not exist before an obligation is created
     * an exception will be thrown. This method also resets the policy before deserialization. However, the {@link AdminPolicy}
     * nodes are assumed to be created and can be referenced in the input string without explicit creation. If any of the
     * admin policy nodes are created in the input string an exception will be thrown.
     *
     * @param author The UserContext describing the author of the deserialized policy elements.
     * @param input The string representation of the policy to deserialize.
     * @param policyDeserializer The PolicyDeserializer to apply the input string to the policy.
     * @throws PMException If there is an error deserializing the given inputs string.
     */
    @Override
    public void deserialize(UserContext author, String input, PolicyDeserializer policyDeserializer) throws PMException {
        beginTx();
        reset();

        try {
            policyDeserializer.deserialize(this, author, input);
        } catch (PMException e) {
            rollback();
            throw e;
        }

        commit();
    }
}
