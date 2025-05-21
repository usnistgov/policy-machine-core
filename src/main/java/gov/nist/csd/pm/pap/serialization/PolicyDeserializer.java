package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;

public interface PolicyDeserializer {

    void deserialize(PAP pap, String input) throws PMException;

}
