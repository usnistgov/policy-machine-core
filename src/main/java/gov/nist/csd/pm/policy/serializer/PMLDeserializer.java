package gov.nist.csd.pm.policy.serializer;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

public class PMLDeserializer implements PolicyDeserializer {

    private final UserContext userCtx;
    private final FunctionDefinitionStatement[] customFunctions;

    public PMLDeserializer(UserContext userContext, FunctionDefinitionStatement ... customFunctions) {
        this.userCtx = userContext;
        this.customFunctions = customFunctions;
    }

    @Override
    public void deserialize(Policy policy, String s) throws PMException {
        PMLExecutor.compileAndExecutePML(policy, userCtx, s, customFunctions);
    }
}
