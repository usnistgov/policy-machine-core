package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

public class PMLBootstrapper implements PolicyBootstrapper{

    private UserContext author;
    private String pml;
    private FunctionDefinitionStatement[] customFunctions;

    public PMLBootstrapper(UserContext author, String pml, FunctionDefinitionStatement ... customFunctions) {
        this.author = author;
        this.pml = pml;
        this.customFunctions = customFunctions;
    }

    @Override
    public void bootstrap(PAP pap) throws PMException {
        pap.deserialize().fromPML(author, pml, customFunctions);
    }
}
