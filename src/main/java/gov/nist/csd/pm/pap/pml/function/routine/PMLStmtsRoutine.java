package gov.nist.csd.pm.pap.pml.function.routine;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PMLStmtsRoutine extends PMLRoutine implements PMLStatementSerializable {
    private PMLStatementBlock statements;

    public PMLStmtsRoutine(String name, Type<?> returnType, List<FormalParameter<?>> formalParameters, PMLStatementBlock statements) {
        super(name, returnType, formalParameters);
        this.statements = statements;
    }

    public PMLStatementBlock getStatements() {
        return statements;
    }

    public void setStatements(PMLStatementBlock statements) {
        this.statements = statements;
    }

    @Override
    protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        return new Args(argsMap);
    }

    @Override
    public Object execute(PAP pap, Args args) throws PMException {
        ExecutionContext ctx = getCtx();

        return ctx.executeRoutineStatements(statements.getStmts(), args);
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
        if (!(o instanceof PMLStmtsRoutine that)) return false;
        return Objects.equals(statements, that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(statements);
    }
}
