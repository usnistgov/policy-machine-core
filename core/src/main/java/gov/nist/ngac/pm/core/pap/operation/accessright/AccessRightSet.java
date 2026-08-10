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