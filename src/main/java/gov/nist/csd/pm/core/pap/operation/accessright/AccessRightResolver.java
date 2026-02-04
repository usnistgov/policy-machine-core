package gov.nist.csd.pm.core.pap.operation.accessright;

import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.query.access.TargetDagResult;
import gov.nist.csd.pm.core.pap.query.access.UserDagResult;
import java.util.ArrayList;
import java.util.Collection;
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

    private static boolean isProhibitionSatisfied(Prohibition prohibition, Set<Long> reachedTargets) {
        boolean inter = prohibition.isIntersection();
        Collection<ContainerCondition> containers = prohibition.getContainers();
        boolean addOps = false;

        for (ContainerCondition containerCondition : containers) {
            long contId = containerCondition.getId();
            boolean isComplement = containerCondition.isComplement();
            boolean reached = reachedTargets.contains(contId);

            addOps = !isComplement && reached ||
                    isComplement && !reached;

            if ((addOps && !inter) || (!addOps && inter)) {
                break;
            }
        }

        return addOps;
    }

}
