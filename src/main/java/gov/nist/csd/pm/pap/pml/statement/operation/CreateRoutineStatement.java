package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.executable.op.PreparedOperation;
import gov.nist.csd.pm.pap.executable.op.routine.CreateAdminRoutineOp;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutineSignature;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.statement.CreateExecutableStatement;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;

import java.util.Map;
import java.util.Objects;

import static gov.nist.csd.pm.pap.executable.op.routine.CreateAdminRoutineOp.ROUTINE_OPERAND;

public class CreateRoutineStatement extends PreparedOperation<Void> implements CreateExecutableStatement {

    private final PMLStmtsRoutine routine;

    public CreateRoutineStatement(PMLStmtsRoutine routine) {
        super(new CreateAdminRoutineOp(), Map.of(ROUTINE_OPERAND, routine));

        this.routine = routine;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format(
                "%s%s",
                new PMLRoutineSignature(routine.getName(), routine.getReturnType(), routine.getOperandNames(), routine.getOperandTypes())
                        .toFormattedString(indentLevel),
                routine.getStatements().toFormattedString(indentLevel)
        );
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        super.execute(pap);

        ctx.scope().addExecutable(routine.getName(), routine);

        return new VoidValue();
    }

    @Override
    public PMLExecutableSignature getSignature() {
        return routine.getSignature();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateRoutineStatement that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(routine, that.routine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), routine);
    }
}
