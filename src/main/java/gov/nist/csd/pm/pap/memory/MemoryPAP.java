package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.pal.PALExecutor;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.model.access.UserContext;

public class MemoryPAP extends PAP {
    public MemoryPAP() throws PMException {
        super(new MemoryConnection(new MemoryPolicyStore()), true);
    }

    private MemoryPAP(boolean verifySuperPolicy) throws PMException {
        super(new MemoryConnection(new MemoryPolicyStore()), verifySuperPolicy);
    }

    @Override
    public void fromPAL(UserContext author, String input, FunctionDefinitionStatement... customFunctions) throws PMException {
        MemoryConnection memoryConnection = new MemoryConnection(new MemoryPolicyStore());
        PALExecutor.execute(memoryConnection, author, input, customFunctions);
        this.init(memoryConnection, true);
    }
}
