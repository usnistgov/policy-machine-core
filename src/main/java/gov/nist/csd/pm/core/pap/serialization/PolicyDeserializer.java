package gov.nist.csd.pm.core.pap.serialization;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;

public interface PolicyDeserializer {

    void deserialize(PAP pap, String input) throws PMException;

}
