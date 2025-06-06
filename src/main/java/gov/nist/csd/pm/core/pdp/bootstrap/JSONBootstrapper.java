package gov.nist.csd.pm.core.pdp.bootstrap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.routine.Routine;
import gov.nist.csd.pm.core.pap.serialization.json.JSONDeserializer;
import java.util.List;

public class JSONBootstrapper extends PolicyBootstrapper{

    private String json;

    public JSONBootstrapper(List<Operation<?, ?>> operations,
                            List<Routine<?, ?>> routines,
                            String json) {
        super(operations, routines);
        this.json = json;
    }

    @Override
    public void bootstrap(PAP pap) throws PMException {
        pap.deserialize(json, new JSONDeserializer());
    }
}
