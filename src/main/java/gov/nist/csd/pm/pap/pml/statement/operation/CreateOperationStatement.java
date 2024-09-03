package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PreparedOperation;
import gov.nist.csd.pm.pap.op.operation.CreateAdminOperationOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;

import java.util.Map;
import java.util.Objects;

import static gov.nist.csd.pm.pap.op.operation.CreateAdminOperationOp.OPERATION_OPERAND;

public class CreateOperationStatement extends PreparedOperation<Void> implements CreateFunctionStatement {

    protected PMLStmtsOperation op;

    public CreateOperationStatement(PMLStmtsOperation op) {
        super(new CreateAdminOperationOp(), Map.of(OPERATION_OPERAND, op));

        this.op = op;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return op.toFormattedString(indentLevel);
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        super.execute(pap);

        ctx.scope().global().addExecutable(op.getName(), op);

        return new VoidValue();
    }

    @Override
    public PMLExecutableSignature getSignature() {
        return op.getSignature();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreateOperationStatement)) {
            return false;
        }
        CreateOperationStatement that = (CreateOperationStatement) o;
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(op, that.op);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), op);
    }
}
