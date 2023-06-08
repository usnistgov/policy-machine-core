package gov.nist.csd.pm.pdp.exception;

import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.common.exception.PMException;

import java.util.Arrays;

public class UnauthorizedException extends PMException {

    public UnauthorizedException(UserContext user, String target, String ... missingAccessRights) {
        super("[" + user + "] does not have access right " + Arrays.toString(missingAccessRights) + " on [" + target + "]");
    }

}
