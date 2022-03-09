package gov.nist.csd.pm.policy.exceptions;

import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.Arrays;

public class UnauthorizedException extends PMException {

    public UnauthorizedException(UserContext user, String target, String ... missingAccessRights) {
        super("[" + user + "] does not have access right " + Arrays.toString(missingAccessRights) + " on [" + target + "]");
    }

    public UnauthorizedException(UserContext user, String ... missingAccessRights) {
        super("[" + user + "] does not have access right " + Arrays.toString(missingAccessRights));
    }
}
