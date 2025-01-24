package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.graph.dag.TargetDagResult;
import gov.nist.csd.pm.common.graph.dag.UserDagResult;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.Prohibition;

import java.util.*;

import static gov.nist.csd.pm.pap.AdminAccessRights.*;
import static gov.nist.csd.pm.pap.AdminAccessRights.ALL_RESOURCE_ACCESS_RIGHTS;

public class AccessRightResolver {

    private AccessRightResolver() {}

    public static AccessRightSet resolvePrivileges(UserDagResult userCtx, TargetDagResult targetCtx, AccessRightSet resourceOps) {
        Map<String, AccessRightSet> resolvedPcMap = new HashMap<>();
        for (Map.Entry<String, AccessRightSet> pc : targetCtx.pcMap().entrySet()) {
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
        Set<String> reachedTargets = targetCtx.reachedTargets();

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
        Set<String> reachedTargets = targetDagResult.reachedTargets();

        for(Prohibition p : prohibitions) {
            if (isProhibitionSatisfied(p, reachedTargets)) {
                satisfied.add(p);
            }
        }

        return satisfied;
    }

    private static AccessRightSet resolvePolicyClassAccessRightSets(Map<String, AccessRightSet> pcMap) {
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
        // if the permission set includes *, remove the * and add all resource operations
        if (accessRightSet.contains(ALL_ACCESS_RIGHTS)) {
            accessRightSet.remove(ALL_ACCESS_RIGHTS);
            accessRightSet.addAll(allAdminAccessRights());
            accessRightSet.addAll(resourceOps);
        } else {
            // if the permissions includes *a or *r add all the admin ops/resource ops as necessary
            if (accessRightSet.contains(ALL_ADMIN_ACCESS_RIGHTS)) {
                accessRightSet.remove(ALL_ADMIN_ACCESS_RIGHTS);
                accessRightSet.addAll(allAdminAccessRights());
            }
            if (accessRightSet.contains(ALL_RESOURCE_ACCESS_RIGHTS)) {
                accessRightSet.remove(ALL_RESOURCE_ACCESS_RIGHTS);
                accessRightSet.addAll(resourceOps);
            }
        }
    }

    private static boolean isProhibitionSatisfied(Prohibition prohibition, Set<String> reachedTargets) {
        boolean inter = prohibition.isIntersection();
        Collection<ContainerCondition> containers = prohibition.getContainers();
        boolean addOps = false;

        for (ContainerCondition containerCondition : containers) {
            String contName = containerCondition.getId();
            boolean isComplement = containerCondition.isComplement();

            addOps = !isComplement && reachedTargets.contains(contName) ||
                    isComplement && !reachedTargets.contains(contName);

            if ((addOps && !inter) || (!addOps && inter)) {
                break;
            }
        }

        return addOps;
    }

}
