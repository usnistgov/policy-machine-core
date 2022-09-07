package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.Adjudicator;
import gov.nist.csd.pm.pdp.reviewer.PolicyReviewer;
import gov.nist.csd.pm.policy.author.PALAuthor;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;

import java.util.List;
import java.util.Map;

class PAL implements PALAuthor, PolicyEventEmitter {

    private final UserContext userCtx;
    private final PAP pap;
    private final Adjudicator adjudicator;
    private final List<PolicyEventListener> epps;

    public PAL(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer, List<PolicyEventListener> epps) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.adjudicator = new Adjudicator(userCtx, pap, policyReviewer);
        this.epps = epps;
    }

    @Override
    public void addFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException {
        adjudicator.pal().addFunction(functionDefinitionStatement);

        pap.pal().addFunction(functionDefinitionStatement);

        emitEvent(new EventContext(userCtx, new AddFunctionEvent(functionDefinitionStatement)));
    }

    @Override
    public void removeFunction(String functionName) throws PMException {
        adjudicator.pal().removeFunction(functionName);

        pap.pal().removeFunction(functionName);

        emitEvent(new EventContext(userCtx, new RemoveFunctionEvent(functionName)));
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getFunctions() throws PMException {
        adjudicator.pal().getFunctions();

        return pap.pal().getFunctions();
    }

    @Override
    public void addConstant(String constantName, Value constantValue) throws PMException {
        adjudicator.pal().addConstant(constantName, constantValue);

        pap.pal().addConstant(constantName, constantValue);

        emitEvent(new EventContext(userCtx, new AddConstantEvent(constantName, constantValue)));
    }

    @Override
    public void removeConstant(String constName) throws PMException {
        adjudicator.pal().removeConstant(constName);

        pap.pal().removeConstant(constName);

        emitEvent(new EventContext(userCtx, new RemoveConstantEvent(constName)));
    }

    @Override
    public Map<String, Value> getConstants() throws PMException {
        adjudicator.pal().getConstants();

        return pap.pal().getConstants();
    }

    @Override
    public PALContext getContext() throws PMException {
        adjudicator.pal().getContext();

        return adjudicator.pal().getContext();
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
        // adding event listeners is done by the PDP class
    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {
        // removing event listeners is done by the PDP class
    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        for (PolicyEventListener epp : epps) {
            epp.handlePolicyEvent(event);
        }
    }
}
