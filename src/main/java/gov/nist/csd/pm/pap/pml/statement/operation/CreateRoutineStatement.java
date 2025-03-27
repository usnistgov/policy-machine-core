package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.routine.CreateAdminRoutineOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.statement.CreateExecutableStatement;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import java.util.Objects;

public class CreateRoutineStatement extends OperationStatement<CreateAdminRoutineOp> implements CreateExecutableStatement {

    protected PMLStmtsRoutine pmlStmtsRoutine;

    public CreateRoutineStatement(PMLStmtsRoutine pmlStmtsRoutine) {
        super(new CreateAdminRoutineOp());

        this.pmlStmtsRoutine = pmlStmtsRoutine;
    }

    @Override
    public PMLExecutableSignature getSignature() {
        return pmlStmtsRoutine.getSignature();
    }

    @Override
    public ActualArgs prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        return op.actualArgs(pmlStmtsRoutine);
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        Value value = super.execute(ctx, pap);

        ctx.scope().addExecutable(pmlStmtsRoutine.getName(), pmlStmtsRoutine);

        return value;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return pmlStmtsRoutine.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CreateRoutineStatement that))
            return false;
        return Objects.equals(pmlStmtsRoutine, that.pmlStmtsRoutine);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pmlStmtsRoutine);
    }
}
