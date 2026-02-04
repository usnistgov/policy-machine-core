package gov.nist.csd.pm.core.pap.operation.accessright;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Arrays;
import java.util.Collection;

public class AccessRightSet extends ObjectOpenHashSet<String> {

    public AccessRightSet(){
    }

    public AccessRightSet(AdminAccessRight... adminAccessRight) {
        this();

        for (AdminAccessRight a : adminAccessRight) {
            this.add(a.toString());
        }
    }

    public AccessRightSet(String ... ops) {
        this.addAll(Arrays.asList(ops));
    }

    public AccessRightSet(Collection<String> ops) {
        this();
        this.addAll(ops);
    }

    public AccessRightSet(AccessRightSet a, AccessRightSet b) {
        this();

        this.addAll(a);
        this.addAll(b);
    }

    public static AccessRightSet wildcard() {
        return new AccessRightSet(WildcardAccessRight.WILDCARD.toString());
    }

    public static AccessRightSet adminWildcard() {
        return new AccessRightSet(WildcardAccessRight.ADMIN_WILDCARD.toString());
    }

    public static AccessRightSet resourceWildcard() {
        return new AccessRightSet(WildcardAccessRight.RESOURCE_WILDCARD.toString());
    }
}