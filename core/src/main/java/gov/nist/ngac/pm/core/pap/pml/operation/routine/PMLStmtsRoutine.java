package gov.nist.ngac.pm.core.pap.pml.operation.routine;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;
import java.util.Objects;

/**
 * A PML "create routine" definition's runtime form, executing its {@link PMLStatementBlock} body.
 */
public class PMLStmtsRoutine<T> extends PMLRoutine<T> implements PMLStatementSerializable {

    private PMLStatementBlock statements;

    public PMLStmtsRoutine(String name, Type<T> returnType, List<FormalParameter<?>> formalParameters, PMLStatementBlock statements) {
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
    public T execute(PAP pap, UserContext userCtx, Args args) throws PMException {
        ExecutionContext ctx = getCtx();

        Object result = ctx.executeRoutineStatements(statements.getStmts(), args);

        return getReturnType().cast(result);
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
        if (!(o instanceof PMLStmtsRoutine<?> that)) return false;
        return Objects.equals(statements, that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(statements);
    }
}
