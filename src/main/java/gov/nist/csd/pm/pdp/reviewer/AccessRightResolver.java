package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.dag.TargetDagResult;
import gov.nist.csd.pm.policy.model.graph.dag.UserDagResult;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.*;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.ALL_RESOURCE_ACCESS_RIGHTS;

public class AccessRightResolver {

    public AccessRightSet resolvePrivileges(UserDagResult userContext, TargetDagResult targetCtx, String target, AccessRightSet resourceOps) {
        Map<String, AccessRightSet> resolvedPcMap = new HashMap<>();
        for (Map.Entry<String, AccessRightSet> pc : targetCtx.pcSet().entrySet()) {
            AccessRightSet pcOps = pc.getValue();

            // replace instances of *, *a or *r with the literal access rights
            resolveWildcardAccessRights(pcOps, resourceOps);

            resolvedPcMap.put(pc.getKey(), pcOps);
        }

        AccessRightSet result = resolvePolicyClassAccessRightSets(resolvedPcMap);

        // remove any prohibited access rights
        AccessRightSet denied = resolveDeniedAccessRights(userContext, targetCtx, target);
        result.removeAll(denied);

        return result;
    }

    public AccessRightSet resolveDeniedAccessRights(UserDagResult userCtx, TargetDagResult targetCtx, String target) {
        AccessRightSet denied = new AccessRightSet();
        Set<Prohibition> prohibitions = userCtx.prohibitions();
        Set<String> reachedTargets = targetCtx.reachedTargets();

        for(Prohibition p : prohibitions) {
            if (isProhibitionSatisfied(p, reachedTargets, target)) {
                denied.addAll(p.getAccessRightSet());
            }
        }

        return denied;
    }

    public List<Prohibition> computeSatisfiedProhibitions(UserDagResult userDagResult, TargetDagResult targetDagResult,
                                                          String target) {
        List<Prohibition> satisfied = new ArrayList<>();

        Set<Prohibition> prohibitions = userDagResult.prohibitions();
        Set<String> reachedTargets = targetDagResult.reachedTargets();

        for(Prohibition p : prohibitions) {
            if (isProhibitionSatisfied(p, reachedTargets, target)) {
                satisfied.add(p);
            }
        }

        return satisfied;
    }

    private AccessRightSet resolvePolicyClassAccessRightSets(Map<String, AccessRightSet> pcMap) {
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

    private void resolveWildcardAccessRights(AccessRightSet accessRightSet, AccessRightSet resourceOps) {
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

    private boolean isProhibitionSatisfied(Prohibition prohibition, Set<String> reachedTargets, String target) {
        boolean inter = prohibition.isIntersection();
        List<ContainerCondition> containers = prohibition.getContainers();
        boolean addOps = false;

        for (ContainerCondition containerCondition : containers) {
            String contName = containerCondition.getName();
            boolean isComplement = containerCondition.isComplement();

            if (target.equals(contName)) {
                // if the prohibition is UNION and the target is the container then the prohibition is satisfied
                // if the prohibition is INTERSECTION and the target is the container then the prohibition is not satisfied
                if (!inter && !isComplement) {
                    addOps = true;
                }

                break;
            }

            if (!isComplement && reachedTargets.contains(contName) || isComplement && !reachedTargets.contains(contName)) {
                addOps = true;

                // if the prohibition is not intersection, one satisfied container condition means
                // the prohibition is satisfied
                if (!inter) {
                    break;
                }
            } else {
                // since the intersection requires the target to satisfy each node condition in the prohibition
                // if one is not satisfied then the whole is not satisfied
                addOps = false;

                // if the prohibition is the intersection, one unsatisfied container condition means the whole
                // prohibition is not satisfied
                if (inter) {
                    break;
                }
            }
        }

        return addOps;
    }

}
