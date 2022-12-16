package gov.nist.csd.pm.policy.serializer;

import gov.nist.csd.pm.policy.author.*;
import gov.nist.csd.pm.policy.author.pal.PALExecutor;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.Graph;

public class PALDeserializer implements PolicyDeserializer {

    private final UserContext userCtx;
    private final FunctionDefinitionStatement[] customFunctions;

    public PALDeserializer(UserContext userContext, FunctionDefinitionStatement ... customFunctions) {
        this.userCtx = userContext;
        this.customFunctions = customFunctions;
    }

    @Override
    public void deserialize(PolicyAuthor policyAuthor, String s) throws PMException {
        PALExecutor.execute(policyAuthor, userCtx, s, customFunctions);
    }
}
