package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.common.graph.relationship.Association;
import gov.nist.csd.pm.core.pap.query.model.subgraph.Subgraph;
import gov.nist.csd.pm.core.pap.store.PolicyStore;

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
    public Collection<Long> getAdjacentDescendants(long nodeId) throws PMException {
        checkNodeExists(nodeId);
        return store.graph().getAdjacentDescendants(nodeId);
    }

    @Override
    public Collection<Long> getAdjacentAscendants(long nodeId) throws PMException {
        checkNodeExists(nodeId);
        return store.graph().getAdjacentAscendants(nodeId);
    }

    @Override
    public Collection<Association> getAssociationsWithSource(long uaId) throws PMException {
        checkNodeExists(uaId);
        return store.graph().getAssociationsWithSource(uaId);
    }

    @Override
    public Collection<Association> getAssociationsWithTarget(long targetId) throws PMException {
        checkNodeExists(targetId);
        return store.graph().getAssociationsWithTarget(targetId);
    }

    @Override
    public Subgraph getAscendantSubgraph(long nodeId) throws PMException {
        checkNodeExists(nodeId);
        return store.graph().getAscendantSubgraph(nodeId);
    }

    @Override
    public Subgraph getDescendantSubgraph(long nodeId) throws PMException {
        checkNodeExists(nodeId);
        return store.graph().getDescendantSubgraph(nodeId);
    }

    @Override
    public Collection<Long> getAttributeDescendants(long nodeId) throws PMException {
        checkNodeExists(nodeId);
        return store.graph().getAttributeDescendants(nodeId);
    }

    @Override
    public Collection<Long> getPolicyClassDescendants(long nodeId) throws PMException {
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
        Collection<Long> search = store.graph().search(type, properties);

        List<Node> nodes = new ArrayList<>();
        for (long nodeId : search) {
            nodes.add(store.graph().getNodeById(nodeId));
        }

        return nodes;
    }

    @Override
    public Collection<Long> getPolicyClasses() throws PMException {
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
