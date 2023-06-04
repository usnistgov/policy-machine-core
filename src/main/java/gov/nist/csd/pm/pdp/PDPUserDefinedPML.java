package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.UserDefinedPMLAdjudicator;
import gov.nist.csd.pm.policy.UserDefinedPML;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.Map;

class PDPUserDefinedPML implements UserDefinedPML, PolicyEventEmitter {
    private UserContext userCtx;
    private UserDefinedPMLAdjudicator adjudicator;
    private PAP pap;
    private PolicyEventListener listener;

    public PDPUserDefinedPML(UserContext userCtx, UserDefinedPMLAdjudicator adjudicator, PAP pap, PolicyEventListener listener) {
        this.userCtx = userCtx;
        this.adjudicator = adjudicator;
        this.pap = pap;
        this.listener = listener;
    }

    @Override
    public void createFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
        adjudicator.createFunction(functionDefinitionStatement);

        pap.userDefinedPML().createFunction(functionDefinitionStatement);

        emitEvent(new EventContext(userCtx, new AddFunctionEvent(functionDefinitionStatement)));
    }

    @Override
    public void deleteFunction(String functionName) throws PMException {
        adjudicator.deleteFunction(functionName);

        pap.userDefinedPML().deleteFunction(functionName);

        emitEvent(new EventContext(userCtx, new RemoveFunctionEvent(functionName)));

    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() throws PMException {
        return pap.userDefinedPML().getFunctions();
    }

    @Override
    public FunctionDefinitionStatement getFunction(String name) throws PMException {
        return pap.userDefinedPML().getFunction(name);
    }

    @Override
    public void createConstant(String constantName, Value constantValue) throws PMException {
        adjudicator.createConstant(constantName, constantValue);

        pap.userDefinedPML().createConstant(constantName, constantValue);

        emitEvent(new EventContext(userCtx, new AddConstantEvent(constantName, constantValue)));

    }

    @Override
    public void deleteConstant(String constName) throws PMException {
        adjudicator.deleteConstant(constName);

        pap.userDefinedPML().deleteConstant(constName);

        emitEvent(new EventContext(userCtx, new RemoveConstantEvent(constName)));
    }

    @Override
    public Map<String, Value> getConstants() throws PMException {
        return pap.userDefinedPML().getConstants();
    }

    @Override
    public Value getConstant(String name) throws PMException {
        return pap.userDefinedPML().getConstant(name);
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {

    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {

    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        this.listener.handlePolicyEvent(event);
    }
}
