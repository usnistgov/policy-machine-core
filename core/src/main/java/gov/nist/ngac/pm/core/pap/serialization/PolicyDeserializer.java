package gov.nist.ngac.pm.core.pap.serialization;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;

/**
 * Loads a serialized policy into a {@link PAP}.
 */
public interface PolicyDeserializer {

    /**
     * Deserializes the given input and applies it to the given PAP.
     *
     * @param pap the PAP to load the policy into
     * @param input the serialized policy
     * @throws PMException if the input can't be parsed or applying it fails
     */
    void deserialize(PAP pap, String input) throws PMException;

}
