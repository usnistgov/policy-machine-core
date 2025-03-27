package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.statement.CreateExecutableStatement;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import java.util.Objects;

public class CreateOperationStatement extends OperationStatement<CreateAdminOperationOp> implements CreateExecutableStatement {

    protected PMLStmtsOperation pmlStmtsOperation;

    public CreateOperationStatement(PMLStmtsOperation pmlStmtsOperation) {
        super(new CreateAdminOperationOp());

        this.pmlStmtsOperation = pmlStmtsOperation;
    }

    @Override
    public PMLExecutableSignature getSignature() {
        return pmlStmtsOperation.getSignature();
    }

    @Override
    public ActualArgs prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        return op.actualArgs(pmlStmtsOperation);
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        Value value = super.execute(ctx, pap);

        ctx.scope().addExecutable(pmlStmtsOperation.getName(), pmlStmtsOperation);

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
        if (!(o instanceof CreateOperationStatement that))
            return false;
        return Objects.equals(op, that.op);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(op);
    }
}
