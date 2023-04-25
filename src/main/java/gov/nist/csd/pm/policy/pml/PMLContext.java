package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.model.expression.Value;

import java.util.HashMap;
import java.util.Map;

public class PMLContext {

    public static PMLContext fromPolicy(Policy policy) throws PMException {
        Map<String, FunctionDefinitionStatement> functions = policy.userDefinedPML().getFunctions();
        Map<String, Value> constants = policy.userDefinedPML().getConstants();

        return new PMLContext(functions, constants);
    }

    private final Map<String, FunctionDefinitionStatement> functions;

    private final Map<String, Value> constants;

    public PMLContext() {
        functions = new HashMap<>();
        constants = new HashMap<>();
    }

    public PMLContext(Map<String, FunctionDefinitionStatement> functions, Map<String, Value> constants) {
        this.functions = functions;
        this.constants = constants;
    }

    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) {
        this.functions.put(functionDefinitionStatement.getFunctionName(), functionDefinitionStatement);
    }

    public void removeFunction(String functionName) {
        this.functions.remove(functionName);
    }

    public Map<String, FunctionDefinitionStatement> getFunctions() {
        return functions;
    }

    public void addConstant(String constantName, Value constantValue) {
        this.constants.put(constantName, constantValue);
    }

    public void removeConstant(String constName) {
        this.constants.remove(constName);
    }

    public Map<String, Value> getConstants() {
        return constants;
    }

    public PMLContext getContext() {
        return this;
    }

}
