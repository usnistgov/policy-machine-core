package gov.nist.csd.pm.core.pap.pml.scope;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Scope<V, F> implements Serializable {

    private PAP pap;
    private Map<String, V> constants;
    private Map<String, V> variables;
    private Map<String, F> operations;
    private Scope<V, F> parentScope;

    public Scope(PAP pap, Map<String, V> constants, Map<String, F> operations) throws PMException {
        this.pap = pap;
        this.constants = constants;
        this.variables = new HashMap<>();
        this.operations = operations;
    }

    public Scope(PAP pap, Map<String, V> constants, Map<String, F> operations, Scope<V, F> parentScope) throws PMException {
        this.pap = pap;
        this.constants = constants;
        this.variables = new HashMap<>();
        this.operations = operations;
        this.parentScope = parentScope;
    }

    protected Scope(PAP pap,
                  Map<String, V> constants,
                  Map<String, V> variables,
                  Map<String, F> operations,
                  Scope<V, F> parentScope) {
        this.pap = pap;
        this.constants = constants;
        this.variables = variables;
        this.operations = operations;
        this.parentScope = parentScope;
    }

    public abstract Scope<V, F> copy();
    public abstract Scope<V, F> copyBasicFunctionsOnly();
    public abstract Scope<V, F> copyBasicAndQueryFunctionsOnly();

    public PAP getPap() {
        return pap;
    }

    public void setPap(PAP pap) {
        this.pap = pap;
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

    public Map<String, F> getOperations() {
        return operations;
    }

    public void setOperations(Map<String, F> operations) {
        this.operations = operations;
    }

    public Scope<V, F> getParentScope() {
        return parentScope;
    }

    public void setParentScope(Scope<V, F> parentScope) {
        this.parentScope = parentScope;
    }

    public F getOperation(String name) throws UnknownOperationInScopeException {
        F function = operations.get(name);
        if (function == null) {
            throw new UnknownOperationInScopeException(name);
        }

        return function;
    }

    public boolean functionExists(String name) {
        return operations.containsKey(name);
    }

    public void addOperation(String name, F f) throws OperationAlreadyDefinedInScopeException {
        if (parentHasFunction(name) || operations.containsKey(name)) {
            throw new OperationAlreadyDefinedInScopeException(name);
        }

        operations.put(name, f);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Scope<?, ?> scope)) return false;
        return Objects.equals(constants, scope.constants) && Objects.equals(
            variables,
            scope.variables
        ) && Objects.equals(operations, scope.operations) && Objects.equals(parentScope, scope.parentScope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constants, variables, operations, parentScope);
    }

    private boolean parentHasVariable(String name) {
        return parentScope != null && parentScope.variables.containsKey(name);
    }

    private boolean parentHasFunction(String name) {
        return parentScope != null && parentScope.operations.containsKey(name);
    }


}
