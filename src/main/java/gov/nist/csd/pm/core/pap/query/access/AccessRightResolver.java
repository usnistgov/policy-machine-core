package gov.nist.csd.pm.core.pap.query.access;

import gov.nist.csd.pm.core.common.graph.dag.TargetDagResult;
import gov.nist.csd.pm.core.common.graph.dag.UserDagResult;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;

import java.util.*;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.*;

public class AccessRightResolver {

    private AccessRightResolver() {}

    public static AccessRightSet resolvePrivileges(UserDagResult userCtx, TargetDagResult targetCtx, AccessRightSet resourceOps) {
        Map<Long, AccessRightSet> resolvedPcMap = new HashMap<>();
        for (Map.Entry<Long, AccessRightSet> pc : targetCtx.pcMap().entrySet()) {
            AccessRightSet pcOps = pc.getValue();

            // replace instances of *, *a or *r with the literal access rights
            resolveWildcardAccessRights(pcOps, resourceOps);

            resolvedPcMap.put(pc.getKey(), pcOps);
        }

        AccessRightSet result = resolvePolicyClassAccessRightSets(resolvedPcMap);

        // remove any prohibited access rights
        AccessRightSet denied = resolveDeniedAccessRights(userCtx, targetCtx);
        result.removeAll(denied);

        return result;
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

    private static void resolveWildcardAccessRights(AccessRightSet accessRightSet, AccessRightSet resourceOps) {
        if (accessRightSet.contains(WC_ALL)) {
            accessRightSet.clear();
            accessRightSet.addAll(ALL_ACCESS_RIGHTS_SET);
            accessRightSet.addAll(resourceOps);
            return;
        }

        for (Map.Entry<String, Set<String>> entry : WILDCARD_MAP.entrySet()) {
            String wildcard = entry.getKey();
            if (accessRightSet.contains(wildcard)) {
                accessRightSet.remove(wildcard);
                accessRightSet.addAll(entry.getValue());
            }
        }
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
