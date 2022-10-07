package gov.nist.csd.pm.pdp.memory;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStoreEventHandler;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.GraphReader;
import gov.nist.csd.pm.policy.ObligationsReader;
import gov.nist.csd.pm.policy.PolicyReader;
import gov.nist.csd.pm.policy.ProhibitionsReader;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.tx.TxRunner;

public class MemoryPDP extends PDP {

    private final PolicyEventHandler policyEventHandler;

    public MemoryPDP(MemoryPAP pap) throws PMException {
        super(pap);

        this.policyEventHandler = new EmbeddedPolicyListener(pap);
    }

    public MemoryPDP(PAP pap) throws PMException {
        super(pap);

        this.policyEventHandler = new ReviewerPolicyListener(new MemoryPolicyStore());
        this.pap.addEventListener(this.policyEventHandler, true);
    }

    @Override
    public PolicyReviewer policyReviewer() throws PMException {
        return new MemoryPolicyReviewer(policyEventHandler);
    }

    public synchronized void runTx(UserContext userCtx, PDPTxRunner txRunner) throws PMException {
        TxRunner.runTx(pap, () -> {
            PDPTx pdpTx = new PDPTx(userCtx, pap, new MemoryPolicyReviewer(policyEventHandler), eventListeners);
            txRunner.run(pdpTx);
        });
    }

    private static class ReviewerPolicyListener extends MemoryPolicyStoreEventHandler implements PolicyReader {

        public ReviewerPolicyListener(MemoryPolicyStore store) {
            super(store);
        }

        @Override
        public synchronized void handlePolicyEvent(PolicyEvent event) throws PMException {
            // ignore begin and commit events
            // reviewer will operate as all events are added to the policy
            // in the event of rollback it will call policySync to rollback

            if (event instanceof RollbackTxEvent rollbackTxEvent) {
                handlePolicySync(rollbackTxEvent.policySync());
            } else {
                super.handlePolicyEvent(event);
            }
        }

        private void handlePolicySync(PolicySynchronizationEvent event) {
            this.policy = new MemoryPolicyStore(event);
        }
    }

    private static class EmbeddedPolicyListener extends PolicyEventHandler implements PolicyReader {

        public EmbeddedPolicyListener(MemoryPAP pap) {
            super(pap);
        }

        @Override
        public void handlePolicyEvent(PolicyEvent event) throws PMException {
            // don't need to handle events as the pap will be updated in real time
        }

        @Override
        public GraphReader graph() {
            return policy.graph();
        }

        @Override
        public ProhibitionsReader prohibitions() {
            return policy.prohibitions();
        }

        @Override
        public ObligationsReader obligations() {
            return policy.obligations();
        }
    }
}
