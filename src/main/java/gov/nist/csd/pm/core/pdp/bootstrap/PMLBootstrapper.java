package gov.nist.csd.pm.core.pdp.bootstrap;

import static gov.nist.csd.pm.core.common.tx.TxRunner.runTx;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.routine.Routine;

import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class PMLBootstrapper extends PolicyBootstrapper {

    private final String bootstrapUser;
    private final String pml;

    public PMLBootstrapper(List<Operation<?, ?>> operations, List<Routine<?, ?>> routines, String bootstrapUser, String pml) {
        super(operations, routines);
        this.bootstrapUser = bootstrapUser;
        this.pml = pml;
    }

    @Override
    public void bootstrap(PAP pap) throws PMException {
        for (Operation<?, ?> op : operations) {
            pap.modify().operations().createAdminOperation(op);
        }

        for (Routine<?, ?> r : routines) {
            pap.modify().routines().createAdminRoutine(r);
        }

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
