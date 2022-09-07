package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.PALStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.TransactionNotStartedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class MemoryPALStore extends PALStore {

    private PALContext palCtx;
    private TxHandler<PALContext> txHandler;

    MemoryPALStore() {
        this.palCtx = new PALContext();
        this.txHandler = new TxHandler<>();
    }

    MemoryPALStore(PALContext palCtx) {
        this.palCtx = copyPALContext(palCtx);
        this.txHandler = new TxHandler<>();
    }

    private PALContext copyPALContext(PALContext palContext) {
        PALContext copy = new PALContext();
        for (String functionName : palContext.getFunctions().keySet()) {
            FunctionDefinitionStatement functionDefinitionStatement = palContext.getFunctions().get(functionName);
            copy.addFunction(functionDefinitionStatement);
        }

        for (String constName : palContext.getConstants().keySet()) {
            copy.addConstant(constName, palContext.getConstants().get(constName));
        }

        return copy;
    }

    @Override
    public synchronized void addFunction(FunctionDefinitionStatement functionDefinitionStatement) {
        if (functionDefinitionStatement.isFunctionExecutor()) {
            palCtx.addFunction(new FunctionDefinitionStatement(
                    functionDefinitionStatement.getFunctionName(),
                    functionDefinitionStatement.getReturnType(),
                    new ArrayList<>(functionDefinitionStatement.getArgs()),
                    functionDefinitionStatement.getFunctionExecutor()
            ));
        } else {
            palCtx.addFunction(new FunctionDefinitionStatement(
                    functionDefinitionStatement.getFunctionName(),
                    functionDefinitionStatement.getReturnType(),
                    new ArrayList<>(functionDefinitionStatement.getArgs()),
                    new ArrayList<>(functionDefinitionStatement.getBody())
            ));
        }
    }

    @Override
    public synchronized void removeFunction(String functionName) {
        palCtx.removeFunction(functionName);
    }

    @Override
    public synchronized Map<String, FunctionDefinitionStatement> getFunctions() {
        return new HashMap<>(palCtx.getFunctions());
    }

    @Override
    public synchronized void addConstant(String constantName, Value constantValue) {
        palCtx.addConstant(constantName, constantValue);
    }

    @Override
    public synchronized void removeConstant(String constName) {
        palCtx.removeConstant(constName);
    }

    @Override
    public synchronized Map<String, Value> getConstants() {
        return new HashMap<>(palCtx.getConstants());
    }

    @Override
    public synchronized PALContext getContext() {
        return palCtx;
    }

    @Override
    public synchronized void beginTx() throws PMException {
        if (!txHandler.isInTx()) {
            txHandler.setState(copyPALContext(palCtx));
        }

        txHandler.beginTx();
    }

    @Override
    public synchronized void commit() throws PMException {
        if (!txHandler.isInTx()) {
            throw new TransactionNotStartedException();
        }

        txHandler.commit();
    }

    @Override
    public synchronized void rollback() throws TransactionNotStartedException {
        if (!txHandler.isInTx()) {
            return;
        }

        palCtx = txHandler.getState();
        txHandler.rollback();
    }


}
