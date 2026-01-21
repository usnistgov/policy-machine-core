package gov.nist.csd.pm.core.pap.pml.context;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.pml.scope.ExecuteScope;
import gov.nist.csd.pm.core.pap.pml.scope.Scope;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.BreakResult;
import gov.nist.csd.pm.core.pap.pml.statement.result.ContinueResult;
import gov.nist.csd.pm.core.pap.pml.statement.result.ReturnResult;
import gov.nist.csd.pm.core.pap.pml.statement.result.StatementResult;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ExecutionContext implements Serializable {

    protected final UserContext author;
    protected final Scope<Object, Operation<?>> scope;
    protected final PAP pap;

    public ExecutionContext(UserContext author, PAP pap) throws PMException {
        this.author = author;
        this.scope = new ExecuteScope(pap);
        this.pap = pap;
    }

    public ExecutionContext(UserContext author, PAP pap, Scope<Object, Operation<?>> scope) throws PMException {
        this.author = author;
        this.scope = scope;
        this.pap = pap;
    }

    public UserContext author() {
        return author;
    }

    public Scope<Object, Operation<?>> scope() {
        return scope;
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

            if (result instanceof ReturnResult returnResult) {
                return returnResult;
            } else if (result instanceof BreakResult || result instanceof ContinueResult) {
                return (StatementResult) result;
            }
        }

        return new VoidResult();
    }

    public Object executeOperationStatements(List<PMLStatement<?>> stmts, Args args) throws PMException {
        StatementResult result = executeStatements(stmts, args);

        if (result instanceof ReturnResult returnResult) {
            return returnResult.getValue();
        }

        return null;
    }

    public Object executeRoutineStatements(List<PMLStatement<?>> stmts, Args args) throws PMException {
        StatementResult result = executeStatements(stmts, args);

        if (result instanceof ReturnResult returnResult) {
            return returnResult.getValue();
        }

        return null;
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
