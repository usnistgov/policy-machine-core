package gov.nist.csd.pm.policy.serializer;

import gov.nist.csd.pm.policy.pml.PALExecutor;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

public class PALDeserializer implements PolicyDeserializer {

    private final UserContext userCtx;
    private final FunctionDefinitionStatement[] customFunctions;

    public PALDeserializer(UserContext userContext, FunctionDefinitionStatement ... customFunctions) {
        this.userCtx = userContext;
        this.customFunctions = customFunctions;
    }

    @Override
    public void deserialize(PolicyAuthor policyAuthor, String s) throws PMException {
        PALExecutor.compileAndExecutePAL(policyAuthor, userCtx, s, customFunctions);
    }
}
