package gov.nist.csd.pm.core.pap.pml.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

public class PMLRequiredCapabilityFunc extends RequiredCapability {

    private final PMLStatementBlock stmts;

    public PMLRequiredCapabilityFunc(PMLStatementBlock stmts) {
        this.stmts = stmts;
    }

    public PMLStatementBlock getStmts() {
        return stmts;
    }

    @Override
    public boolean isSatisfied(PAP pap, UserContext userCtx, Args args) throws PMException {
        ExecutionContext executionCtx = pap.buildExecutionContext(userCtx);
        executionCtx.executeStatements(stmts.getStmts(), args);

        // statements will throw an exception if check statement not satisfied
        return true;
    }
}
