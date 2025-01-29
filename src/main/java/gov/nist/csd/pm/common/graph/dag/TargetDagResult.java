package gov.nist.csd.pm.common.graph.dag;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record TargetDagResult(Map<Long, AccessRightSet> pcMap, Set<Long> reachedTargets) {

}
