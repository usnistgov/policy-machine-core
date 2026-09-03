/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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
