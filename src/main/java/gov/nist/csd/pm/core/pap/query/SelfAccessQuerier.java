package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class SelfAccessQuerier extends Querier implements SelfAccessQuery{

    public SelfAccessQuerier(PolicyStore store) {
        super(store);
    }

    @Override
    public AccessRightSet computePrivileges(TargetContext targetCtx) throws PMException {
        return new AccessRightSet();
    }

    @Override
    public List<AccessRightSet> computePrivileges(List<TargetContext> targetCtxs) throws PMException {
        return IntStream.range(0, targetCtxs.size())
            .mapToObj(i -> new AccessRightSet())
            .toList();
    }

    @Override
    public AccessRightSet computeDeniedPrivileges(TargetContext targetCtx) throws PMException {
        return new AccessRightSet();
    }

    @Override
    public SubgraphPrivileges computeSubgraphPrivileges(long root) throws PMException {
        List<SubgraphPrivileges> subgraphs = new ArrayList<>();

        Collection<Long> adjacentAscendants = store.graph().getAdjacentAscendants(root);
        for (long adjacent : adjacentAscendants) {
            subgraphs.add(computeSubgraphPrivileges(adjacent));
        }

        return new SubgraphPrivileges(
            store.graph().getNodeById(root),
            new AccessRightSet(),
            subgraphs
        );
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentAscendantPrivileges(long root) throws PMException {
        Map<Node, AccessRightSet> map = new HashMap<>();

        Collection<Long> adjacentAscendants = store.graph().getAdjacentAscendants(root);
        for (Long adjAsc : adjacentAscendants) {
            Node node = store.graph().getNodeById(adjAsc);
            map.put(node, new AccessRightSet());
        }

        return map;
    }

    @Override
    public Map<Node, AccessRightSet> computeAdjacentDescendantPrivileges(long root) throws PMException {
        Map<Node, AccessRightSet> map = new HashMap<>();

        Collection<Long> adjacentAscendants = store.graph().getAdjacentDescendants(root);
        for (Long adjAsc : adjacentAscendants) {
            Node node = store.graph().getNodeById(adjAsc);
            map.put(node, new AccessRightSet());
        }

        return map;
    }

    @Override
    public Map<Node, AccessRightSet> computePersonalObjectSystem() throws PMException {
        return Map.of();
    }
}
