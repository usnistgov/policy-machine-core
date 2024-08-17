package gov.nist.csd.pm.pap.serialization.pml;

import gov.nist.csd.pm.pap.serialization.PolicyDeserializer;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.query.UserContext;

public class PMLDeserializer implements PolicyDeserializer {

    @Override
    public void deserialize(PAP pap, UserContext author, String input) throws PMException {
        pap.executePML(author, input);
    }
}
