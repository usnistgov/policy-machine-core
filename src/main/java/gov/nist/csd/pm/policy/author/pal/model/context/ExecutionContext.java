package gov.nist.csd.pm.policy.author.pal.model.context;

import gov.nist.csd.pm.policy.author.pal.compiler.Variable;
import gov.nist.csd.pm.policy.author.pal.model.scope.*;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.io.Serializable;
import java.util.Objects;

import static gov.nist.csd.pm.policy.author.pal.model.scope.Scope.Mode.EXECUTE;

public class ExecutionContext implements Serializable {

    private final UserContext author;
    private final Scope scope;

    public ExecutionContext(UserContext author) {
        this.author = author;
        this.scope = new Scope(EXECUTE);
    }

    public UserContext author() {
        return author;
    }

    public Scope scope() {
        return scope;
    }

    public ExecutionContext copy() throws UnknownFunctionInScopeException, FunctionAlreadyDefinedInScopeException,
            UnknownVariableInScopeException, VariableAlreadyDefinedInScopeException {
        ExecutionContext copy = new ExecutionContext(this.author);

        for (String funcName : this.scope.functions().keySet()) {
            copy.scope.addFunction(this.scope.getFunction(funcName));
        }

        for (String varName : this.scope.variables().keySet()) {
            Variable variable = this.scope.getVariable(varName);
            copy.scope.addVariable(varName, variable.type(), variable.isConst());
        }

        for (String varName : this.scope.values().keySet()) {
            copy.scope.addValue(varName, this.scope.getValue(varName));
        }

        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionContext that = (ExecutionContext) o;
        return Objects.equals(author, that.author) && Objects.equals(scope, that.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, scope);
    }
}
