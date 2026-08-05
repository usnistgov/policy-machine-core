package gov.nist.ngac.pm.core.pap.serialization;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;

public interface PolicyDeserializer {

    void deserialize(PAP pap, String input) throws PMException;

}
