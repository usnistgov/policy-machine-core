package gov.nist.csd.pm.policy.author.pal.model.scope;

import gov.nist.csd.pm.policy.author.pal.PALBuiltinConstants;
import gov.nist.csd.pm.policy.author.pal.PALBuiltinFunctions;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.compiler.Variable;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;

import java.io.Serializable;
import java.util.*;

public class Scope implements Serializable {

    /**
     * Store a set of function definition statements.
     * These will include any builtin functions such as concat().
     */
    private Map<String, FunctionDefinitionStatement> functions;

    /**
     * The variables accessible in the scope.
     *
     * Only used if mode is COMPILE
     */
    private Map<String, Variable> variables;

    /**
     * The values accessible in the scope.
     *
     * Only used if mode is EXECUTE
     */
    private final Map<String, Value> values;

    /**
     * The resources access rights expression for the policy. This is used during compilation.
     */
    private List<Expression> resourceAccessRightsExpression;

    /**
     * The resources access rights for the policy. This is used during execution.
     */
    private AccessRightSet resourceAccessRights;

    private final Mode mode;

    public Scope(Mode mode) {
        this.mode = mode;
        this.functions = new HashMap<>();
        this.variables = new HashMap<>();
        this.values = new HashMap<>();
        this.resourceAccessRightsExpression = new ArrayList<>();
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

    public List<Expression> getResourceAccessRightsExpression() {
        return resourceAccessRightsExpression;
    }

    public boolean isResourceAccessRightsExpressionSet() {
        return !resourceAccessRightsExpression.isEmpty();
    }

    public Scope copy() {
        Scope copy = new Scope(this.mode);

        copy.functions = new HashMap<>(this.functions);
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

    public void loadFromPALContext(PALContext palCtx) {
        functions.putAll(palCtx.getFunctions());
        values.putAll(palCtx.getConstants());
    }

    public void setResourceAccessRightsExpression(List<Expression> expression) {
        this.resourceAccessRightsExpression = expression;
    }

    public void setResourceAccessRights(AccessRightSet accessRightSet) {
        this.resourceAccessRights = accessRightSet;
    }

    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) throws FunctionAlreadyDefinedInScopeException {
        if (functions.containsKey(functionDefinitionStatement.getFunctionName())
                || isBuiltinFunction(functionDefinitionStatement.getFunctionName())) {
            throw new FunctionAlreadyDefinedInScopeException(functionDefinitionStatement.getFunctionName());
        }

        this.functions.put(functionDefinitionStatement.getFunctionName(), functionDefinitionStatement);
    }

    public FunctionDefinitionStatement getFunction(String name) throws UnknownFunctionInScopeException {
        if (functions.containsKey(name)) {
            return functions.get(name);
        } else if (isBuiltinFunction(name)) {
            return PALBuiltinFunctions.builtinFunctions().get(name);
        }

        throw new UnknownFunctionInScopeException(name);
    }

    public void addVariable(String name, Type type, boolean isConst) throws VariableAlreadyDefinedInScopeException {
        if (constantExists(name)
                || isBuiltinVariable(name)) {
            throw new VariableAlreadyDefinedInScopeException(name);
        }

        this.variables.put(name, new Variable(name, type, isConst));
    }

    private boolean constantExists(String name) {
        return variables.containsKey(name) && variables.get(name).isConst();
    }

    public Variable getVariable(String name) throws UnknownVariableInScopeException {
        if (variables.containsKey(name)) {
            return variables.get(name);
        } else if (isBuiltinVariable(name)) {
            return PALBuiltinConstants.builtinVariables().get(name);
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

    public void addValue(String name, Value value) {
        this.values.put(name, value);
    }

    public Value getValue(String name) throws UnknownVariableInScopeException {
        if (values.containsKey(name)) {
            return values.get(name);
        } else if (isBuiltinValue(name)) {
            return PALBuiltinConstants.builtinValues().get(name);
        } else if (resourceAccessRights.contains(name)) {
            return new Value(name);
        }

        throw new UnknownVariableInScopeException(name);
    }

    private boolean isBuiltinVariable(String name) {
        return PALBuiltinConstants.builtinVariables().containsKey(name);
    }
    private boolean isBuiltinValue(String name) {
        return PALBuiltinConstants.builtinValues().containsKey(name);
    }

    private boolean isBuiltinFunction(String name) {
        return PALBuiltinFunctions.builtinFunctions().containsKey(name);
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
