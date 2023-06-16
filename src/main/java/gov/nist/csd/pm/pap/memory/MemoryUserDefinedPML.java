package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.UserDefinedPML;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.io.Serializable;
import java.util.HashMap;
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
    public void createFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().userDefinedPML().createFunction(functionDefinitionStatement);
        }

        functions.put(functionDefinitionStatement.getFunctionName(), functionDefinitionStatement);
    }

    @Override
    public void deleteFunction(String functionName) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().userDefinedPML().deleteFunction(functionName);
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
    public void createConstant(String constantName, Value constantValue) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().userDefinedPML().createConstant(constantName, constantValue);
        }

        constants.put(constantName, constantValue);
    }

    @Override
    public void deleteConstant(String constName) throws PMException {
        if (tx.isActive()) {
            tx.getPolicyStore().userDefinedPML().deleteConstant(constName);
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
