package gov.nist.csd.pm.policy.author.pal.model.context;

import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ExecutionContext implements Serializable {

    private final UserContext author;
    private final Map<String, FunctionDefinitionStatement> functions;
    private final Map<String, Value> variables;
    private final Map<String, Value> constants;
    private AccessRightSet resourceAccessRights;

    public ExecutionContext(UserContext author) {
        this.author = author;
        this.functions = new HashMap<>();
        this.variables = new HashMap<>();
        this.constants = new HashMap<>();
        this.resourceAccessRights = new AccessRightSet();
    }

    public ExecutionContext copy() {
        ExecutionContext copy = new ExecutionContext(this.author);

        for (String funcName : this.getFunctions().keySet()) {
            copy.addFunction(this.getFunction(funcName));
        }

        for (String varName : this.getVariables().keySet()) {
            copy.addVariable(varName, this.getVariable(varName), false);
        }

        for (String varName : this.getConstants().keySet()) {
            copy.addVariable(varName, this.getVariable(varName), true);
        }

        copy.setResourceAccessRights(resourceAccessRights);

        return copy;
    }

    public void updateVariables(ExecutionContext executionCtx) {
        for (String varName : executionCtx.variables.keySet()) {
            if (!this.variables.containsKey(varName)) {
                continue;
            }

            this.variables.put(varName, executionCtx.getVariable(varName));
        }
    }

    public UserContext getAuthor() {
        return author;
    }

    public Map<String, FunctionDefinitionStatement> getFunctions() {
        return functions;
    }

    public Map<String, Value> getVariables() {
        return variables;
    }

    public Map<String, Value> getConstants() {
        return constants;
    }

    public void addFunction(FunctionDefinitionStatement functionDefinitionStmt) {
        this.functions.put(functionDefinitionStmt.getFunctionName(), functionDefinitionStmt);
    }

    public void addVariable(String varName, Value value, boolean isConst) {
        if (isConst) {
            this.constants.put(varName, value);
        } else {
            this.variables.put(varName, value);
        }
    }

    public void setResourceAccessRights(AccessRightSet resourceAccessRights) {
        this.resourceAccessRights = resourceAccessRights;
    }

    public AccessRightSet getResourceAccessRights() {
        return resourceAccessRights;
    }

    public FunctionDefinitionStatement getFunction(String name) {
        return functions.get(name);
    }

    public Value getVariable(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        } else {
            return constants.get(name);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionContext that = (ExecutionContext) o;
        return Objects.equals(author, that.author) && Objects.equals(functions, that.functions) && Objects.equals(variables, that.variables) && Objects.equals(constants, that.constants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, functions, variables, constants);
    }
}
