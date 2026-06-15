package gov.nist.csd.pm.core.pap.query.access;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.AnonymousTargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

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
        Collection<Long> resolvedIds = targetCtx.resolveNodeIds(policyStore.graph()::getNodeByName);

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
