package gov.nist.csd.pm.pap.pml.context;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.scope.ExecuteScope;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.value.*;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ExecutionContext implements Serializable {

    protected final UserContext author;
    protected final Scope<Value, AdminFunction<?>> scope;
    protected final PAP pap;
    private boolean isExplain;

    public ExecutionContext(UserContext author, PAP pap) throws PMException {
        this.author = author;
        this.scope = new ExecuteScope(pap);
        this.pap = pap;
        this.isExplain = false;
    }

    public ExecutionContext(UserContext author, PAP pap, Scope<Value, AdminFunction<?>> scope) throws PMException {
        this.author = author;
        this.scope = scope;
        this.pap = pap;
        this.isExplain = false;
    }

    public UserContext author() {
        return author;
    }

    public Scope<Value, AdminFunction<?>> scope() {
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

    public Value executeStatements(List<PMLStatement> stmts, Args args) throws PMException {
        ExecutionContext copy = writeArgsToScope(args);

        for (PMLStatement statement : stmts) {
            Value value = statement.execute(copy, pap);

            scope.overwriteFromScope(copy.scope);

            if (value instanceof ReturnValue || value instanceof BreakValue || value instanceof ContinueValue) {
                return value;
            }
        }

        return new VoidValue();
    }

    public Value executeOperationStatements(List<PMLStatement> stmts, Args args) throws PMException {
        return executeStatements(stmts, args);
    }

    public Value executeRoutineStatements(List<PMLStatement> stmts, Args args) throws PMException {
        return executeStatements(stmts, args);
    }

    protected ExecutionContext writeArgsToScope(Args args) throws PMException {
        ExecutionContext copy = this.copy();

        args.foreach((formalArg, o) -> {
            String key = formalArg.getName();

            Value value;
            if (o instanceof Value) {
                value = (Value) o;
            } else {
                value = Value.fromObject(o);
            }

            copy.scope.updateVariable(key, value);
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
