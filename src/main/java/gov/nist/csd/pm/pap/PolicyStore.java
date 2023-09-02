package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.serialization.PolicyStoreDeserializer;
import gov.nist.csd.pm.pap.serialization.PolicyStoreSerializer;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicyDeserializer;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.tx.Transactional;

/**
 * PolicyStore is an abstract class that outlines the expected behavior of a backend implementation. It is expected that
 * any subclass calls {@link AdminPolicy#verify(AdminPolicy.Verifier, GraphStore)} in the constructor to verify the setup
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
     * Reset the underlying policy. This method should call {@link AdminPolicy#verify(AdminPolicy.Verifier, GraphStore)}
     * to initialize the admin policy elements after reset.
     *
     * @throws PMException If there is an error during the reset or admin initialization process
     */
    @Override
    public abstract void reset() throws PMException;

    @Override
    public PolicySerializer serialize() throws PMException {
        return new PolicyStoreSerializer(this);
    }

    @Override
    public PolicyDeserializer deserialize() throws PMException {
        return new PolicyStoreDeserializer(this);
    }
}
