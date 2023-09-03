package gov.nist.csd.pm.pap.serialization.pml;

import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicyDeserializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

public class PMLDeserializer implements PolicyDeserializer {

    private FunctionDefinitionStatement[] customFunctions;

    public PMLDeserializer(FunctionDefinitionStatement... customFunctions) {
        this.customFunctions = customFunctions;
    }

    public void setCustomFunctions(FunctionDefinitionStatement[] customFunctions) {
        this.customFunctions = customFunctions;
    }

    @Override
    public void deserialize(Policy policy, UserContext author, String input) throws PMException {
        PMLExecutor.compileAndExecutePML(policy, author, input, customFunctions);
    }
}
