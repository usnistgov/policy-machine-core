package gov.nist.ngac.pm.core.pap.pml.context;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.pml.scope.ExecuteScope;
import gov.nist.ngac.pm.core.pap.pml.scope.Scope;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.result.BreakResult;
import gov.nist.ngac.pm.core.pap.pml.statement.result.ContinueResult;
import gov.nist.ngac.pm.core.pap.pml.statement.result.ReturnResult;
import gov.nist.ngac.pm.core.pap.pml.statement.result.StatementResult;
import gov.nist.ngac.pm.core.pap.pml.statement.result.VoidResult;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Runtime context for executing PML statements.
 */
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

    /**
     * Returns the user this context executes statements as.
     */
    public UserContext author() {
        return author;
    }

    /**
     * Returns the current variable/operation scope.
     */
    public Scope<Object, Operation<?>> scope() {
        return scope;
    }

    /**
     * Returns a new context with a full copy of this context's scope, for executing a nested block
     * without mutating this one.
     */
    public ExecutionContext copy() throws PMException {
        return new ExecutionContext(author, pap, scope.copy());
    }

    /**
     * Returns a new context copied from the parent scope, for leaving a nested block back to its
     * enclosing scope.
     */
    public ExecutionContext copyWithParentScope() throws PMException {
        return new ExecutionContext(
                author,
                pap,
                scope.getParentScope() == null ? new ExecuteScope(pap) : scope.getParentScope().copy()
        );
    }

    /**
     * Executes a block of statements, stopping early on a return, break, or continue result.
     *
     * @param stmts the statements to execute in order
     * @param args the arguments to bind into the block's scope before executing
     * @return the block's result, or a {@link VoidResult} if none of the statements returned one
     * @throws PMException if executing a statement fails
     */
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

    /**
     * Executes an operation's body, unwrapping its return value.
     *
     * @return the operation's return value, or null if it doesn't return one
     */
    public Object executeOperationStatements(List<PMLStatement<?>> stmts, Args args) throws PMException {
        StatementResult result = executeStatements(stmts, args);

        if (result instanceof ReturnResult returnResult) {
            return returnResult.getValue();
        }

        return null;
    }

    /**
     * Executes a routine's body, unwrapping its return value.
     *
     * @return the routine's return value, or null if it doesn't return one
     */
    public Object executeRoutineStatements(List<PMLStatement<?>> stmts, Args args) throws PMException {
        StatementResult result = executeStatements(stmts, args);

        if (result instanceof ReturnResult returnResult) {
            return returnResult.getValue();
        }

        return null;
    }

    /**
     * Copies this context and binds each argument into the copy's scope.
     */
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
