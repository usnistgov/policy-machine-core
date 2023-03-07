package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

public interface PALExecutable {

    void executePAL(UserContext userContext, String input,
                    FunctionDefinitionStatement... functionDefinitionStatements) throws PMException;

}
