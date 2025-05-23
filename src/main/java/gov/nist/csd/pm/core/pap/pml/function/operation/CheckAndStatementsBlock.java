package gov.nist.csd.pm.core.pap.pml.function.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.statement.basic.BasicStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;

import java.util.Objects;

public class CheckAndStatementsBlock extends BasicStatement {

	private final PMLStatementBlock checks;
	private final PMLStatementBlock statements;

	public CheckAndStatementsBlock(PMLStatementBlock checks, PMLStatementBlock statements) {
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
	public Object execute(ExecutionContext ctx, PAP pap) throws PMException {
		ctx.executeOperationStatements(checks.getStmts(), new Args());

		return ctx.executeStatements(statements.getStmts(), new Args());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CheckAndStatementsBlock that)) return false;
		return Objects.equals(checks, that.checks) && Objects.equals(statements, that.statements);
	}

	@Override
	public int hashCode() {
		return Objects.hash(checks, statements);
	}
}
