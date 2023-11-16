package gov.nist.csd.pm.policy.pml.scope;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AdminAccessRights;
import gov.nist.csd.pm.policy.pml.PMLBuiltinFunctions;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static gov.nist.csd.pm.pap.AdminPolicyNode.*;
import static gov.nist.csd.pm.pap.AdminPolicyNode.OBLIGATIONS_TARGET;

public class GlobalScope<V, F> implements Serializable {

    public static GlobalScope<Variable, FunctionSignature> withVariablesAndSignatures(Policy policy, FunctionDefinitionStatement ... customFunctionStatements)
            throws PMException {
        // buitin variables
        Map<String, Variable> builtinConstants = new HashMap<>();
        for (String ar : AdminAccessRights.allAdminAccessRights()) {
            builtinConstants.put(ar, new Variable(ar, Type.string(), true));
        }

        // admin policy nodes constants
        builtinConstants.put(ADMIN_POLICY.constantName(), new Variable(ADMIN_POLICY.constantName(), Type.string(), true));
        builtinConstants.put(POLICY_CLASS_TARGETS.constantName(), new Variable(POLICY_CLASS_TARGETS.constantName(), Type.string(), true));
        builtinConstants.put(ADMIN_POLICY_TARGET.constantName(), new Variable(ADMIN_POLICY_TARGET.constantName(), Type.string(), true));
        builtinConstants.put(PML_FUNCTIONS_TARGET.constantName(), new Variable(PML_FUNCTIONS_TARGET.constantName(), Type.string(), true));
        builtinConstants.put(PML_CONSTANTS_TARGET.constantName(), new Variable(PML_CONSTANTS_TARGET.constantName(), Type.string(), true));
        builtinConstants.put(PROHIBITIONS_TARGET.constantName(), new Variable(PROHIBITIONS_TARGET.constantName(), Type.string(), true));
        builtinConstants.put(OBLIGATIONS_TARGET.constantName(), new Variable(OBLIGATIONS_TARGET.constantName(), Type.string(), true));

        Map<String, Value> constants = policy.userDefinedPML().getConstants();
        Map<String, Variable> persistedConstants = new HashMap<>();
        for (Map.Entry<String, Value> e : constants.entrySet()) {
            String varName = e.getKey();
            Value value = e.getValue();
            persistedConstants.put(varName, new Variable(varName, value.getType(), true));
        }

        Map<String, FunctionDefinitionStatement> functions = policy.userDefinedPML().getFunctions();
        Map<String, FunctionSignature> persistedFunctions = new HashMap<>();
        for (Map.Entry<String, FunctionDefinitionStatement> e : functions.entrySet()) {
            String varName = e.getKey();
            FunctionDefinitionStatement value = e.getValue();
            persistedFunctions.put(varName, value.getSignature());
        }

        // add built in functions
        Map<String, FunctionSignature> builtinFunctions = new HashMap<>();
        functions = PMLBuiltinFunctions.builtinFunctions();
        for (Map.Entry<String, FunctionDefinitionStatement> e : functions.entrySet()) {
            builtinFunctions.put(e.getKey(), e.getValue().getSignature());
        }

        Map<String, FunctionSignature> customFunctions = new HashMap<>();
        for (FunctionDefinitionStatement stmt : customFunctionStatements) {
            customFunctions.put(stmt.getSignature().getFunctionName(), stmt.getSignature());
        }

        return new GlobalScope<>(builtinConstants, persistedConstants, builtinFunctions, persistedFunctions, customFunctions);
    }

    public static GlobalScope<Value, FunctionDefinitionStatement> withValuesAndDefinitions(Policy policy, FunctionDefinitionStatement ... customFunctionStatements)
            throws PMException {
        // buitin variables
        Map<String, Value> builtinVariables = new HashMap<>();
        for (String ar : AdminAccessRights.allAdminAccessRights()) {
            builtinVariables.put(ar, new StringValue(ar));
        }

        builtinVariables.put(ADMIN_POLICY.constantName(), new StringValue(ADMIN_POLICY.nodeName()));
        builtinVariables.put(POLICY_CLASS_TARGETS.constantName(), new StringValue(POLICY_CLASS_TARGETS.nodeName()));
        builtinVariables.put(ADMIN_POLICY_TARGET.constantName(), new StringValue(ADMIN_POLICY_TARGET.nodeName()));
        builtinVariables.put(PML_FUNCTIONS_TARGET.constantName(), new StringValue(PML_FUNCTIONS_TARGET.nodeName()));
        builtinVariables.put(PML_CONSTANTS_TARGET.constantName(), new StringValue(PML_CONSTANTS_TARGET.nodeName()));
        builtinVariables.put(PROHIBITIONS_TARGET.constantName(), new StringValue(PROHIBITIONS_TARGET.nodeName()));
        builtinVariables.put(OBLIGATIONS_TARGET.constantName(), new StringValue(OBLIGATIONS_TARGET.nodeName()));

        Map<String, Value> persistedVariables = policy.userDefinedPML().getConstants();

        Map<String, FunctionDefinitionStatement> persistedFunctions = policy.userDefinedPML().getFunctions();

        // add built in functions
        Map<String, FunctionDefinitionStatement> builtinFunctions = PMLBuiltinFunctions.builtinFunctions();

        // add custom functions
        Map<String, FunctionDefinitionStatement> customFunctions = new HashMap<>();
        for (FunctionDefinitionStatement stmt : customFunctionStatements) {
            customFunctions.put(stmt.getSignature().getFunctionName(), stmt);
        }

        return new GlobalScope<>(builtinVariables, persistedVariables, builtinFunctions, persistedFunctions, customFunctions);
    }

