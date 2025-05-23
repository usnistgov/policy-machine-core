package gov.nist.csd.pm.core.pap.pml.scope;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Scope<V, F> implements Serializable {

    private Map<String, V> constants;
    private Map<String, V> variables;
    private Map<String, F> functions;
    private Scope<V, F> parentScope;

    public Scope(Map<String, V> constants, Map<String, V> variables, Map<String, F> functions, Scope<V, F> parentScope) {
        this.constants = constants;
        this.variables = variables;
        this.functions = functions;
        this.parentScope = parentScope;
    }

    public Scope(Map<String, V> constants, Map<String, V> variables, Map<String, F> functions) {
        this.constants = constants;
        this.variables = variables;
        this.functions = functions;
        this.parentScope = null;
    }

    public Scope() {
        this.constants = new HashMap<>();
        this.variables = new HashMap<>();
        this.functions = new HashMap<>();
        this.parentScope = null;
    }

    public Map<String, V> getConstants() {
        return constants;
    }

    public void setConstants(Map<String, V> constants) {
        this.constants = constants;
    }

    public Map<String, V> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, V> variables) {
        this.variables = variables;
    }

    public Map<String, F> getFunctions() {
        return functions;
    }

    public void setFunctions(Map<String, F> functions) {
        this.functions = functions;
    }

    public Scope<V, F> getParentScope() {
        return parentScope;
    }

    public void setParentScope(Scope<V, F> parentScope) {
        this.parentScope = parentScope;
    }

    public Scope<V, F> copy() {
        return new Scope<>(
                new HashMap<>(this.constants),
                new HashMap<>(this.variables),
                new HashMap<>(this.functions),
                this.parentScope != null ? this.parentScope.copy() : null
        );
    }

    public F getFunction(String name) throws UnknownFunctionInScopeException {
        F function = functions.get(name);
        if (function == null) {
            throw new UnknownFunctionInScopeException(name);
        }

        return function;
    }

    public boolean functionExists(String name) {
        return functions.containsKey(name);
    }

    public void addFunction(String name, F f) throws FunctionAlreadyDefinedInScopeException {
        if (parentHasFunction(name) || functions.containsKey(name)) {
            throw new FunctionAlreadyDefinedInScopeException(name);
        }

        functions.put(name, f);
    }

    public V getVariable(String name) throws UnknownVariableInScopeException {
        V variable = constants.get(name);
        if (variable != null) {
            return variable;
        }

        variable = variables.get(name);
        if (variable != null) {
            return variable;
        }

        throw new UnknownVariableInScopeException(name);
    }

    public boolean variableExists(String name) {
        return variables.containsKey(name);
    }

    public void addVariable(String name, V v) throws VariableAlreadyDefinedInScopeException {
        if (parentHasVariable(name) || variables.containsKey(name)) {
            throw new VariableAlreadyDefinedInScopeException(name);
        }

        variables.put(name, v);
    }

    public void updateVariable(String name, V value) {
        variables.put(name, value);
    }

    public void overwriteFromScope(Scope<V, F> scope) {
        for (String varName : scope.variables.keySet()) {
            if (!this.variables.containsKey(varName)) {
                continue;
            }

            this.variables.put(varName, scope.variables.get(varName));
        }
    }

    private boolean parentHasVariable(String name) {
        return parentScope != null && parentScope.variables.containsKey(name);
    }

    private boolean parentHasFunction(String name) {
        return parentScope != null && parentScope.functions.containsKey(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Scope<?, ?> scope)) return false;
	    return Objects.equals(constants, scope.constants) && Objects.equals(
                variables,
                scope.variables
        ) && Objects.equals(functions, scope.functions) && Objects.equals(parentScope, scope.parentScope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constants, variables, functions, parentScope);
    }
}
