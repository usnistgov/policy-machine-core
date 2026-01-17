package gov.nist.csd.pm.core.pap.pml.function.basic;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.core.pap.pml.type.TypeResolver;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;
import java.util.Objects;

public class PMLStmtsBasicFunction<T> extends PMLBasicFunction<T> implements PMLStatementSerializable {
    private PMLStatementBlock statements;

    public PMLStmtsBasicFunction(String name, Type<T> returnType, List<FormalParameter<?>> formalArgs, PMLStatementBlock statements) {
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
    public T execute(Void query, Args args) throws PMException {
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
        if (!(o instanceof PMLStmtsBasicFunction<?> that)) return false;
        return Objects.equals(statements, that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(statements);
    }
}
