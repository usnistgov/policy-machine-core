package gov.nist.csd.pm.policy.model.access;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class AccessRightSet extends HashSet<String> {

    public AccessRightSet(){
    }

    public AccessRightSet(String ... ops) {
        this.addAll(Arrays.asList(ops));
    }

    public AccessRightSet(Collection<String> ops) {
        this.addAll(ops);
    }
}
