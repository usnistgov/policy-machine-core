package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.pap.query.GraphQuery;
import gov.nist.csd.pm.pap.query.model.subgraph.AscendantSubgraph;
import gov.nist.csd.pm.pap.query.model.subgraph.DescendantSubgraph;
import gov.nist.csd.pm.pap.store.PolicyStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GraphQuerier extends Querier implements GraphQuery {

    public GraphQuerier(PolicyStore store) {
        super(store);
    }

    @Override
    public Node getNodeByName(String name) throws PMException {
        checkNodeExists(name);
        return store.graph().getNodeByName(name);
    }

    @Override
    public long getNodeId(String name) throws PMException {
        checkNodeExists(name);
        return store.graph().getNodeByName(name).getId();
    }

    @Override
    public Node getNodeById(long id) throws PMException {
        checkNodeExists(id);
        return store.graph().getNodeById(id);
    }

    @Override
    public long[] getAdjacentDescendants(long nodeId) throws PMException {
        checkNodeExists(nodeId);
        return store.graph().getAdjacentDescendants(nodeId);
    }

    @Override
    public long[] getAdjacentAscendants(long nodeId) throws PMException {
        checkNodeExists(nodeId);
        return store.graph().getAdjacentAscendants(nodeId);
    }

    @Override
    public Collection<Association> getAssociationsWithSource(long uaId) throws PMException {
        checkNodeExists(uaId);
        return List.of(store.graph().getAssociationsWithSource(uaId));
    }

    @Override
    public Collection<Association> getAssociationsWithTarget(long targetId) throws PMException {
        checkNodeExists(targetId);
        return List.of(store.graph().getAssociationsWithTarget(targetId));
    }

    @Override
    public AscendantSubgraph getAscendantSubgraph(long nodeId) throws PMException {
        checkNodeExists(nodeId);
        return store.graph().getAscendantSubgraph(nodeId);
    }

    @Override
    public DescendantSubgraph getDescendantSubgraph(long nodeId) throws PMException {
        checkNodeExists(nodeId);
        return store.graph().getDescendantSubgraph(nodeId);
    }

    @Override
    public long[] getAttributeDescendants(long nodeId) throws PMException {
        checkNodeExists(nodeId);
        return store.graph().getAttributeDescendants(nodeId);
    }

    @Override
    public long[] getPolicyClassDescendants(long nodeId) throws PMException {
        checkNodeExists(nodeId);
        return store.graph().getPolicyClassDescendants(nodeId);
    }

    @Override
    public boolean isAscendant(long ascendantId, long descendantId) throws PMException {
        checkNodeExists(ascendantId);
        checkNodeExists(descendantId);
        return store.graph().isAscendant(ascendantId, descendantId);
    }

    @Override
    public boolean isDescendant(long ascendantId, long descendantId) throws PMException {
        checkNodeExists(ascendantId);
        checkNodeExists(descendantId);
        return store.graph().isDescendant(ascendantId, descendantId);
    }

    @Override
    public boolean nodeExists(long id) throws PMException {
        return store.graph().nodeExists(id);
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        return store.graph().nodeExists(name);
    }

    @Override
    public Collection<Node> search(NodeType type, Map<String, String> properties) throws PMException {
        long[] search = store.graph().search(type, properties);

        List<Node> nodes = new ArrayList<>();
        for (long nodeId : search) {
            nodes.add(store.graph().getNodeById(nodeId));
        }

        return nodes;
    }

    @Override
    public long[] getPolicyClasses() throws PMException {
        return store.graph().getPolicyClasses();
    }

    /**
     * Check that the given nodes exists.
     * @param node The node to check.
     */
    protected void checkNodeExists(long node) throws PMException {
        if (!store.graph().nodeExists(node)) {
            throw new NodeDoesNotExistException(node);
        }
    }

    /**
     * Check that the given nodes exists.
     * @param node The node to check.
     */
    protected void checkNodeExists(String node) throws PMException {
        if (!store.graph().nodeExists(node)) {
            throw new NodeDoesNotExistException(node);
        }
    }
}
