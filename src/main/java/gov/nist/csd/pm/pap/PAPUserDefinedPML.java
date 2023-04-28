package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.UserDefinedPML;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.ConstantAlreadyDefinedException;
import gov.nist.csd.pm.policy.exceptions.FunctionAlreadyDefinedException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class PAPUserDefinedPML implements UserDefinedPML, PolicyEventEmitter {
    protected PolicyStore policyStore;

    protected PolicyEventListener listener;

    public PAPUserDefinedPML(PolicyStore policyStore, PolicyEventListener listener) throws PMException {
        this.policyStore = policyStore;
        this.listener = listener;
    }

    @Override
    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
        if (policyStore.userDefinedPML().getFunctions().containsKey(functionDefinitionStatement.getFunctionName())) {
            throw new FunctionAlreadyDefinedException(functionDefinitionStatement.getFunctionName());
        }

        policyStore.userDefinedPML().addFunction(functionDefinitionStatement);

        emitEvent(new AddFunctionEvent(functionDefinitionStatement));
    }

    @Override
    public void removeFunction(String functionName) throws PMException {
        policyStore.userDefinedPML().removeFunction(functionName);

        emitEvent(new RemoveFunctionEvent(functionName));
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() throws PMException {
        return policyStore.userDefinedPML().getFunctions();
    }

    @Override
    public FunctionDefinitionStatement getFunction(String name) throws PMException {
        return policyStore.userDefinedPML().getFunction(name);
    }

    @Override
    public void addConstant(String constantName, Value constantValue) throws PMException {
        if (policyStore.userDefinedPML().getConstants().containsKey(constantName)) {
            throw new ConstantAlreadyDefinedException(constantName);
        }

        policyStore.userDefinedPML().addConstant(constantName, constantValue);

        emitEvent(new AddConstantEvent(constantName, constantValue));
    }

    @Override
    public void removeConstant(String constName) throws PMException {
        policyStore.userDefinedPML().removeConstant(constName);

        emitEvent(new RemoveConstantEvent(constName));
    }

    @Override
    public Map<String, Value> getConstants() throws PMException {
        return policyStore.userDefinedPML().getConstants();
    }

    @Override
    public Value getConstant(String name) throws PMException {
        return policyStore.userDefinedPML().getConstant(name);
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {

    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {

    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        listener.handlePolicyEvent(event);
    }
}
