package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.statement.ControlStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.Map;
import java.util.Objects;

public class PMLStmtsOperationBody extends ControlStatement {

	private final PMLStatementBlock checks;
	private final PMLStatementBlock statements;

	public PMLStmtsOperationBody(PMLStatementBlock checks, PMLStatementBlock statements) {
		this.checks = checks;
		this.statements = statements;
	}

	public PMLStatementBlock getChecks() {
		return checks;
	}

	public PMLStatementBlock getStatements() {
		return statements;
	}

	@Override
	public String toFormattedString(int indentLevel) {
		return checks.toFormattedString(indentLevel) + " " + statements.toFormattedString(indentLevel);
	}

	@Override
	public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
		ctx.executeOperationStatements(checks.getStmts(), Map.of());
		return ctx.executeStatements(statements.getStmts(), Map.of());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PMLStmtsOperationBody that)) return false;
		return Objects.equals(checks, that.checks) && Objects.equals(statements, that.statements);
	}

	@Override
	public int hashCode() {
		return Objects.hash(checks, statements);
	}
}
