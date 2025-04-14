package gov.nist.csd.pm.pdp.bootstrap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

public class PMLBootstrapper extends PolicyBootstrapper {

    private final String pml;

    public PMLBootstrapper(List<Operation<?, ?>> operations, List<Routine<?, ?>> routines, String pml) {
        super(operations, routines);
        this.pml = pml;
    }

    @Override
    public void bootstrap(UserContext bootstrapUser, PAP pap) throws PMException {
        for (Operation<?, ?> op : operations) {
            pap.modify().operations().createAdminOperation(op);
        }

        for (Routine<?, ?> r : routines) {
            pap.modify().routines().createAdminRoutine(r);
        }

        pap.executePML(bootstrapUser, pml);
    }
}
