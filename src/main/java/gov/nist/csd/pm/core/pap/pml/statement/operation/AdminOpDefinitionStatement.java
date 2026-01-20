package gov.nist.csd.pm.core.pap.pml.statement.operation;


import static gov.nist.csd.pm.core.pap.operation.operation.CreateAdminOperationOp.ADMIN_OPERATION_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.admin.PMLStmtsAdminOperation;
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
    public PMLOperationSignature getSignature() {
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
        ctx.scope().addOperation(pmlStmtsOperation.getName(), pmlStmtsOperation);

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
