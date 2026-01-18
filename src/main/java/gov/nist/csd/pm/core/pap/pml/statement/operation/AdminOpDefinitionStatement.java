package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.function.op.operation.CreateAdminOperationOp.ADMIN_OPERATION_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLStmtsAdminOperation;
import gov.nist.csd.pm.core.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;
import java.util.Objects;

public class AdminOpDefinitionStatement extends OperationStatement implements FunctionDefinitionStatement {

    protected PMLStmtsAdminOperation<?> pmlStmtsOperation;

    public AdminOpDefinitionStatement(PMLStmtsAdminOperation<?> pmlStmtsOperation) {
        super(new CreateAdminOperationOp());

        this.pmlStmtsOperation = pmlStmtsOperation;
    }

    @Override
    public PMLFunctionSignature getSignature() {
        return pmlStmtsOperation.getSignature();
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        return new Args().put(ADMIN_OPERATION_PARAM, pmlStmtsOperation);
    }

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        // add operation to policy
        VoidResult value = super.execute(ctx, pap);

        // add operation to scope
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
        if (!(o instanceof AdminOpDefinitionStatement that))
            return false;
        return Objects.equals(op, that.op);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(op);
    }
}
