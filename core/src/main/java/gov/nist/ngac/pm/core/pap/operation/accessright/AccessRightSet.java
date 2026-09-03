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

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Arrays;
import java.util.Collection;

/**
 * A set of access right names.
 */
public class AccessRightSet extends ObjectOpenHashSet<String> {

    public AccessRightSet(){
    }

    public AccessRightSet(AdminAccessRight... adminAccessRight) {
        super();

        for (AdminAccessRight a : adminAccessRight) {
            this.add(a.toString());
        }
    }

    public AccessRightSet(String ... ops) {
        this.addAll(Arrays.asList(ops));
    }

    public AccessRightSet(Collection<String> ops) {
        super();
        this.addAll(ops);
    }

    public AccessRightSet(AccessRightSet a, AccessRightSet b) {
        super();

        this.addAll(a);
        this.addAll(b);
    }

    /**
     * Returns a set containing only the "*" wildcard, matching every access right.
     *
     * @return a new set containing only the "*" wildcard
     */
    public static AccessRightSet wildcard() {
        return new AccessRightSet(WildcardAccessRight.WILDCARD.toString());
    }

    /**
     * Returns a set containing only the "admin:*" wildcard, matching every admin access right.
     *
     * @return a new set containing only the "admin:*" wildcard
     */
    public static AccessRightSet adminWildcard() {
        return new AccessRightSet(WildcardAccessRight.ADMIN_WILDCARD.toString());
    }

    /**
     * Returns a set containing only the "resource:*" wildcard, matching every resource access right.
     *
     * @return a new set containing only the "resource:*" wildcard
     */
    public static AccessRightSet resourceWildcard() {
        return new AccessRightSet(WildcardAccessRight.RESOURCE_WILDCARD.toString());
    }
}