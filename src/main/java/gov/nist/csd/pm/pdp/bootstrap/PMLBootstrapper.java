package gov.nist.csd.pm.pdp.bootstrap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.pml.function.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.function.routine.PMLRoutine;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public class PMLBootstrapper implements PolicyBootstrapper {

    private final String pml;
    private final List<Operation<?, ?>> pmlOperations;
    private final List<Routine<?, ?>> pmlRoutines;
    private final Map<String, Object> pmlConstants;

    public PMLBootstrapper(String pml, List<Operation<?, ?>> pmlOperations, List<Routine<?, ?>> pmlRoutines, Map<String, Object> pmlConstants) {
        this.pml = pml;
        this.pmlOperations = pmlOperations;
        this.pmlRoutines = pmlRoutines;
        this.pmlConstants = pmlConstants;
    }

    @Override
    public void bootstrap(UserContext bootstrapUser, PAP pap) throws PMException {
        pap.executePML(bootstrapUser, pml);
    }
}
