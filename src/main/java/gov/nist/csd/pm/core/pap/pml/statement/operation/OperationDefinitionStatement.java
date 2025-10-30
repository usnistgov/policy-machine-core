package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.function.op.operation.CreateAdminOperationOp.OPERATION_PARAM;

import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;

import gov.nist.csd.pm.core.pap.pml.function.operation.PMLStmtsOperation;
import gov.nist.csd.pm.core.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;
import java.util.Objects;

public class OperationDefinitionStatement extends OperationStatement implements FunctionDefinitionStatement {

    protected PMLStmtsOperation pmlStmtsOperation;

    public OperationDefinitionStatement(PMLStmtsOperation pmlStmtsOperation) {
        super(new CreateAdminOperationOp());

        this.pmlStmtsOperation = pmlStmtsOperation;
    }

    @Override
    public PMLFunctionSignature getSignature() {
        return pmlStmtsOperation.getSignature();
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        return new Args().put(OPERATION_PARAM, pmlStmtsOperation);
    }

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        VoidResult value = super.execute(ctx, pap);

        ctx.scope().addFunction(pmlStmtsOperation.getName(), pmlStmtsOperation);

        return value;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return pmlStmtsOperation.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OperationDefinitionStatement that))
            return false;
        return Objects.equals(op, that.op);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(op);
    }
}
