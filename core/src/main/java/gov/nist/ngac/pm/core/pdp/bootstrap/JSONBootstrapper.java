package gov.nist.ngac.pm.core.pdp.bootstrap;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.serialization.json.JSONDeserializer;

/**
 * {@link PolicyBootstrapper} that deserializes a JSON policy document into the PAP.
 */
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
