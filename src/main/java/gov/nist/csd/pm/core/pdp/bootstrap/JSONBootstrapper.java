package gov.nist.csd.pm.core.pdp.bootstrap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.serialization.json.JSONDeserializer;

public class JSONBootstrapper extends PolicyBootstrapper{

    private String json;

    public JSONBootstrapper(String json) {
        this.json = json;
    }

    @Override
    public void bootstrap(PAP pap) throws PMException {
        pap.deserialize(json, new JSONDeserializer());
    }
}
