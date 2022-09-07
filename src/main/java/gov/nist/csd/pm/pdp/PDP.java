package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pdp.reviewer.PolicyReviewer;
import gov.nist.csd.pm.policy.author.*;
import gov.nist.csd.pm.policy.author.pal.PALExecutable;
import gov.nist.csd.pm.policy.author.pal.PALExecutor;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.events.PolicyEventEmitter;
import gov.nist.csd.pm.policy.events.PolicyEventListener;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.tx.TxRunner;

import java.util.ArrayList;
import java.util.List;

public class PDP implements PolicyEventEmitter {

    private final PAP pap;
    private final List<PolicyEventListener> eventListeners;

    private final PolicyReviewer policyReviewer;

    public PDP(PAP pap, PolicyReviewer policyReviewer) throws PMException {
        this.pap = pap;
        this.eventListeners = new ArrayList<>();
        this.policyReviewer = policyReviewer;

        this.pap.addEventListener(this.policyReviewer, true);
    }

    public PolicyReviewer policyReviewer() {
        return this.policyReviewer;
    }

    public synchronized void runTx(UserContext userCtx, PDPTxRunner txRunner) throws PMException {
        TxRunner.runTx(pap, () -> {
            PDPTx pdpTx = new PDPTx(userCtx, pap, policyReviewer, eventListeners);
            txRunner.run(pdpTx);
        });
    }

    @Override
    public void addEventListener(PolicyEventListener listener, boolean sync) throws PMException {
        eventListeners.add(listener);
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

    public static class PDPTx extends PolicyAuthor implements PALExecutable {

        private final UserContext userCtx;
        private final List<PolicyEventListener> epps;
        private final PAP pap;
        private final PolicyReviewer policyReviewer;

        public PDPTx(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer, List<PolicyEventListener> epps) {
            this.userCtx = userCtx;
            this.pap = pap;
            this.epps = epps;
            this.policyReviewer = policyReviewer;
        }

        @Override
        public GraphAuthor graph() {
            return new Graph(userCtx, pap, policyReviewer, epps);
        }

        @Override
        public ProhibitionsAuthor prohibitions() {
            return new Prohibitions(userCtx, pap, policyReviewer, epps);
        }

        @Override
        public ObligationsAuthor obligations() {
            return new Obligations(userCtx, pap, policyReviewer, epps);
        }

        @Override
        public PALAuthor pal() {
            return new PAL(userCtx, pap, policyReviewer, epps);
        }

        @Override
        public List<PALStatement> compile(String input) throws PMException {
            return new PALExecutor(this).compile(input);
        }

        @Override
        public void execute(UserContext author, String input) throws PMException {
            new PALExecutor(this).execute(author, input);
        }
    }
}
