package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.PolicyDeserializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

class PAPDeserializer implements PolicyDeserializer {

    private PolicyStore policyStore;

    public PAPDeserializer(PolicyStore policyStore) {
        this.policyStore = policyStore;
    }

    @Override
    public void fromJSON(String json) throws PMException {
        // start a new tx to deserialize policy
        policyStore.beginTx();

        policyStore.reset();

        // deserialize using deserializer
        policyStore.deserialize().fromJSON(json);

        policyStore.commit();
    }

    @Override
    public void fromPML(UserContext author, String pml, FunctionDefinitionStatement... customFunctions) throws PMException {
        // start a new tx to deserialize policy
        policyStore.beginTx();

        policyStore.reset();

        // deserialize using deserializer
        policyStore.deserialize().fromPML(author, pml, customFunctions);

        // need to apply super policy to any policy classes created or attributes assigned to policy classes
        SuperPolicy.verifySuperPolicy(this.policyStore);

        policyStore.commit();
    }
}
