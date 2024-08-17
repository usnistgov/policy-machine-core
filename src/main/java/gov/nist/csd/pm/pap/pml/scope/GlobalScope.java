package gov.nist.csd.pm.pap.pml.scope;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class GlobalScope<V, F> implements Serializable {

    private Map<String, V> constants;
    private Map<String, F> executables;

    protected GlobalScope() {
        constants = new HashMap<>();
        executables = new HashMap<>();
    }

    public GlobalScope(Map<String, V> constants, Map<String, F> executables) {
        this.constants = constants;
        this.executables = executables;
    }

    public void addConstant(String key, V value) {
        this.constants.put(key, value);
    }

    public void addExecutable(String name, F operation) {
        this.executables.put(name, operation);
    }

    public V getConstant(String varName) {
        return constants.get(varName);
    }

    public F getExecutable(String funcName) {
        return executables.get(funcName);
    }

    public Map<String, F> getExecutables() {
        return executables;
    }

    public Map<String, V> getConstants() {
        return constants;
    }

    public void addExecutables(Map<String, F> funcs) {
        executables.putAll(funcs);
    }

    public void addConstants(Map<String, V> c) {
        constants.putAll(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlobalScope<?, ?> that = (GlobalScope<?, ?>) o;
        return Objects.equals(constants, that.constants) && Objects.equals(executables, that.executables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constants, executables);
    }
}
