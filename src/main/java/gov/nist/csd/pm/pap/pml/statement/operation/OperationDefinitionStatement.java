package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;

import gov.nist.csd.pm.pap.pml.function.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.pap.pml.value.Value;
import java.util.Objects;

public class OperationDefinitionStatement extends OperationStatement<CreateAdminOperationOp> implements FunctionDefinitionStatement {

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
        return op.actualArgs(pmlStmtsOperation);
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        Value value = super.execute(ctx, pap);

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