    private Map<String, V> builtinConstants;
    private Map<String, V> persistedConstants;
    private Map<String, F> builtinFunctions;
    private Map<String, F> persistedFunctions;
    private Map<String, F> customFunctions;

    private GlobalScope() {
        builtinConstants = new HashMap<>();
        persistedConstants = new HashMap<>();
        builtinFunctions = new HashMap<>();
        persistedFunctions = new HashMap<>();
        customFunctions = new HashMap<>();
    }

    private GlobalScope(Map<String, V> builtinConstants, Map<String, V> persistedConstants,
                       Map<String, F> builtinFunctions,
                       Map<String, F> persistedFunctions, Map<String, F> customFunctions) {
        this.builtinConstants = builtinConstants;
        this.persistedConstants = persistedConstants;
        this.builtinFunctions = builtinFunctions;
        this.persistedFunctions = persistedFunctions;
        this.customFunctions = customFunctions;
    }

    public GlobalScope<V, F> withPersistedFunctions(Map<String, F> persistedFunctions) {
        this.persistedFunctions = persistedFunctions;
        return this;
    }

    public GlobalScope<V, F> withPersistedConstants(Map<String, V> persistedConstants) {
        this.persistedConstants = persistedConstants;
        return this;
    }

    public GlobalScope<V, F> withCustomFunctions(Map<String, F> customFunctions) {
        this.customFunctions = customFunctions;
        return this;
    }

    public V getConstant(String varName) {
        if (builtinConstants.containsKey(varName)) {
            return builtinConstants.get(varName);
        } else if (persistedConstants.containsKey(varName)) {
            return persistedConstants.get(varName);
        }

        return null;
    }

    public F getFunction(String funcName) {
        if (builtinFunctions.containsKey(funcName)) {
            return builtinFunctions.get(funcName);
        } else if (persistedFunctions.containsKey(funcName)) {
            return persistedFunctions.get(funcName);
        } else if (customFunctions.containsKey(funcName)) {
            return customFunctions.get(funcName);
        }

        return null;
    }

    public Map<String, V> getBuiltinConstants() {
        return builtinConstants;
    }

    public void setBuiltinConstants(Map<String, V> builtinConstants) {
        this.builtinConstants = builtinConstants;
    }

    public Map<String, V> getPersistedConstants() {
        return persistedConstants;
    }

    public void setPersistedConstants(Map<String, V> persistedConstants) {
        this.persistedConstants = persistedConstants;
    }

    public Map<String, F> getBuiltinFunctions() {
        return builtinFunctions;
    }

    public void setBuiltinFunctions(Map<String, F> builtinFunctions) {
        this.builtinFunctions = builtinFunctions;
    }

    public Map<String, F> getPersistedFunctions() {
        return persistedFunctions;
    }

    public void setPersistedFunctions(Map<String, F> persistedFunctions) {
        this.persistedFunctions = persistedFunctions;
    }

    public Map<String, F> getCustomFunctions() {
        return customFunctions;
    }

    public void setCustomFunctions(Map<String, F> customFunctions) {
        this.customFunctions = customFunctions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GlobalScope<?, ?> that = (GlobalScope<?, ?>) o;
        return Objects.equals(builtinConstants, that.builtinConstants) && Objects.equals(
                persistedConstants, that.persistedConstants) && Objects.equals(
                builtinFunctions, that.builtinFunctions) && Objects.equals(
                persistedFunctions, that.persistedFunctions) && Objects.equals(
                customFunctions, that.customFunctions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(builtinConstants, persistedConstants, builtinFunctions, persistedFunctions, customFunctions);
    }
}
