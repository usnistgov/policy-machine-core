package gov.nist.csd.pm.pdp.bootstrap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.List;

public abstract class PolicyBootstrapper {

    protected List<Operation<?, ?>> operations;
    protected List<Routine<?, ?>> routines;

    public PolicyBootstrapper() {
        operations = new ArrayList<>();
        routines = new ArrayList<>();
    }

    public PolicyBootstrapper(List<Operation<?, ?>> operations, List<Routine<?, ?>> routines) {
        this.operations = operations;
        this.routines = routines;
    }

    public abstract void bootstrap(UserContext bootstrapUser, PAP pap) throws PMException;

}
