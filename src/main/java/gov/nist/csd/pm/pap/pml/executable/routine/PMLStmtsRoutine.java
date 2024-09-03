package gov.nist.csd.pm.pap.pml.executable.routine;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PMLStmtsRoutine extends PMLRoutine implements PMLStatementSerializable {
    private PMLStatementBlock statements;

    public PMLStmtsRoutine(String name, Type returnType, List<String> operandNames, Map<String, Type> operandTypes, PMLStatementBlock statements) {
        super(name, returnType, operandNames, operandTypes);
        this.statements = statements;
    }

    public PMLStatementBlock getStatements() {
        return statements;
    }

    public void setStatements(PMLStatementBlock statements) {
        this.statements = statements;
    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        ExecutionContext ctx = getCtx();

        return ctx.executeRoutineStatements(statements.getStmts(), operands);
    }


    @Override
    public String toFormattedString(int indentLevel) {
        return String.format(
                "%s%s",
                getSignature().toFormattedString(indentLevel),
                getStatements().toFormattedString(indentLevel)
        );
    }

    @Override
    public String toString() {
        return toFormattedString(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PMLStmtsRoutine)) return false;
        PMLStmtsRoutine that = (PMLStmtsRoutine) o;
        return Objects.equals(statements, that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(statements);
    }
}
