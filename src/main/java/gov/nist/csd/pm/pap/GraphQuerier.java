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

import java.util.Collection;
import java.util.Map;

public class GraphQuerier extends Querier implements GraphQuery {

    public GraphQuerier(PolicyStore store) {
        super(store);
    }

    @Override
    public Node getNode(String name) throws PMException {
        checkNodeExists(name);
        return store.graph().getNode(name);
    }

    @Override
    public Collection<String> getAdjacentDescendants(String node) throws PMException {
        checkNodeExists(node);
        return store.graph().getAdjacentDescendants(node);
    }

    @Override
    public Collection<String> getAdjacentAscendants(String node) throws PMException {
        checkNodeExists(node);
        return store.graph().getAdjacentAscendants(node);
    }

    @Override
    public Collection<Association> getAssociationsWithSource(String ua) throws PMException {
        checkNodeExists(ua);
        return store.graph().getAssociationsWithSource(ua);
    }

    @Override
    public Collection<Association> getAssociationsWithTarget(String target) throws PMException {
        checkNodeExists(target);
        return store.graph().getAssociationsWithTarget(target);
    }

    @Override
    public AscendantSubgraph getAscendantSubgraph(String node) throws PMException {
        checkNodeExists(node);
        return store.graph().getAscendantSubgraph(node);
    }

    @Override
    public DescendantSubgraph getDescendantSubgraph(String node) throws PMException {
        checkNodeExists(node);
        return store.graph().getDescendantSubgraph(node);
    }

    @Override
    public Collection<String> getAttributeDescendants(String node) throws PMException {
        checkNodeExists(node);
        return store.graph().getAttributeDescendants(node);
    }

    @Override
    public Collection<String> getPolicyClassDescendants(String node) throws PMException {
        checkNodeExists(node);
        return store.graph().getPolicyClassDescendants(node);
    }

    @Override
    public boolean isAscendant(String ascendant, String descendant) throws PMException {
        checkNodeExists(ascendant);
        checkNodeExists(descendant);
        return store.graph().isAscendant(ascendant, descendant);
    }

    @Override
    public boolean isDescendant(String ascendant, String descendant) throws PMException {
        checkNodeExists(ascendant);
        checkNodeExists(descendant);
        return store.graph().isDescendant(ascendant, descendant);
    }

    @Override
    public boolean nodeExists(String name) throws PMException {
        return store.graph().nodeExists(name);
    }

    @Override
    public Collection<String> search(NodeType type, Map<String, String> properties) throws PMException {
        return store.graph().search(type, properties);
    }

    @Override
    public Collection<String> getPolicyClasses() throws PMException {
        return store.graph().getPolicyClasses();
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
