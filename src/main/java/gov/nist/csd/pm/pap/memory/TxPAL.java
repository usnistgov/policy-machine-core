package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.PALStore;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.tx.TxPolicyEventListener;

import java.util.Map;

class TxPAL extends PALStore implements PolicyEventEmitter {

    private final PALStore store;
    private final TxPolicyEventListener txPolicyEventListener;

    public TxPAL(PALStore store, TxPolicyEventListener txPolicyEventListener) {
        this.store = store;
        this.txPolicyEventListener = txPolicyEventListener;
    }

    @Override
    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
        emitEvent(new AddFunctionEvent(functionDefinitionStatement));
        store.addFunction(functionDefinitionStatement);
    }

    @Override
    public void removeFunction(String functionName) throws PMException {
        emitEvent(new RemoveFunctionEvent(functionName));
        store.removeFunction(functionName);
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() throws PMException {
        return store.getFunctions();
    }

    @Override
    public void addConstant(String constantName, Value constantValue) throws PMException {
        emitEvent(new AddConstantEvent(constantName, constantValue));
        store.addConstant(constantName, constantValue);
    }

    @Override
    public void removeConstant(String constName) throws PMException {
        emitEvent(new RemoveConstantEvent(constName));
        store.removeConstant(constName);
    }

    @Override
    public Map<String, Value> getConstants() throws PMException {
        return store.getConstants();
    }

    @Override
    public PALContext getContext() throws PMException {
        return store.getContext();
    }

    @Override
    public void beginTx() throws PMException {

    }

    @Override
    public void commit() throws PMException {

    }

    @Override
    public void rollback() throws PMException {

    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {

    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {

    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        txPolicyEventListener.handlePolicyEvent(event);
    }
}
