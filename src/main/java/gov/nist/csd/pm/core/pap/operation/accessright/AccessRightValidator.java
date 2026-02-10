package gov.nist.csd.pm.core.pap.operation.accessright;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.UnknownAccessRightException;

public class AccessRightValidator {

    public static void validateAccessRights(AccessRightSet resourceAccessRights, Iterable<String> accessRightSet) throws PMException {
        for (String ar : accessRightSet) {
            if (!resourceAccessRights.contains(ar)
                && !isAdminAccessRight(ar)
                && !isWildcardAccessRight(ar)) {
                throw new UnknownAccessRightException(ar);
            }
        }
    }

    public static boolean isAdminAccessRight(String ar) {
        return AdminAccessRight.fromString(ar) != null;
    }

    public static boolean isWildcardAccessRight(String ar) {
        return WildcardAccessRight.fromString(ar) != null;
    }

}
