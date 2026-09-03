/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.scope;


import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A lexical scope of PML variables and operations, with an optional parent scope for outer lookups.
 *
 * @param <V> the variable value type
 * @param <F> the operation type
 */
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

    /**
     * Returns a copy of this scope, linked as a child of it.
     */
    public abstract Scope<V, F> copy();

    /**
     * Returns a copy of this scope containing only its function operations.
     */
    public abstract Scope<V, F> copyFunctionsOnly();

    /**
     * Returns a copy of this scope containing only its function and query operations.
     */
    public abstract Scope<V, F> copyFunctionsAndQueriesOnly();

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

    /**
     * Looks up an operation by name in this scope only (not the parent chain).
     *
     * @param name the operation name
     * @return the operation
     * @throws UnknownOperationInScopeException if no operation with that name is in this scope
     */
    public F getOperation(String name) throws UnknownOperationInScopeException {
        F operation = operations.get(name);
        if (operation == null) {
            throw new UnknownOperationInScopeException(name);
        }

        return operation;
    }

    /**
     * Checks whether an operation with the given name exists in this scope only (not the parent chain).
     */
    public boolean operationExists(String name) {
        return operations.containsKey(name);
    }

    /**
     * Registers an operation in this scope.
     *
     * @param name the operation name
     * @param f the operation
     * @throws OperationAlreadyDefinedInScopeException if an operation with that name already exists in
     * this scope or an ancestor scope
     */
    public void addOperation(String name, F f) throws OperationAlreadyDefinedInScopeException {
        if (parentHasOperation(name) || operations.containsKey(name)) {
            throw new OperationAlreadyDefinedInScopeException(name);
        }

        operations.put(name, f);
    }

    /**
     * Looks up a variable by name in this scope's constants, then its variables (not the parent chain).
     *
     * @param name the variable name
     * @return the variable value
     * @throws UnknownVariableInScopeException if no constant or variable with that name is in this scope
     */
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

    /**
     * Checks whether a variable with the given name exists in this scope's variables — unlike
     * {@link #getVariable}, this does not check constants.
     */
    public boolean variableExists(String name) {
        return variables.containsKey(name);
    }

    /**
     * Declares a new variable in this scope.
     *
     * @param name the variable name
     * @param v the initial value
     * @throws VariableAlreadyDefinedInScopeException if a variable with that name already exists in this
     * scope or an ancestor scope
     */
    public void addVariable(String name, V v) throws VariableAlreadyDefinedInScopeException {
        if (parentHasVariable(name) || variables.containsKey(name)) {
            throw new VariableAlreadyDefinedInScopeException(name);
        }

        variables.put(name, v);
    }

    /**
     * Assigns a new value to an already-declared variable in this scope, or declares it if it doesn't
     * already exist here.
     */
    public void updateVariable(String name, V value) {
        variables.put(name, value);
    }

    /**
     * Copies each variable's value from the given scope into this scope, but only for variables that
     * already exist here.
     */
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

    private boolean parentHasOperation(String name) {
        return parentScope != null && parentScope.operations.containsKey(name);
    }


}
