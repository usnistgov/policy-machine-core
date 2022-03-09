package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.List;

public interface PALExecutable {

    List<PALStatement> compile(String input) throws PMException;
    void execute(UserContext author, String input) throws PMException;

}
