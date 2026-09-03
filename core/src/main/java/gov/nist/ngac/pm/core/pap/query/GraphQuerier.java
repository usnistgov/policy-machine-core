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

package gov.nist.ngac.pm.core.pap.query;

import gov.nist.ngac.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.query.model.subgraph.Subgraph;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A {@link GraphQuery} implementation backed by the policy store's graph store.
 */
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
     * Checks that the given node exists.
     *
     * @param node the node to check
     */
    protected void checkNodeExists(long node) throws PMException {
        if (!store.graph().nodeExists(node)) {
            throw new NodeDoesNotExistException(node);
        }
    }

    /**
     * Checks that the given node exists.
     *
     * @param node the node to check
     */
    protected void checkNodeExists(String node) throws PMException {
        if (!store.graph().nodeExists(node)) {
            throw new NodeDoesNotExistException(node);
        }
    }
}
