package gov.nist.csd.pm.pap.serialization.pml;

import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

public class PMLDeserializer {

    public static void fromPML(PolicyStore policyStore, UserContext author, String input,
                               FunctionDefinitionStatement... customFunctions) throws PMException {
        PMLExecutor.compileAndExecutePML(policyStore, author, input, customFunctions);
    }

}
