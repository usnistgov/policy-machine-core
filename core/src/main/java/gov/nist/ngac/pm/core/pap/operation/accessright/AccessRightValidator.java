package gov.nist.ngac.pm.core.pap.operation.accessright;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.exception.UnknownAccessRightException;

/**
 * Validates that a set of access right names are all known.
 */
public class AccessRightValidator {

    /**
     * Checks that every access right in the given set is known.
     *
     * @param resourceAccessRights the policy's declared resource access rights
     * @param accessRightSet the access rights to validate
     * @throws PMException if any access right is not a declared resource right, admin right, or wildcard
     */
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
