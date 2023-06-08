package gov.nist.csd.pm.pap.pml.scope;

import java.io.Serializable;
import java.util.Objects;

public class Scope<V, F> implements Serializable {

    private GlobalScope<V, F> global;
    private LocalScope<V> local;

    public Scope(GlobalScope<V, F> global) {
        this.global = global;
        this.local = new LocalScope<>();
    }

    public Scope(GlobalScope<V, F> global, LocalScope<V> localScope) {
        this.global = global;
        this.local = localScope;
    }

    public Scope<V, F> copy() {
        return new Scope<>(global, local.copy());
    }

    public GlobalScope<V, F> global() {
        return global;
    }

    public LocalScope<V> local() {
        return local;
    }

    public F getFunction(String name) throws UnknownFunctionInScopeException {
        F function = global.getExecutable(name);
        if (function == null) {
            throw new UnknownFunctionInScopeException(name);
        }

        return function;
    }

    public boolean functionExists(String name) {
        try {
            getFunction(name);
            return true;
        } catch (UnknownFunctionInScopeException e) {
            return false;
        }
    }

    public void addVariable(String name, V v) throws VariableAlreadyDefinedInScopeException {
        if (variableExists(name)) {
            throw new VariableAlreadyDefinedInScopeException(name);
        }

        this.local.addVariable(name, v);
    }

    public void addOrOverwriteVariable(String name, V v) {
        this.local.addOrOverwriteVariable(name, v);
    }

    public V getVariable(String name) throws UnknownVariableInScopeException {
        V variable = global.getConstant(name);
        if (variable != null) {
            return variable;
        }

        variable = local.getVariable(name);
        if (variable != null) {
            return variable;
        }

        throw new UnknownVariableInScopeException(name);
    }

    public boolean variableExists(String name) {
        try {
            getVariable(name);
        } catch (UnknownVariableInScopeException e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Scope<?, ?> scope = (Scope<?, ?>) o;
        return Objects.equals(global, scope.global) && Objects.equals(local, scope.local);
    }

    @Override
    public int hashCode() {
        return Objects.hash(global, local);
    }
}
