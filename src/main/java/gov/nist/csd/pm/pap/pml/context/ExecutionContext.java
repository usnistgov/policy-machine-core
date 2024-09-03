package gov.nist.csd.pm.pap.pml.context;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.AdminExecutable;
import gov.nist.csd.pm.pap.pml.scope.ExecuteGlobalScope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.value.*;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.pml.scope.Scope;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExecutionContext implements Serializable {

    protected final UserContext author;
    protected final Scope<Value, AdminExecutable<?>> scope;
    protected final PAP pap;
    private boolean isExplain;

    public ExecutionContext(UserContext author, PAP pap) throws PMException {
        this.author = author;
        this.scope = new Scope<>(new ExecuteGlobalScope(pap));
        this.pap = pap;
        this.isExplain = false;
    }

    public ExecutionContext(UserContext author, PAP pap, Scope<Value, AdminExecutable<?>> scope) throws PMException {
        this.author = author;
        this.scope = scope;
        this.pap = pap;
        this.isExplain = false;
    }

    public UserContext author() {
        return author;
    }

    public Scope<Value, AdminExecutable<?>> scope() {
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

    public ExecutionContext copyWithoutScope() throws PMException {
        return new ExecutionContext(author, pap);
    }

    public Value executeStatements(List<PMLStatement> stmts, Map<String, Object> operands) throws PMException {
        ExecutionContext copy = writeOperandsToScope(operands);

        for (PMLStatement statement : stmts) {
            Value value = statement.execute(copy, pap);

            scope.local().overwriteFromLocalScope(copy.scope.local());

            if (value instanceof ReturnValue || value instanceof BreakValue || value instanceof ContinueValue) {
                return value;
            }
        }

        return new VoidValue();
    }

    public Value executeOperationStatements(List<PMLStatement> stmts, Map<String, Object> operands) throws PMException {
        return executeStatements(stmts, operands);
    }

    public Value executeRoutineStatements(List<PMLStatement> stmts, Map<String, Object> operands) throws PMException {
        return executeStatements(stmts, operands);
    }

    protected ExecutionContext writeOperandsToScope(Map<String, Object> operands) throws PMException {
        ExecutionContext copy = this.copy();

        for (Map.Entry<String, Object> entry : operands.entrySet()) {
            String key = entry.getKey();
            Object o = entry.getValue();

            Value value;
            if (o instanceof Value) {
                value = (Value) o;
            } else {
                value = Value.fromObject(o);
            }

            copy.scope.local().addOrOverwriteVariable(key, value);
        }

        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExecutionContext)) {
            return false;
        }
        ExecutionContext that = (ExecutionContext) o;
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
