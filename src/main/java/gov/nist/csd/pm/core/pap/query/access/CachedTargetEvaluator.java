package gov.nist.csd.pm.core.pap.query.access;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessrights.AccessRightSet;
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
        if (targetCtx.isNode()) {
            firstLevelDescs.addAll(policyStore.graph().getAdjacentDescendants(targetCtx.getTargetId()));
        } else {
            firstLevelDescs.addAll(targetCtx.getAttributeIds());
        }

        Set<Long> userProhibitionTargets = collectUserProhibitionTargets(userDagResult.prohibitions());
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
