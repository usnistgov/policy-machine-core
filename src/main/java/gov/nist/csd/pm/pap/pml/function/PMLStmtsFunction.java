package gov.nist.csd.pm.pap.pml.function;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.routine.PMLRoutine;
import gov.nist.csd.pm.pap.pml.function.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class PMLStmtsFunction extends PMLFunction implements PMLStatementSerializable {
    private PMLStatementBlock statements;

    public PMLStmtsFunction(String name, Type returnType, List<PMLFormalArg> formalArgs, PMLStatementBlock statements) {
        super(name, returnType, formalArgs);
        this.statements = statements;
    }

    public PMLStatementBlock getStatements() {
        return statements;
    }

    public void setStatements(PMLStatementBlock statements) {
        this.statements = statements;
    }

    @Override
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        ExecutionContext ctx = getCtx();

        return ctx.executeRoutineStatements(statements.getStmts(), actualArgs);
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
        if (!(o instanceof PMLStmtsFunction that)) return false;
        return Objects.equals(statements, that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(statements);
    }
}
