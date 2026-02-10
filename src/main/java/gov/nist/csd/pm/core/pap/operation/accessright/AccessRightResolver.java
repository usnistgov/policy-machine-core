package gov.nist.csd.pm.core.pap.operation.accessright;

import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.query.access.TargetDagResult;
import gov.nist.csd.pm.core.pap.query.access.UserDagResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AccessRightResolver {

    private AccessRightResolver() {}

    public static AccessRightSet resolvePrivileges(UserDagResult userCtx, TargetDagResult targetCtx, AccessRightSet resourceAccessRights) {
        // resolve any access rights with "*" to their enumerated rights
        Map<Long, AccessRightSet> resolvedPcMap = resolvePcMap(targetCtx.pcMap(), resourceAccessRights);

        // determine the common set of access rights across all policy classes
        AccessRightSet result = resolvePolicyClassAccessRightSets(resolvedPcMap);

        // remove any prohibited access rights
        AccessRightSet denied = resolveDeniedAccessRights(userCtx, targetCtx);
        result.removeAll(denied);

        return result;
    }

    private static Map<Long, AccessRightSet> resolvePcMap(Map<Long, AccessRightSet> targetCtx, AccessRightSet resourceAccessRights) {
        Map<Long, AccessRightSet> resolvedPcMap = new HashMap<>();
        for (Map.Entry<Long, AccessRightSet> pc : targetCtx.entrySet()) {
            AccessRightSet pcOps = pc.getValue();

            // replace instances of *, *a or *r with the literal access rights
            pcOps = resolveWildcardAccessRights(pcOps, resourceAccessRights);

            resolvedPcMap.put(pc.getKey(), pcOps);
        }

        return resolvedPcMap;
    }

    public static AccessRightSet resolveDeniedAccessRights(UserDagResult userCtx, TargetDagResult targetCtx) {
        AccessRightSet denied = new AccessRightSet();
        Set<Prohibition> prohibitions = userCtx.prohibitions();
        Set<Long> reachedTargets = targetCtx.reachedTargets();

        for(Prohibition p : prohibitions) {
            if (isProhibitionSatisfied(p, reachedTargets)) {
                denied.addAll(p.getAccessRightSet());
            }
        }

        return denied;
    }

    public static List<Prohibition> computeSatisfiedProhibitions(UserDagResult userDagResult, TargetDagResult targetDagResult) {
        List<Prohibition> satisfied = new ArrayList<>();

        Set<Prohibition> prohibitions = userDagResult.prohibitions();
        Set<Long> reachedTargets = targetDagResult.reachedTargets();

        for(Prohibition p : prohibitions) {
            if (isProhibitionSatisfied(p, reachedTargets)) {
                satisfied.add(p);
            }
        }

        return satisfied;
    }

    private static AccessRightSet resolvePolicyClassAccessRightSets(Map<Long, AccessRightSet> pcMap) {
        // retain only the ops that the decider knows about
        AccessRightSet allowed = new AccessRightSet();
        boolean first = true;
        for (AccessRightSet ops : pcMap.values()) {
            if(first) {
                allowed.addAll(ops);
                first = false;
                continue;
            }

            // if the ops for the pc are empty then the user has no permissions on the target
            if (ops.isEmpty()) {
                return new AccessRightSet();
            } else {
                allowed.retainAll(ops);
            }
        }

        return allowed;
    }

    private static AccessRightSet resolveWildcardAccessRights(AccessRightSet accessRightSet, AccessRightSet resourceOps) {
        AccessRightSet resolved = new AccessRightSet();

        for (String ar : accessRightSet) {
            WildcardAccessRight wildcardAccessRight = WildcardAccessRight.fromString(ar);
            if (wildcardAccessRight == null) {
                resolved.add(ar);
            } else {
                resolved.addAll(wildcardAccessRight.resolveAccessRights(resourceOps));
            }
        }

        return resolved;
    }

    private static boolean isProhibitionSatisfied(Prohibition prohibition, Set<Long> reachedAttributes) {
        Set<Long> inclusionSet = prohibition.getInclusionSet();
        Set<Long> exclusionSet = prohibition.getExclusionSet();
        boolean isConjunctive = prohibition.isConjunctive();

        if (inclusionSet.isEmpty() && exclusionSet.isEmpty()) {
            return false;
        }

        if (isConjunctive) {
            // conjunctive (intersection): all inclusion and exclusion conditions must be met

            // target must be an ascendant of every node in the inclusion set
            for (long inc : inclusionSet) {
                if (!reachedAttributes.contains(inc)) return false;
            }

            // target must NOT be an ascendant of ANY node in the exclusion set
            for (long exc : exclusionSet) {
                if (reachedAttributes.contains(exc)) return false;
            }

            return true;
        } else {
            // disjunctive (union): only one inclusion or exclusion condition needs to be met

            // satisfied if target is an ascendant of ANY node in the inclusion set
            for (long inc : inclusionSet) {
                if (reachedAttributes.contains(inc)) return true;
            }

            // satisfied if target is NOT an ascendant of at least one node in the exclusion set.
            for (long exc : exclusionSet) {
                if (!reachedAttributes.contains(exc)) return true;
            }

            return false;
        }
    }
}
