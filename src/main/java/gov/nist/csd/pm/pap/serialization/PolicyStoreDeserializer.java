package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.pap.serialization.json.JSONDeserializer;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pap.serialization.pml.PMLSerializer;
import gov.nist.csd.pm.policy.PolicyDeserializer;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

public class PolicyStoreDeserializer implements PolicyDeserializer {

    private PolicyStore policyStore;

    public PolicyStoreDeserializer(PolicyStore policyStore) {
        this.policyStore = policyStore;
    }

    @Override
    public void fromJSON(UserContext author, String json, FunctionDefinitionStatement... customFunctions)
            throws PMException {
        JSONDeserializer.fromJSON(policyStore, author, json, customFunctions);
    }

    @Override
    public void fromPML(UserContext author, String pml, FunctionDefinitionStatement... customFunctions)
            throws PMException {
        PMLDeserializer.fromPML(policyStore, author, pml, customFunctions);
    }
}
