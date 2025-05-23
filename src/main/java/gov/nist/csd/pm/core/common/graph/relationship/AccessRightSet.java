package gov.nist.csd.pm.core.common.graph.relationship;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Arrays;
import java.util.Collection;

public class AccessRightSet extends ObjectOpenHashSet<String> {

    public AccessRightSet(){
    }

    public AccessRightSet(String ... ops) {
        this.addAll(Arrays.asList(ops));
    }

    public AccessRightSet(Collection<String> ops) {
        this.addAll(ops);
    }

    public AccessRightSet(AccessRightSet a, AccessRightSet b) {
        this.addAll(a);
        this.addAll(b);
    }
}
