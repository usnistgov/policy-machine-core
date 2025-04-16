package gov.nist.csd.pm.pdp.bootstrap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.serialization.json.JSONDeserializer;
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
    public void bootstrap(UserContext bootstrapUser, PAP pap) throws PMException {
        pap.deserialize(bootstrapUser, json, new JSONDeserializer());
    }
}
