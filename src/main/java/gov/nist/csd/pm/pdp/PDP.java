package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.epp.EventEmitter;
import gov.nist.csd.pm.epp.EventListener;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutable;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.ArrayList;
import java.util.List;

public abstract class PDP implements EventEmitter {

    protected final PAP pap;
    protected final List<EventListener> eventListeners;

    protected PDP(PAP pap) {
        this.pap = pap;
        this.eventListeners = new ArrayList<>();
    }

    public abstract PolicyReviewer reviewer() throws PMException;

    public abstract void runTx(UserContext userCtx, PDPTxRunner txRunner) throws PMException;

    @Override
    public void addEventListener(EventListener listener) {
        eventListeners.add(listener);
    }

    @Override
    public void removeEventListener(EventListener listener) {
        eventListeners.remove(listener);
    }

    @Override
    public void emitEvent(EventContext event) throws PMException {
        for (EventListener listener : eventListeners) {
            listener.processEvent(event);
        }
    }

    public interface PDPTxRunner {
        void run(PDPTx policy) throws PMException;
    }

    public static class PDPTx implements Policy, PMLExecutable, EventEmitter, EventListener {

        private final Adjudicator adjudicator;
        private final PAP pap;
        private final List<EventListener> epps;

        private final PDPGraph pdpGraph;
        private final PDPProhibitions pdpProhibitions;
        private final PDPObligations pdpObligations;
        private final PDPUserDefinedPML pdpUserDefinedPML;

        public PDPTx(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer, List<EventListener> epps) throws PMException {
            this.adjudicator = new Adjudicator(userCtx, pap, policyReviewer);
            this.pap = pap;
            this.epps = epps;

            this.pdpGraph = new PDPGraph(userCtx, adjudicator.graph(), pap, this);
            this.pdpProhibitions = new PDPProhibitions(userCtx, adjudicator.prohibitions(), pap, this);
            this.pdpObligations = new PDPObligations(userCtx, adjudicator.obligations(), pap, this);
            this.pdpUserDefinedPML = new PDPUserDefinedPML(userCtx, adjudicator.userDefinedPML(), pap, this);
        }

        @Override
        public void addEventListener(EventListener listener) {
            epps.add(listener);
        }

        @Override
        public void removeEventListener(EventListener listener) {
            epps.remove(listener);
        }

        @Override
        public void emitEvent(EventContext event) throws PMException {
            for (EventListener epp : epps) {
                epp.processEvent(event);
            }
        }

        @Override
        public void processEvent(EventContext eventCtx) throws PMException {
            for (EventListener epp : epps) {
                epp.processEvent(eventCtx);
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
        public void reset() throws PMException {
            adjudicator.reset();

            pap.reset();
        }
    }
}
