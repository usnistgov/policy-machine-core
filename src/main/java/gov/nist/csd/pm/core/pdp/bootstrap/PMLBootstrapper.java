package gov.nist.csd.pm.core.pdp.bootstrap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.PluginRegistry;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.routine.Routine;

import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.List;

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
            tx.executePML(new UserContext(bootstrapUserId), pml);

            // clean up bootstrap policy
            tx.modify().graph().deassign(bootstrapUserId, List.of(ua));
            tx.modify().graph().deleteNode(ua);
            tx.modify().graph().deleteNode(pc);
        });
    }
}
