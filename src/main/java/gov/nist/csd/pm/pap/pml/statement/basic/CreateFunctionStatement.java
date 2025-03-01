package gov.nist.csd.pm.pap.pml.statement.basic;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutineSignature;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.statement.CreateExecutableStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateRoutineStatement;
import gov.nist.csd.pm.pap.pml.value.VoidValue;

import java.util.Objects;

public class CreateFunctionStatement implements CreateExecutableStatement {

	private final PMLStmtsRoutine routine;
	private final PMLFunctionSignature signature;

	public CreateFunctionStatement(PMLStmtsRoutine routine) {
		this.routine = routine;
		this.signature = new PMLFunctionSignature(
				routine.getName(),
				routine.getReturnType(),
				routine.getOperandNames(),
				routine.getOperandTypes()
		);
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
	public VoidValue execute(ExecutionContext ctx, PAP pap) throws PMException {
		ctx.scope().addExecutable(routine.getName(), routine);

		return new VoidValue();
	}

	@Override
	public PMLExecutableSignature getSignature() {
		return signature;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CreateFunctionStatement that)) return false;
		return Objects.equals(routine, that.routine);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), routine);
	}
}

