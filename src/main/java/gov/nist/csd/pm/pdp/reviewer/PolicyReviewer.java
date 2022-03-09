package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.policy.events.PolicyEventListener;
import gov.nist.csd.pm.policy.model.graph.dag.TargetDagResult;
import gov.nist.csd.pm.policy.model.graph.dag.UserDagResult;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.*;

import gov.nist.csd.pm.policy.review.PolicyReview;
import gov.nist.csd.pm.policy.tx.Transactional;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;

public abstract class PolicyReviewer implements PolicyReview, PolicyEventListener, Transactional {

    public AccessRightSet resolvePermissions(UserDagResult userContext, TargetDagResult targetCtx, String target, AccessRightSet resourceOps) {
        AccessRightSet allowed = resolveAllowedPermissions(targetCtx.pcSet(), resourceOps);

        // remove any prohibited operations
        Set<String> denied = resolveProhibitions(userContext, targetCtx, target);
        allowed.removeAll(denied);

        return allowed;
    }

    public AccessRightSet resolveAllowedPermissions(Map<String, AccessRightSet> pcMap, AccessRightSet resourceOps) {
        Map<String, AccessRightSet> resolvedPcMap = new HashMap<>();
        for (String pc : pcMap.keySet()) {
            AccessRightSet pcOps = pcMap.get(pc);

            // replace instances of *, *a or *r with the literal operations
            resolveSpecialPermissions(pcOps, resourceOps);

            resolvedPcMap.put(pc, pcOps);
        }

        return resolvePolicyClassOperationSets(resolvedPcMap);
    }

    public AccessRightSet resolvePolicyClassOperationSets(Map<String, AccessRightSet> pcMap) {
        // retain only the ops that the decider knows about
        AccessRightSet allowed = new AccessRightSet();
        boolean first = true;
        for (String pc : pcMap.keySet()) {
            Set<String> ops = pcMap.get(pc);
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

    public void resolveSpecialPermissions(AccessRightSet permissions, AccessRightSet resourceOps) {
        // if the permission set includes *, remove the * and add all resource operations
        if (permissions.contains(ALL_ACCESS_RIGHTS)) {
            permissions.remove(ALL_ACCESS_RIGHTS);
            permissions.addAll(ALL_ADMIN_ACCESS_RIGHTS_SET);
            permissions.addAll(resourceOps);
        } else {
            // if the permissions includes *a or *r add all the admin ops/resource ops as necessary
            if (permissions.contains(ALL_ADMIN_ACCESS_RIGHTS)) {
                permissions.remove(ALL_ADMIN_ACCESS_RIGHTS);
                permissions.addAll(ALL_ADMIN_ACCESS_RIGHTS_SET);
            }
            if (permissions.contains(ALL_RESOURCE_ACCESS_RIGHTS)) {
                permissions.remove(ALL_RESOURCE_ACCESS_RIGHTS);
                permissions.addAll(resourceOps);
            }
        }
    }

    public AccessRightSet resolveProhibitions(UserDagResult userCtx, TargetDagResult targetCtx, String target) {
        AccessRightSet denied = new AccessRightSet();
        Set<Prohibition> prohibitions = userCtx.prohibitions();
        Set<String> reachedTargets = targetCtx.reachedTargets();

        for(Prohibition p : prohibitions) {
            boolean inter = p.isIntersection();
            List<ContainerCondition> containers = p.getContainers();
            boolean addOps = false;

            for (ContainerCondition containerCondition : containers) {
                String contName = containerCondition.name();
                boolean isComplement = containerCondition.complement();

                if (target.equals(contName)) {
                    // if the prohibition is UNION and the target is the container then the prohibition is satisfied
                    // if the prohibition is INTERSECTION and the target is the container then the prohibition is not satisfied
                    if (!inter) {
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

            if (addOps) {
                denied.addAll(p.getAccessRightSet());
            }
        }
        return denied;
    }
}
