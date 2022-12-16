package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.store.PolicyStore;
import gov.nist.csd.pm.policy.author.PALAuthor;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.ConstantAlreadyDefinedException;
import gov.nist.csd.pm.policy.exceptions.FunctionAlreadyDefinedException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class PAL extends PALAuthor implements PolicyEventEmitter {

    private final PolicyStore store;
    private final List<PolicyEventListener> listeners;

    public PAL(PolicyStore store) {
        this.store = store;
        this.listeners = new ArrayList<>();

        // add pre defined functions and constants
    }

    @Override
    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
        if (getFunctions().containsKey(functionDefinitionStatement.getFunctionName())) {
            throw new FunctionAlreadyDefinedException(functionDefinitionStatement.getFunctionName());
        }

        this.store.pal().addFunction(functionDefinitionStatement);

        emitEvent(new AddFunctionEvent(functionDefinitionStatement));
    }

    @Override
    public void removeFunction(String functionName) throws PMException {
        this.store.pal().removeFunction(functionName);

        emitEvent(new RemoveFunctionEvent(functionName));
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() throws PMException {
        return this.store.pal().getFunctions();
    }

    @Override
    public void addConstant(String constantName, Value constantValue) throws PMException {
        if (getConstants().containsKey(constantName)) {
            throw new ConstantAlreadyDefinedException(constantName);
        }

        this.store.pal().addConstant(constantName, constantValue);

        emitEvent(new AddConstantEvent(constantName, constantValue));
    }

    @Override
    public void removeConstant(String constName) throws PMException {
        this.store.pal().removeConstant(constName);

        emitEvent(new RemoveConstantEvent(constName));
    }

    @Override
    public Map<String, Value> getConstants() throws PMException {
        return this.store.pal().getConstants();
    }

    @Override
    public PALContext getContext() throws PMException {
        return this.store.pal().getContext();
    }


    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) {
        listeners.add(listener);
    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        for (PolicyEventListener listener : listeners) {
            listener.handlePolicyEvent(event);
        }
    }
}
