package gov.nist.csd.pm.pdp.memory;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStoreEventHandler;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.tx.TxRunner;

public class MemoryPDP extends PDP {

    private final PDPEventListener policyEventHandler;
    private final MemoryPolicyReviewer memoryPolicyReviewer;

    public MemoryPDP(PAP pap, boolean loadPolicyIntoMemory) throws PMException {
        super(pap);

        if (loadPolicyIntoMemory) {
            // load the policy into memory
            this.policyEventHandler = new ReviewerPolicyListener(new MemoryPolicyStore());
            this.pap.addEventListener(this.policyEventHandler, true);
        } else {
            this.policyEventHandler = new EmbeddedPolicyListener(pap);
        }

        this.memoryPolicyReviewer = new MemoryPolicyReviewer(policyEventHandler.policy);
    }

    @Override
    public PolicyReviewer reviewer() throws PMException {
        return memoryPolicyReviewer;
    }

    public void runTx(UserContext userCtx, PDPTxRunner txRunner) throws PMException {
        TxRunner.runTx(pap, () -> {
            PDPTx pdpTx = new PDPTx(userCtx, pap, new BulkPolicyReviewer(userCtx, pap), eventListeners);
            txRunner.run(pdpTx);
        });
    }

    private static class ReviewerPolicyListener extends PDPEventListener {

        public ReviewerPolicyListener(MemoryPolicyStore store) {
            super(store);
        }

        @Override
        public void handlePolicyEvent(PolicyEvent event) throws PMException {
            // ignore begin and commit events
            // reviewer will operate as all events are added to the policy
            // in the event of rollback it will call policySync to rollback

            if (event instanceof RollbackTxEvent rollbackTxEvent) {
                policy = rollbackTxEvent.policySync().getPolicyStore();
            } else if (event instanceof PolicySynchronizationEvent policySynchronizationEvent) {
                policy = policySynchronizationEvent.getPolicyStore();
            } else {
                event.apply(policy);
            }
        }
    }

    private static class EmbeddedPolicyListener extends PDPEventListener {

        public EmbeddedPolicyListener(PAP pap) {
            super(pap);
        }

        @Override
        public void handlePolicyEvent(PolicyEvent event) throws PMException {
            // don't need to handle events as the pap will be updated in real time
        }
    }

    private abstract static class PDPEventListener implements PolicyEventListener {

        Policy policy;

        public PDPEventListener(Policy policy) {
            this.policy = policy;
        }
    }
}
