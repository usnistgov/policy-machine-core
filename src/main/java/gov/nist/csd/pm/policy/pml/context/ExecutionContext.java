package gov.nist.csd.pm.policy.pml.context;

import gov.nist.csd.pm.policy.exceptions.PMLFunctionNotDefinedException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.scope.*;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.io.Serializable;
import java.util.Objects;

public class ExecutionContext implements Serializable {

    private final UserContext author;
    private final Scope<Value, FunctionDefinitionStatement> scope;

    public ExecutionContext(UserContext author, Scope<Value, FunctionDefinitionStatement> scope) {
        this.author = author;
        this.scope = scope;
    }

    public ExecutionContext(UserContext author, GlobalScope<Value, FunctionDefinitionStatement> globalScope) {
        this.author = author;
        this.scope = new Scope<>(globalScope);
    }

    public UserContext author() {
        return author;
    }

    public Scope<Value, FunctionDefinitionStatement> scope() {
        return scope;
    }

    public ExecutionContext copy() throws UnknownFunctionInScopeException, FunctionAlreadyDefinedInScopeException,
                                          UnknownVariableInScopeException, VariableAlreadyDefinedInScopeException,
                                          PMLFunctionNotDefinedException {
        return new ExecutionContext(this.author, this.scope.copy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExecutionContext that = (ExecutionContext) o;
        return Objects.equals(author, that.author) && Objects.equals(scope, that.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, scope);
    }
}
