package gov.nist.csd.pm.core.pap.pml.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapabilityFunc;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.Map;

public class PMLRequiredCapabilityFunc extends RequiredCapabilityFunc implements PMLStatementSerializable {

    private PMLStatementBlock statementBlock;

    public PMLRequiredCapabilityFunc(PMLStatementBlock statementBlock) {
        super(new PMLRequiredCapabilityFuncExecutor(statementBlock));
        this.statementBlock = statementBlock;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("@reqcap(%s)", statementBlock.toFormattedString(indentLevel));
    }

    static class PMLRequiredCapabilityFuncExecutor implements RequiredCapabilityFuncExecutor {

        private final PMLStatementBlock block;

        public PMLRequiredCapabilityFuncExecutor(PMLStatementBlock block) {
            this.block = block;
        }

        @Override
        public boolean isSatisfied(PAP pap, UserContext userCtx, Args args) {
            try {
                ExecutionContext execCtx = pap.buildExecutionContext(userCtx);

                // add args to execution context since they aren't passed to execute
                Map<String, Object> map = args.toMap();
                for (Map.Entry<String, Object> e : map.entrySet()) {
                    execCtx.scope().addVariable(e.getKey(), e.getValue());
                }

                block.execute(execCtx, pap);
                return true;
            } catch (PMException e) {
                return false;
            }
        }
    }
}
