package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

public interface PMLExecutable {

    void executePML(UserContext userContext, String input,
                    FunctionDefinitionStatement... functionDefinitionStatements) throws PMException;

}
