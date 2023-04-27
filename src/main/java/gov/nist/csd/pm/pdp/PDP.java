package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.Adjudicator;
import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.events.PolicyEventEmitter;
import gov.nist.csd.pm.policy.events.PolicyEventListener;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutable;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.ArrayList;
import java.util.List;

public abstract class PDP implements PolicyEventEmitter {

    protected final PAP pap;
    protected final List<PolicyEventListener> eventListeners;

    protected PDP(PAP pap) {
        this.pap = pap;
        this.eventListeners = new ArrayList<>();
    }

    public abstract PolicyReviewer reviewer() throws PMException;

    public abstract void runTx(UserContext userCtx, PDPTxRunner txRunner) throws PMException;

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
        eventListeners.add(listener);

        if (sync) {
            listener.handlePolicyEvent(pap.policySync());
        }
    }

    @Override
    public void removeEventListener(PolicyEventListener listener) {
        eventListeners.remove(listener);
    }

    @Override
    public void emitEvent(PolicyEvent event) throws PMException {
        for (PolicyEventListener listener : eventListeners) {
            listener.handlePolicyEvent(event);
        }
    }

    public interface PDPTxRunner {
        void run(PDPTx policy) throws PMException;
    }

    public static class PDPTx implements PolicyEventEmitter, PolicyEventListener, PMLExecutable, Policy {

        private final UserContext userCtx;
        private final Adjudicator adjudicator;
        private final PAP pap;
        private List<PolicyEventListener> epps;

        private PDPGraph pdpGraph;
        private PDPProhibitions pdpProhibitions;
        private PDPObligations pdpObligations;
        private PDPUserDefinedPML pdpUserDefinedPML;

        public PDPTx(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer, List<PolicyEventListener> epps) throws PMException {
            this.userCtx = userCtx;
            this.adjudicator = new Adjudicator(userCtx, pap, policyReviewer);
            this.pap = pap;
            this.epps = epps;

            this.pdpGraph = new PDPGraph(userCtx, adjudicator.graph(), pap, this);
            this.pdpProhibitions = new PDPProhibitions(userCtx, adjudicator.prohibitions(), pap, this);
            this.pdpObligations = new PDPObligations(userCtx, adjudicator.obligations(), pap, this);
            this.pdpUserDefinedPML = new PDPUserDefinedPML(userCtx, adjudicator.userDefinedPML(), pap, this);
        }

        @Override
        public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
            epps.add(listener);
            
            if (sync) {
                listener.handlePolicyEvent(pap.policySync());
            }
        }

        @Override
        public void removeEventListener(PolicyEventListener listener) {
            epps.remove(listener);
        }

        @Override
        public void emitEvent(PolicyEvent event) throws PMException {
            for (PolicyEventListener epp : epps) {
                epp.handlePolicyEvent(event);
            }
        }

        @Override
        public void executePML(UserContext userContext, String input, FunctionDefinitionStatement... functionDefinitionStatements) throws PMException {
            PMLExecutor.compileAndExecutePML(this, userContext, input, functionDefinitionStatements);
        }

        @Override
        public Graph graph() {
            return pdpGraph;
        }

        @Override
        public Prohibitions prohibitions() {
            return pdpProhibitions;
        }

        @Override
        public Obligations obligations() {
            return pdpObligations;
        }

        @Override
        public UserDefinedPML userDefinedPML() {
            return pdpUserDefinedPML;
        }

        @Override
        public PolicySerializer serialize() throws PMException {
            adjudicator.serialize();

            return pap.serialize();
        }

        @Override
        public PolicyDeserializer deserialize() throws PMException {
            adjudicator.deserialize();

            return pap.deserialize();
        }

        @Override
        public void handlePolicyEvent(PolicyEvent event) throws PMException {
            for (PolicyEventListener listener : epps) {
                listener.handlePolicyEvent(event);
            }
        }
    }
}
