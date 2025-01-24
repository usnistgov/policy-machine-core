package gov.nist.csd.pm.common.graph.dag;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record UserDagResult(Object2ObjectOpenHashMap<String, AccessRightSet> borderTargets,
                            Set<Prohibition> prohibitions) {
    public UserDagResult() {
        this(new Object2ObjectOpenHashMap<>(), new HashSet<>());
    }
}
