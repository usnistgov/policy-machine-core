package gov.nist.csd.pm.pap.pml.scope;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LocalScope<V, E> implements Serializable {

    private final Map<String, V> variables;
    private final Map<String, E> executables;
    private final LocalScope<V, E> parentScope;

    public LocalScope() {
        variables = new HashMap<>();
        executables = new HashMap<>();
        parentScope = null;
    }

    public LocalScope(LocalScope<V, E> parentScope) {
        variables = new HashMap<>();
        executables = new HashMap<>();
        this.parentScope = parentScope;
    }

    public LocalScope<V, E> copy() {
        LocalScope<V, E> copy = new LocalScope<>(this);
        copy.variables.putAll(variables);
        copy.executables.putAll(executables);

        return copy;
    }

    public void clearVariables() {
        variables.clear();
    }

    public LocalScope<V, E> parentScope() {
        return parentScope;
    }

    public Map<String, V> getVariables() {
        Map<String, V> variables = new HashMap<>();

        if (parentScope != null) {
            variables.putAll(this.parentScope.variables);
        }

        variables.putAll(this.variables);

        return variables;
    }

    public V getVariable(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        } else if (parentScope != null) {
            return parentScope.getVariable(name);
        }

        return null;
    }

    public void addVariable(String name, V v) throws VariableAlreadyDefinedInScopeException {
        if ((parentScope != null && parentScope.variables.containsKey(name)) ||
                variables.containsKey(name)) {
            throw new VariableAlreadyDefinedInScopeException(name);
        }

        variables.put(name, v);
    }

    public void addOrOverwriteVariable(String name, V v) {
        variables.put(name, v);
    }

    public void overwriteFromLocalScope(LocalScope<V, E> localScope) {
        for (String varName : localScope.variables.keySet()) {
            if (!this.variables.containsKey(varName)) {
                continue;
            }

            this.variables.put(varName, localScope.variables.get(varName));
        }
    }

    public void addExecutable(String name, E e) throws ExecutableAlreadyDefinedInScopeException {
        if ((parentScope != null && parentScope.variables.containsKey(name)) ||
                variables.containsKey(name)) {
            throw new ExecutableAlreadyDefinedInScopeException(name);
        }

        executables.put(name, e);
    }

    public E getExecutable(String name) {
        if (executables.containsKey(name)) {
            return executables.get(name);
        } else if (parentScope != null) {
            return parentScope.getExecutable(name);
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocalScope<?, ?> that = (LocalScope<?, ?>) o;
        return Objects.equals(variables, that.variables) && Objects.equals(
                parentScope, that.parentScope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variables, parentScope);
    }
}
