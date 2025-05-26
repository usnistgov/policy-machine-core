package gov.nist.csd.pm.core.common.graph.dag;

import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record UserDagResult(Map<Long, AccessRightSet> borderTargets, Set<Prohibition> prohibitions) {
    public UserDagResult() {
        this(new Long2ObjectOpenHashMap<>(), new HashSet<>());
    }
}
