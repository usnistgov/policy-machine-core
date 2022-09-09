package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.List;

public interface PALExecutable {

    List<PALStatement> compilePAL(String input) throws PMException;
    void compileAndExecutePAL(UserContext author, String input) throws PMException;
    String toPAL() throws PMException;

}
