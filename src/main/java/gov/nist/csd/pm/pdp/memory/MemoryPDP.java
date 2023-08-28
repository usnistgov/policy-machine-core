package gov.nist.csd.pm.pdp.memory;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.tx.TxRunner;

public class MemoryPDP extends PDP {

    private final MemoryPolicyReviewer memoryPolicyReviewer;

    public MemoryPDP(PAP pap) throws PMException {
        super(pap);

        this.memoryPolicyReviewer = new MemoryPolicyReviewer(pap);
    }

    @Override
    public PolicyReviewer reviewer() throws PMException {
        return memoryPolicyReviewer;
    }

    public void runTx(UserContext userCtx, PDPTxRunner txRunner) throws PMException {
        TxRunner.runTx(pap, () -> {
            PDPTx pdpTx = new PDPTx(userCtx, pap, new MemoryPolicyReviewer(pap), eventProcessors);
            txRunner.run(pdpTx);
        });
    }
}
