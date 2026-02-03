package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.pap.query.model.subgraph.Subgraph;
import java.util.Collection;
import java.util.Map;

/**
 * Interface to query the graph.
 */
public interface GraphQuery {

    /**
     * Check if a node exists in the graph with the given ID.
     *
     * @param id The ID of the node to check for.
     * @return True if the node exists, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    boolean nodeExists(long id) throws PMException;

    /**
     * Check if a node exists in the graph.
     *
     * @param name The name of the node to check for.
     * @return True if the node exists, false otherwise.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    boolean nodeExists(String name) throws PMException;

    /**
     * Get the Node object associated with the given name.
     *
     * @param name The name of the node to get.
     * @return The Node with the given name.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Node getNodeByName(String name) throws PMException;

    /**
     * Get the ID of the node with the given name.
     *
     * @param name The name of the node to get the ID for.
     * @return The ID of the node with the given name.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    long getNodeId(String name) throws PMException;

    /**
     * Get the Node object with the given id.
     *
     * @param id The ID of the node to get.
     * @return The Node with the given ID.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Node getNodeById(long id) throws PMException;

    /**
     * Search for nodes with the given type and/or properties. To return all nodes, use type=NodeType.ANY and properties=new HashMap<>().
     * <p>
     * Supports wildcard property values i.e. {"prop1": "*"} which will match any nodes with the "prop1" property key.
     *
     * @param type       The type of nodes to search for. Use NodeType.ANY to search for any node type.
     * @param properties The properties of nodes to search for. An empty map will match all nodes.
     * @return The nodes that match the type and property criteria.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Node> search(NodeType type, Map<String, String> properties) throws PMException;

    /**
     * Get all policy class IDs.
     *
     * @return The IDs of all policy classes.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Long> getPolicyClasses() throws PMException;

    /**
     * Get the adjacent descendants of the given node.
     *
     * @param nodeId The node to get the descendants of.
     * @return The IDs of the descendants of the given node.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Long> getAdjacentDescendants(long nodeId) throws PMException;

    /**
     * Get the adjacent ascendants of the given node.
     *
     * @param nodeId The node to get the ascendants of.
     * @return The IDs of the ascendants of the given node.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Long> getAdjacentAscendants(long nodeId) throws PMException;

    /**
     * Get the associations in which the given user attribute is the source.
     *
     * @param uaId The user attribute to get the associations for.
     * @return The associations in which the source of the relation is the given user attribute.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Association> getAssociationsWithSource(long uaId) throws PMException;

    /**
     * Get the associations in which the given node is the target.
     *
     * @param targetId The target attribute to get the associations for.
     * @return The associations in which the target of the relation is the given node.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Association> getAssociationsWithTarget(long targetId) throws PMException;

    /**
     * Get the recursive structure of all ascendants of the given node.
     *
     * @param nodeId The node to get the ascendants for.
     * @return The ascendants of the given node.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Subgraph getAscendantSubgraph(long nodeId) throws PMException;

    /**
     * Get the recursive structure of all descendants of the given node.
     *
     * @param nodeId The node to get the descendants for.
     * @return The recursive descendants of the given node.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Subgraph getDescendantSubgraph(long nodeId) throws PMException;

    /**
     * Get the descendants of the given node that are attributes.
     *
     * @param nodeId The node to get the attribute descendants of.
     * @return A Collection of attribute IDs.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Long> getAttributeDescendants(long nodeId) throws PMException;

    /**
     * Get the descendants of the given node that are policy classes.
     *
     * @param nodeId The node to get the policy class descendants of.
     * @return A Collection of policy class IDs.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    Collection<Long> getPolicyClassDescendants(long nodeId) throws PMException;

    /**
     * Return true if the ascendant is an ascendant of the descendant.
     *
     * @param ascendantId  The ascendant.
     * @param descendantId The descendant.
     * @return True if the ascendant is an ascendant of the descendant.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    boolean isAscendant(long ascendantId, long descendantId) throws PMException;

    /**
     * Return true if the descendant is a descendant of the ascendant.
     *
     * @param ascendantId  The ascendant.
     * @param descendantId The descendant.
     * @return True if the descendant is a descendant of the ascendant.
     * @throws PMException If any PM related exceptions occur in the implementing class.
     */
    boolean isDescendant(long ascendantId, long descendantId) throws PMException;
}
