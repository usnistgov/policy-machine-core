package gov.nist.csd.pm.pap.memory;

import com.google.gson.Gson;
import gov.nist.csd.pm.policy.UserDefinedPML;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MemoryUserDefinedPML implements UserDefinedPML, Serializable {

    protected MemoryTx tx;
    private Map<String, FunctionDefinitionStatement> functions;
    private Map<String, Value> constants;

    public MemoryUserDefinedPML() {
        this.functions = new HashMap<>();
        this.constants = new HashMap<>();
        this.tx = new MemoryTx(false, 0, null);
    }

    public MemoryUserDefinedPML(Map<String, FunctionDefinitionStatement> functions, Map<String, Value> constants) {
        this.functions = functions;
        this.constants = constants;
        this.tx = new MemoryTx(false, 0, null);
    }

    public MemoryUserDefinedPML(UserDefinedPML userDefinedPML) throws PMException {
        this.functions = userDefinedPML.getFunctions();
        this.constants = userDefinedPML.getConstants();
        this.tx = new MemoryTx(false, 0, null);
    }

    @Override
    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) {
        if (tx.active()) {
            tx.policyStore().addFunction(functionDefinitionStatement);
        }

        functions.put(functionDefinitionStatement.getFunctionName(), functionDefinitionStatement);
    }

    @Override
    public void removeFunction(String functionName) throws PMException {
        if (tx.active()) {
            tx.policyStore().removeFunction(functionName);
        }

        functions.remove(functionName);
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() {
        return new HashMap<>(functions);
    }

    @Override
    public FunctionDefinitionStatement getFunction(String name) throws PMException {
        return getFunctions().get(name);
    }

    @Override
    public void addConstant(String constantName, Value constantValue) {
        if (tx.active()) {
            tx.policyStore().addConstant(constantName, constantValue);
        }

        constants.put(constantName, constantValue);
    }

    @Override
    public void removeConstant(String constName) throws PMException {
        if (tx.active()) {
            tx.policyStore().removeConstant(constName);
        }

        constants.remove(constName);
    }

    @Override
    public Map<String, Value> getConstants() {
        return new HashMap<>(constants);
    }

    @Override
    public Value getConstant(String name) {
        return getConstants().get(name);
    }

}
