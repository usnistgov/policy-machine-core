package gov.nist.csd.pm.pap.pml.context;

import com.sun.jdi.VoidValue;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.scope.ExecuteScope;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.result.BreakResult;
import gov.nist.csd.pm.pap.pml.statement.result.ContinueResult;
import gov.nist.csd.pm.pap.pml.statement.result.ReturnResult;
import gov.nist.csd.pm.pap.pml.statement.result.StatementResult;
import gov.nist.csd.pm.pap.pml.statement.result.VoidResult;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ExecutionContext implements Serializable {

    protected final UserContext author;
    protected final Scope<Object, AdminFunction<?, ?>> scope;
    protected final PAP pap;
    private boolean isExplain;

    public ExecutionContext(UserContext author, PAP pap) throws PMException {
        this.author = author;
        this.scope = new ExecuteScope(pap);
        this.pap = pap;
        this.isExplain = false;
    }

    public ExecutionContext(UserContext author, PAP pap, Scope<Object, AdminFunction<?, ?>> scope) throws PMException {
        this.author = author;
        this.scope = scope;
        this.pap = pap;
        this.isExplain = false;
    }

    public UserContext author() {
        return author;
    }

    public Scope<Object, AdminFunction<?, ?>> scope() {
        return scope;
    }

    public boolean isExplain() {
        return isExplain;
    }

    public void setExplain(boolean explain) {
        isExplain = explain;
    }

    public ExecutionContext copy() throws PMException {
        return new ExecutionContext(author, pap, scope.copy());
    }

    public ExecutionContext copyWithParentScope() throws PMException {
        return new ExecutionContext(
                author,
                pap,
                scope.getParentScope() == null ? new ExecuteScope(pap) : scope.getParentScope().copy()
        );
    }

    public StatementResult executeStatements(List<PMLStatement<?>> stmts, Args args) throws PMException {
        ExecutionContext copy = writeArgsToScope(args);

        for (PMLStatement<?> statement : stmts) {
            Object result = statement.execute(copy, pap);

            scope.overwriteFromScope(copy.scope);

            if (result instanceof ReturnResult || result instanceof BreakResult || result instanceof ContinueResult) {
                return (StatementResult) result;
            }
        }

        return new VoidResult();
    }

    public StatementResult executeOperationStatements(List<PMLStatement<?>> stmts, Args args) throws PMException {
        return executeStatements(stmts, args);
    }

    public StatementResult executeRoutineStatements(List<PMLStatement<?>> stmts, Args args) throws PMException {
        return executeStatements(stmts, args);
    }

    protected ExecutionContext writeArgsToScope(Args args) throws PMException {
        ExecutionContext copy = this.copy();

        args.foreach((formalArg, o) -> {
            String key = formalArg.getName();

            copy.scope.updateVariable(key, o);
        });

        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExecutionContext that)) {
            return false;
        }
        return Objects.equals(author, that.author) && Objects.equals(
                scope,
                that.scope
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, scope);
    }

}
