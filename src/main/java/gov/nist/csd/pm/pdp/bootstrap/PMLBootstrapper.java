package gov.nist.csd.pm.pdp.bootstrap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutine;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public class PMLBootstrapper implements PolicyBootstrapper {

    private final String pml;
    private final List<PMLOperation> pmlOperations;
    private final List<PMLRoutine> pmlRoutines;
    private final Map<String, Value> pmlConstants;

    public PMLBootstrapper(String pml, List<PMLOperation> pmlOperations, List<PMLRoutine> pmlRoutines, Map<String, Value> pmlConstants) {
        this.pml = pml;
        this.pmlOperations = pmlOperations;
        this.pmlRoutines = pmlRoutines;
        this.pmlConstants = pmlConstants;
    }

    @Override
    public void bootstrap(UserContext bootstrapUser, PAP pap) throws PMException {
        pap.setPMLOperations(pmlOperations.toArray(PMLOperation[]::new));
        pap.setPMLRoutines(pmlRoutines.toArray(PMLRoutine[]::new));
        pap.setPMLConstants(pmlConstants);
        pap.executePML(bootstrapUser, pml);
    }
}
