package gov.nist.csd.pm.core.pdp.bootstrap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
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

    public abstract void bootstrap(PAP pap) throws PMException;

}
