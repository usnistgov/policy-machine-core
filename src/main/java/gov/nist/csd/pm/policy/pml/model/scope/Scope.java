package gov.nist.csd.pm.policy.pml.model.scope;

import gov.nist.csd.pm.policy.exceptions.PMLFunctionNotDefinedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.pml.PMLBuiltinFunctions;
import gov.nist.csd.pm.policy.pml.PMLContext;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.function.FunctionSignature;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Scope implements Serializable {

    /**
     * Store a set of function definition statements.
     * These will include any builtin functions such as concat().
     */
    private Map<String, FunctionDefinitionStatement> functions;
    private Map<String, FunctionSignature> functionSignatures;

    /**
     * The variables accessible in the scope.
     * Only used if mode is COMPILE.
     */
    private Map<String, Variable> variables;

    /**
     * The values accessible in the scope.
     * Only used if mode is EXECUTE.
     */
    private final Map<String, Value> values;

    /**
     * The resources access rights expression for the policy. This is used during compilation.
     */
    private Expression resourceAccessRightsExpression;

    /**
     * The resources access rights for the policy. This is used during execution.
     */
    private AccessRightSet resourceAccessRights;

    private final Mode mode;

    public Scope(Mode mode) {
        this.mode = mode;
        this.functions = new HashMap<>();
        this.functionSignatures = new HashMap<>();
        this.variables = new HashMap<>();
        this.values = new HashMap<>();
        this.resourceAccessRightsExpression = null;
        this.resourceAccessRights = new AccessRightSet();
    }

    public Map<String, FunctionDefinitionStatement> functions() {
        return functions;
    }

    public Map<String, Variable> variables() {
        return variables;
    }

    public Map<String, Value> values() {
        return values;
    }

    public Expression getResourceAccessRightsExpression() {
        return resourceAccessRightsExpression;
    }

    public boolean isResourceAccessRightsExpressionSet() {
        return resourceAccessRightsExpression != null;
    }

    public Scope copy() {
        Scope copy = new Scope(this.mode);

        copy.functions = new HashMap<>(this.functions);
        copy.functionSignatures = new HashMap<>(this.functionSignatures);
        copy.variables = new HashMap<>(this.variables);

        if (isResourceAccessRightsExpressionSet()) {
            copy.resourceAccessRightsExpression = this.resourceAccessRightsExpression;
        }

        copy.resourceAccessRights = this.resourceAccessRights;

        return copy;
    }

    public void overwriteVariables(Scope overwrite) {
        for (String varName : overwrite.variables.keySet()) {
            if (!this.variables.containsKey(varName)) {
                continue;
            }

            this.variables.put(varName, overwrite.variables.get(varName));
        }
    }

    public void overwriteValues(Scope overwrite) {
        for (String varName : overwrite.values.keySet()) {
            if (!this.values.containsKey(varName)) {
                continue;
            }

            this.values.put(varName, overwrite.values.get(varName));
        }
    }

    public void loadFromPMLContext(PMLContext pmlCtx) {
        functions.putAll(pmlCtx.getFunctions());

        for (FunctionDefinitionStatement func : pmlCtx.getFunctions().values()) {
            functionSignatures.put(func.signature().getFunctionName(), func.signature());
        }

        values.putAll(pmlCtx.getConstants());

        // if the mode is COMPILE add the constants as variables which will be used during the compilation of
        // statements
        if (mode == Mode.COMPILE) {
            Map<String, Value> constants = pmlCtx.getConstants();
            for (Map.Entry<String, Value> constant : constants.entrySet()) {
                String id = constant.getKey();
                Type type = constant.getValue().getType();

                variables.put(id, new Variable(id, type, true));
            }
        }
    }

    public void setResourceAccessRightsExpression(Expression expression) {
        this.resourceAccessRightsExpression = expression;
    }

    public void setResourceAccessRights(AccessRightSet accessRightSet) {
        this.resourceAccessRights = accessRightSet;
    }

    public void addFunctionSignature(FunctionSignature functionSignature) throws FunctionAlreadyDefinedInScopeException {
        if (functionSignatures.containsKey(functionSignature.getFunctionName())
                || isBuiltinFunction(functionSignature.getFunctionName())) {
            throw new FunctionAlreadyDefinedInScopeException(functionSignature.getFunctionName());
        }

        this.functionSignatures.put(functionSignature.getFunctionName(), functionSignature);
    }

    public FunctionSignature getFunctionSignature(String name) throws UnknownFunctionInScopeException {
        if (functionSignatures.containsKey(name)) {
            return functionSignatures.get(name);
        } else if (isBuiltinFunction(name)) {
            return PMLBuiltinFunctions.builtinFunctions().get(name).signature();
        }

        throw new UnknownFunctionInScopeException(name);
    }

    public void removeFunctionSignature(String name) {
        functionSignatures.remove(name);
    }

    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) throws FunctionAlreadyDefinedInScopeException {
        if (functions.containsKey(functionDefinitionStatement.signature().getFunctionName())
                || isBuiltinFunction(functionDefinitionStatement.signature().getFunctionName())) {
            throw new FunctionAlreadyDefinedInScopeException(functionDefinitionStatement.signature().getFunctionName());
        }

        this.functions.put(functionDefinitionStatement.signature().getFunctionName(), functionDefinitionStatement);
    }

    public FunctionDefinitionStatement getFunction(String name) throws UnknownFunctionInScopeException {
        if (functions.containsKey(name)) {
            return functions.get(name);
        } else if (isBuiltinFunction(name)) {
            return PMLBuiltinFunctions.builtinFunctions().get(name);
        }

        throw new UnknownFunctionInScopeException(name);
    }

    public boolean functionExists(String name) {
        try {
            getFunction(name);
            return true;
        } catch (UnknownFunctionInScopeException e) {
            return false;
        }
    }

    public void removeFunction(String name) {
        functions.remove(name);
    }

    public void addVariable(String name, Type type, boolean isConst) throws VariableAlreadyDefinedInScopeException {
        if (variableExists(name)) {
            throw new VariableAlreadyDefinedInScopeException(name);
        }

        this.variables.put(name, new Variable(name, type, isConst));
    }

    public void addOrOverwriteVariable(String name, Type type) {
        this.variables.put(name, new Variable(name, type, false));
    }

    private boolean constantExists(String name) {
        return variables.containsKey(name) && variables.get(name).isConst();
    }

    public Variable getVariable(String name) throws UnknownVariableInScopeException {
        if (variables.containsKey(name)) {
            return variables.get(name);
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

    public void addValue(String name, Value value) throws VariableAlreadyDefinedInScopeException {
        if (this.values.containsKey(name)) {
            throw new VariableAlreadyDefinedInScopeException(name);
        }

        this.values.put(name, value);
    }

    public void addOrOverwriteValue(String name, Value value) {
        this.values.put(name, value);
    }

    public void updateValue(String name, Value value) throws UnknownVariableInScopeException {
        if (!this.values.containsKey(name)) {
            throw new UnknownVariableInScopeException(name);
        }

        this.values.put(name, value);
    }

    public boolean valueExists(String id) {
        return values.containsKey(id);
    }

    public Value getValue(String name) throws UnknownVariableInScopeException {
        if (values.containsKey(name)) {
           return values.get(name);
        }else if (resourceAccessRights.contains(name)) {
            return new StringValue(name);
        }

        throw new UnknownVariableInScopeException(name);
    }

    private boolean isBuiltinFunction(String name) {
        return PMLBuiltinFunctions.isBuiltinFunction(name);
    }

    public enum Mode {
        COMPILE,
        EXECUTE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scope scope = (Scope) o;
        return Objects.equals(functions, scope.functions)
                && Objects.equals(variables, scope.variables)
                && Objects.equals(values, scope.values)
                && Objects.equals(resourceAccessRights, scope.resourceAccessRights)
                && mode == scope.mode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(functions, variables, values, resourceAccessRights, mode);
    }
}
