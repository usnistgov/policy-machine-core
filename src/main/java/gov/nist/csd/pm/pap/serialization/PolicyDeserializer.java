package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.UserContext;

public interface PolicyDeserializer {

    void deserialize(PAP pap, UserContext author, String input) throws PMException;

}
