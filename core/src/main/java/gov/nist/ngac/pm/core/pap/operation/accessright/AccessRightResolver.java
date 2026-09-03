/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.operation.accessright;

import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.query.access.TargetDagResult;
import gov.nist.ngac.pm.core.pap.query.access.UserDagResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Combines a {@link UserDagResult} and a {@link TargetDagResult} into the final access decision.
 */
public class AccessRightResolver {

    private AccessRightResolver() {}

    /**
     * Resolves the access rights a user has on a target.
     *
     * @param userCtx the user's DAG evaluation result
     * @param targetCtx the target's DAG evaluation result
     * @param resourceAccessRights the full set of resource access rights, used to expand wildcards
     * @return the resolved, granted access rights
     */
    public static AccessRightSet resolvePrivileges(UserDagResult userCtx, TargetDagResult targetCtx, AccessRightSet resourceAccessRights) {
        // resolve any access rights with "*" to their enumerated rights
        Map<Long, AccessRightSet> resolvedPcMap = resolvePcMap(targetCtx.pcMap(), resourceAccessRights);

        // determine the common set of access rights across all policy classes
        AccessRightSet result = resolvePolicyClassAccessRightSets(resolvedPcMap);

        // remove any prohibited access rights
        AccessRightSet denied = resolveDeniedAccessRights(userCtx.prohibitions(), targetCtx);
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

    /**
     * Returns the access rights denied by the given prohibitions that are satisfied against the target's
     * reached nodes.
     *
     * @param prohibitions the prohibitions to check
     * @param targetCtx the target's DAG evaluation result
     * @return the union of denied access rights
     */
    public static AccessRightSet resolveDeniedAccessRights(Set<Prohibition> prohibitions, TargetDagResult targetCtx) {
        AccessRightSet denied = new AccessRightSet();
        Set<Long> reachedTargets = targetCtx.reachedTargets();

        for(Prohibition p : prohibitions) {
            if (isProhibitionSatisfied(p, reachedTargets)) {
                denied.addAll(p.getAccessRightSet());
            }
        }

        return denied;
    }

    /**
     * Returns the user's prohibitions that are satisfied against the target's reached nodes.
     *
     * @param userDagResult the user's DAG evaluation result, supplying the prohibitions to check
     * @param targetDagResult the target's DAG evaluation result
     * @return the satisfied prohibitions
     */
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
