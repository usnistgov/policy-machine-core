package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.adjudicator.Adjudicator;
import gov.nist.csd.pm.policy.serializer.PolicyDeserializer;
import gov.nist.csd.pm.policy.serializer.PolicySerializer;
import gov.nist.csd.pm.policy.author.*;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.ArrayList;
import java.util.List;

public abstract class PDP implements PolicyEventEmitter {

    protected final PAP pap;
    protected final List<PolicyEventListener> eventListeners;

    protected PDP(PAP pap) {
        this.pap = pap;
        this.eventListeners = new ArrayList<>();
    }

    public abstract PolicyReviewer policyReviewer() throws PMException;

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

    public static class PDPTx extends PolicyAuthor {

        private final Graph graph;
        private final Prohibitions prohibitions;
        private final Obligations obligations;
        private final PAL pal;
        private final Adjudicator adjudicator;
        private final PAP pap;

        public PDPTx(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer, List<PolicyEventListener> epps) {
            this.graph = new Graph(userCtx, pap, policyReviewer, epps);
            this.prohibitions = new Prohibitions(userCtx, pap, policyReviewer, epps);
            this.obligations = new Obligations(userCtx, pap, policyReviewer, epps);
            this.pal = new PAL(userCtx, pap, policyReviewer, epps);
            this.adjudicator = new Adjudicator(userCtx, pap, policyReviewer);
            this.pap = pap;
        }

        @Override
        public GraphAuthor graph() {
            return graph;
        }

        @Override
        public ProhibitionsAuthor prohibitions() {
            return prohibitions;
        }

        @Override
        public ObligationsAuthor obligations() {
            return obligations;
        }

        @Override
        public PALAuthor pal() {
            return pal;
        }

        @Override
        public String toString(PolicySerializer policySerializer) throws PMException {
            adjudicator.toString(policySerializer);

            return pap.toString(policySerializer);
        }

        @Override
        public void fromString(String s, PolicyDeserializer policyDeserializer) throws PMException {
            adjudicator.fromString(s, policyDeserializer);

            pap.fromString(s, policyDeserializer);
        }
    }
}
