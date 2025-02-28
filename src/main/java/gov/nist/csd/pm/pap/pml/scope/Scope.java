package gov.nist.csd.pm.pap.pml.scope;

import java.io.Serializable;
import java.util.Objects;

public class Scope<V, E> implements Serializable {

    private final GlobalScope<V, E> global;
    private final LocalScope<V, E> local;

    public Scope(GlobalScope<V, E> global) {
        this.global = global;
        this.local = new LocalScope<>();
    }

    public Scope(GlobalScope<V, E> global, LocalScope<V, E> localScope) {
        this.global = global;
        this.local = localScope;
    }

    public Scope<V, E> copy() {
        return new Scope<>(global, local.copy());
    }

    public GlobalScope<V, E> global() {
        return global;
    }

    public LocalScope<V, E> local() {
        return local;
    }

    public E getExecutable(String name) throws UnknownFunctionInScopeException {
        E function = global.getExecutable(name);
        if (function == null) {
            throw new UnknownFunctionInScopeException(name);
        }

        return function;
    }

    public boolean executableExists(String name) {
        try {
            getExecutable(name);
            return true;
        } catch (UnknownFunctionInScopeException e) {
            return false;
        }
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
