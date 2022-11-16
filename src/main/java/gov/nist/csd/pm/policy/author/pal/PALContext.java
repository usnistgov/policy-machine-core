package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.policy.author.PALAuthor;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;

import java.util.HashMap;
import java.util.Map;

public class PALContext extends PALAuthor {

    private final Map<String, FunctionDefinitionStatement> functions;

    private final Map<String, Value> constants;

    public PALContext() {
        functions = new HashMap<>();
        constants = new HashMap<>();
    }

    public PALContext(Map<String, FunctionDefinitionStatement> functions, Map<String, Value> constants) {
        this.functions = functions;
        this.constants = constants;
    }

    @Override
    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) {
        this.functions.put(functionDefinitionStatement.getFunctionName(), functionDefinitionStatement);
    }

    @Override
    public void removeFunction(String functionName) {
        this.functions.remove(functionName);
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() {
        return functions;
    }

    @Override
    public void addConstant(String constantName, Value constantValue) {
        this.constants.put(constantName, constantValue);
    }

    @Override
    public void removeConstant(String constName) {
        this.constants.remove(constName);
    }

    @Override
    public Map<String, Value> getConstants() {
        return constants;
    }

    @Override
    public PALContext getContext() {
        return this;
    }

}
