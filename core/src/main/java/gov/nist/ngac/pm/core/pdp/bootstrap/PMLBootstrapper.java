package gov.nist.ngac.pm.core.pdp.bootstrap;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import java.util.List;

/**
 * {@link PolicyBootstrapper} that executes a PML script as a temporary bootstrap user, tearing the
 * bootstrap user's scaffolding down afterward.
 */
public class PMLBootstrapper extends PolicyBootstrapper {

    private final String bootstrapUser;
    private final String pml;

    public PMLBootstrapper(String bootstrapUser, String pml) {
        this.bootstrapUser = bootstrapUser;
        this.pml = pml;
    }

    @Override
    public void bootstrap(PAP pap) throws PMException {
        pap.runTx(tx -> {
            // create bootstrap policy and user
            long pc = tx.modify().graph().createPolicyClass("bootstrap");
            long ua = tx.modify().graph().createUserAttribute("bootstrapper", List.of(pc));
            long bootstrapUserId = tx.modify().graph().createUser(bootstrapUser, List.of(ua));

            // execute the pml
            tx.executePML(NodeUserContext.of(bootstrapUserId), pml);

            // clean up bootstrap policy
            tx.modify().graph().deassign(bootstrapUserId, List.of(ua));
            tx.modify().graph().deleteNode(ua);
            tx.modify().graph().deleteNode(pc);
        });
    }
}
