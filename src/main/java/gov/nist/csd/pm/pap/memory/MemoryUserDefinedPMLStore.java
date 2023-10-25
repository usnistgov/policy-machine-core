package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.UserDefinedPMLStore;
import gov.nist.csd.pm.policy.UserDefinedPML;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.HashMap;
import java.util.Map;

class MemoryUserDefinedPMLStore extends MemoryStore<TxUserDefinedPML> implements UserDefinedPMLStore, Transactional, BaseMemoryTx {

    private final Map<String, FunctionDefinitionStatement> functions;
    private final Map<String, Value> constants;
    private MemoryGraphStore graph;

    public MemoryUserDefinedPMLStore() {
        this.functions = new HashMap<>();
        this.constants = new HashMap<>();
    }

    public MemoryUserDefinedPMLStore(UserDefinedPML userDefinedPML) throws PMException {
        this.functions = userDefinedPML.getFunctions();
        this.constants = userDefinedPML.getConstants();
    }

    public void setMemoryGraph(MemoryGraphStore graph) {
        this.graph = graph;
    }

    public void clear() {
        this.functions.clear();
        this.constants.clear();
    }

    @Override
    public void beginTx() {
        if (tx == null) {
            tx = new MemoryTx<>(false, 0, new TxUserDefinedPML(new TxPolicyEventTracker(), this));
        }
        tx.beginTx();
    }

    @Override
    public void commit() {
        tx.commit();
    }

    @Override
    public void rollback() {
        tx.getStore().rollback();

        tx.rollback();
    }

    @Override
    public void createFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMLFunctionAlreadyDefinedException, PMBackendException {
        checkCreateFunctionInput(functionDefinitionStatement.getFunctionName());

        // log the command if in a tx
        handleTxIfActive(tx -> tx.createFunction(functionDefinitionStatement));

        functions.put(functionDefinitionStatement.getFunctionName(), new FunctionDefinitionStatement(functionDefinitionStatement));
    }

    @Override
    public void deleteFunction(String functionName) throws PMBackendException {
        if (!checkDeleteFunctionInput(functionName)) {
            return;
        }

        // log the command if in a tx
        handleTxIfActive(tx -> tx.deleteFunction(functionName));

        functions.remove(functionName);
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() {
        HashMap<String, FunctionDefinitionStatement> map = new HashMap<>();
        for (Map.Entry<String, FunctionDefinitionStatement> e : functions.entrySet()) {
            map.put(e.getKey(), new FunctionDefinitionStatement(e.getValue()));
        }

        return map;
    }

    @Override
    public FunctionDefinitionStatement getFunction(String name) throws PMLFunctionNotDefinedException {
        checkGetFunctionInput(name);

        return new FunctionDefinitionStatement(functions.get(name));
    }

    @Override
    public void createConstant(String constantName, Value constantValue) throws PMLConstantAlreadyDefinedException, PMBackendException {
        checkCreateConstantInput(constantName);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.createConstant(constantName, constantValue));

        constants.put(constantName, constantValue);
    }

    @Override
    public void deleteConstant(String constName) throws PMBackendException {
        if (!checkDeleteConstantInput(constName)) {
            return;
        }

        // log the command if in a tx
        handleTxIfActive(tx -> tx.deleteConstant(constName));

        constants.remove(constName);
    }

    @Override
    public Map<String, Value> getConstants() {
        return new HashMap<>(constants);
    }

    @Override
    public Value getConstant(String name) throws PMLConstantNotDefinedException {
        checkGetConstantInput(name);

        return constants.get(name);
    }

    @Override
    public void checkCreateFunctionInput(String name) throws PMLFunctionAlreadyDefinedException {
        if (functions.containsKey(name)) {
            throw new PMLFunctionAlreadyDefinedException(name);
        }
    }

    @Override
    public boolean checkDeleteFunctionInput(String name) {
        return functions.containsKey(name);
    }

    @Override
    public void checkGetFunctionInput(String name) throws PMLFunctionNotDefinedException {
        if (!functions.containsKey(name)) {
            throw new PMLFunctionNotDefinedException(name);
        }
    }

    @Override
    public void checkCreateConstantInput(String name) throws PMLConstantAlreadyDefinedException {
        if (constants.containsKey(name)) {
            throw new PMLConstantAlreadyDefinedException(name);
        }
    }

    @Override
    public boolean checkDeleteConstantInput(String name) {
        return constants.containsKey(name);
    }

    @Override
    public void checkGetConstantInput(String name) throws PMLConstantNotDefinedException {
        if (!constants.containsKey(name)) {
            throw new PMLConstantNotDefinedException(name);
        }
    }
}
