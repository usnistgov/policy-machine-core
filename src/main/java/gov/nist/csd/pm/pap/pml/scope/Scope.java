package gov.nist.csd.pm.pap.pml.scope;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Scope<V, E> implements Serializable {

    private Map<String, V> constants;
    private Map<String, V> variables;
    private Map<String, E> executables;
    private Scope<V, E> parentScope;

    public Scope(Map<String, V> constants, Map<String, V> variables, Map<String, E> executables, Scope<V, E> parentScope) {
        this.constants = constants;
        this.variables = variables;
        this.executables = executables;
        this.parentScope = parentScope;
    }

    public Scope(Map<String, V> constants, Map<String, V> variables, Map<String, E> executables) {
        this.constants = constants;
        this.variables = variables;
        this.executables = executables;
        this.parentScope = null;
    }

    public Scope() {
        this.constants = new HashMap<>();
        this.variables = new HashMap<>();
        this.executables = new HashMap<>();
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

    public Map<String, E> getExecutables() {
        return executables;
    }

    public void setExecutables(Map<String, E> executables) {
        this.executables = executables;
    }

    public Scope<V, E> getParentScope() {
        return parentScope;
    }

    public void setParentScope(Scope<V, E> parentScope) {
        this.parentScope = parentScope;
    }

    public Scope<V, E> copy() {
        return new Scope<>(
                new HashMap<>(this.constants),
                new HashMap<>(this.variables),
                new HashMap<>(this.executables),
                this.parentScope != null ? this.parentScope.copy() : null
        );
    }

    public E getExecutable(String name) throws UnknownExecutableInScopeException {
        E function = executables.get(name);
        if (function == null) {
            throw new UnknownExecutableInScopeException(name);
        }

        return function;
    }

    public boolean executableExists(String name) {
        return executables.containsKey(name);
    }

    public void addExecutable(String name, E e) throws ExecutableAlreadyDefinedInScopeException {
        if (parentHasExecutables(name) || executables.containsKey(name)) {
            throw new ExecutableAlreadyDefinedInScopeException(name);
        }

        executables.put(name, e);
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

    public void overwriteFromScope(Scope<V, E> scope) {
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

    private boolean parentHasExecutables(String name) {
        return parentScope != null && parentScope.executables.containsKey(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Scope<?, ?> scope)) return false;
	    return Objects.equals(constants, scope.constants) && Objects.equals(
                variables,
                scope.variables
        ) && Objects.equals(executables, scope.executables) && Objects.equals(parentScope, scope.parentScope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constants, variables, executables, parentScope);
    }
}
