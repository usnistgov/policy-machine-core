package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.epp.EventProcessor;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.AdjudicatorUserDefinedPML;
import gov.nist.csd.pm.policy.UserDefinedPML;
import gov.nist.csd.pm.policy.events.userdefinedpml.CreateConstantEvent;
import gov.nist.csd.pm.policy.events.userdefinedpml.CreateFunctionEvent;
import gov.nist.csd.pm.policy.events.userdefinedpml.DeleteConstantEvent;
import gov.nist.csd.pm.policy.events.userdefinedpml.DeleteFunctionEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.Map;

class PDPUserDefinedPML implements UserDefinedPML, EventEmitter {
    private UserContext userCtx;
    private AdjudicatorUserDefinedPML adjudicator;
    private PAP pap;
    private EventProcessor listener;

    public PDPUserDefinedPML(UserContext userCtx, AdjudicatorUserDefinedPML adjudicator, PAP pap, EventProcessor listener) {
        this.userCtx = userCtx;
        this.adjudicator = adjudicator;
        this.pap = pap;
        this.listener = listener;
    }

    @Override
    public void createFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
        adjudicator.createFunction(functionDefinitionStatement);

        pap.userDefinedPML().createFunction(functionDefinitionStatement);

        emitEvent(new EventContext(userCtx, new CreateFunctionEvent(functionDefinitionStatement)));
    }

    @Override
    public void deleteFunction(String functionName) throws PMException {
        adjudicator.deleteFunction(functionName);

        pap.userDefinedPML().deleteFunction(functionName);

        emitEvent(new EventContext(userCtx, new DeleteFunctionEvent(functionName)));

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

        emitEvent(new EventContext(userCtx, new CreateConstantEvent(constantName, constantValue)));

    }

    @Override
    public void deleteConstant(String constName) throws PMException {
        adjudicator.deleteConstant(constName);

        pap.userDefinedPML().deleteConstant(constName);

        emitEvent(new EventContext(userCtx, new DeleteConstantEvent(constName)));
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
    public void addEventListener(EventProcessor listener) {

    }

    @Override
    public void removeEventListener(EventProcessor listener) {

    }

    @Override
    public void emitEvent(EventContext event) throws PMException {
        this.listener.processEvent(event);
    }
}
