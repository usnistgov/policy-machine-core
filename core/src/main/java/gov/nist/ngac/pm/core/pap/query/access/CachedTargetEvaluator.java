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

package gov.nist.ngac.pm.core.pap.query.access;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.model.context.AnonymousTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A {@link TargetEvaluator} that caches privileges across evaluations for the same user, clearing the
 * cache when the user context changes.
 */
public class CachedTargetEvaluator extends TargetEvaluator {

    private UserDagResult cachedUserDagResult;
    private final Map<Long, Map<Long, AccessRightSet>> cachedVisitedNodes;

    public CachedTargetEvaluator(PolicyStore policyStore) {
        super(policyStore);
        this.cachedVisitedNodes = new Long2ObjectOpenHashMap<>();
    }

    @Override
    public TargetDagResult evaluate(UserDagResult userDagResult, TargetContext targetContext) throws PMException {
        // If UserDagResult has changed, clear the cache
        if (cachedUserDagResult == null || !cachedUserDagResult.equals(userDagResult)) {
            cachedUserDagResult = userDagResult;
            cachedVisitedNodes.clear();
        }

        return super.evaluate(userDagResult, targetContext);
    }

    @Override
    protected TraversalState initializeEvaluationState(UserDagResult userDagResult, TargetContext targetCtx) throws PMException {
        Collection<Long> firstLevelDescs = new LongArrayList();
        Collection<Long> resolvedIds = targetCtx.resolveNodeIds(policyStore.graph());

        if (targetCtx instanceof NodeTargetContext) {
            long id = resolvedIds.iterator().next();
            firstLevelDescs.addAll(policyStore.graph().getAdjacentDescendants(id));
        } else {
            // AnonymousTargetContext: attribute ids are themselves the starting points
            firstLevelDescs.addAll(resolvedIds);
        }

        Set<Long> userProhibitionTargets = collectUserProhibitionAttributes(userDagResult.prohibitions());
        Set<Long> visitedProhibitionTargets = new LongOpenHashSet();

        // Use cached visitedNodes instead of creating a new one
        return new TraversalState(
            firstLevelDescs,
            userProhibitionTargets,
            cachedVisitedNodes,
            visitedProhibitionTargets
        );
    }
}
