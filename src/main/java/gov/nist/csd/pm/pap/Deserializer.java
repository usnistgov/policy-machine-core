package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.PolicyDeserializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

class Deserializer implements PolicyDeserializer {

    private final PolicyStore policyStore;

    public Deserializer(PolicyStore policyStore) {
        this.policyStore = policyStore;
    }

    @Override
    public void fromJSON(String json) throws PMException {
        // deserialize using deserializer
        // tx logic is handled by the policy store
        policyStore.deserialize().fromJSON(json);
    }

    @Override
    public void fromPML(UserContext author, String pml, FunctionDefinitionStatement... customFunctions) throws PMException {
        // start a new tx to deserialize policy
        policyStore.beginTx();

        // reset policy
        policyStore.reset();

        // deserialize using deserializer
        policyStore.deserialize().fromPML(author, pml, customFunctions);

        // commit the transaction
        policyStore.commit();
    }
}
