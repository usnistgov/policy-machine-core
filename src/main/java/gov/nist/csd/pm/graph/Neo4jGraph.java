package gov.nist.csd.pm.graph;

import gov.nist.csd.pm.db.DatabaseContext;
import gov.nist.csd.pm.db.neo4j.Neo4jConnection;
import gov.nist.csd.pm.db.neo4j.Neo4jHelper;
import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMGraphException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.graph.search.Neo4jSearch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * A Neo4j implementation of a NGAC graph
 */
public class Neo4jGraph implements Graph {

    /**
     * Object to store a connection to a Neo4j database.
     */
    private Neo4jConnection neo4j;

    /**
     * Store the database connection parameters.
     */
    private DatabaseContext dbCtx;

    /**
     * Receive context information about the database connection, and create a new connection to the Neo4j instance.
     * @param ctx Context information about the Neo4j connection.
     * @throws PMDBException When there is an error connecting to Neo4j.
     */
    public Neo4jGraph(DatabaseContext ctx) throws PMDBException {
        this.dbCtx = ctx;
        this.neo4j = new Neo4jConnection(ctx.getHost(), ctx.getPort(), ctx.getUsername(), ctx.getPassword());

        // create an index on node IDs
        // this will improve read performance
        String cypher = "create index on :NODE(id)";
        try (
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher)
        ) {
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Create a new node with the information provided in node. The ID is a random long value.
     *
     * @param node the context of the node to create.  This includes the id, name, type, and properties.
     * @return the ID of the created node.
     * @throws IllegalArgumentException if the node is null.
     * @throws IllegalArgumentException if the node name is null or empty.
     * @throws IllegalArgumentException if the node type is null.
     * @throws PMDBException if the provided node is null.
     */
    @Override
    public long createNode(NodeContext node) throws PMDBException {
        if (node == null) {
            throw new IllegalArgumentException("a null node was provided when creating a node in neo4j");
        } else if(node.getName() == null || node.getName().isEmpty()) {
            throw new IllegalArgumentException("a null name was provided when creating a new node");
        }else if(node.getType() == null) {
            throw new IllegalArgumentException("a null type was provided when creating a new node");
        }

        // if the node properties are null, initialize to an empty map
        if(node.getProperties() == null) {
            node.properties(new HashMap<>());
        }

        // generate a random ID
        long id = new Random().nextLong();

        String cypher = String.format("create(n:NODE:%s{id: %d, name: '%s', type: '%s'})",
                node.getType(), id, node.getName(), node.getType());

        // build a string for the node's properties
        StringBuilder propStr = new StringBuilder();
        for (String key : node.getProperties().keySet()) {
            if (propStr.length() == 0) {
                propStr.append(String.format("%s: '%s'", key, node.getProperties().get(key)));
            }
            else {
                propStr.append(String.format(", %s: '%s'", key, node.getProperties().get(key)));
            }
        }
        cypher += String.format(" set n += {%s}", propStr.toString());

        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher)
        ) {
            stmt.executeQuery();
            return id;
        } catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Update a node based on the given node context.  Only name and properties can be updated.
     *
     * @param node the context of the node to update. This includes the id, name, type, and properties.
     * @throws IllegalArgumentException if the provided node to update is null.
     * @throws IllegalArgumentException if the provided node to update has an ID of 0.
     * @throws PMDBException if there is an error updating the node in Neo4j.
     */
    @Override
    public void updateNode(NodeContext node) throws PMDBException, PMGraphException {
        if(node == null) {
            throw new IllegalArgumentException("a null node was provided when updating a node in neo4j");
        } else if(node.getID() == 0) {
            //throw an exception if the provided context does not have an ID
            throw new IllegalArgumentException("no ID was provided when updating a node in neo4j");
        }

        NodeContext exNode = new Neo4jSearch(dbCtx).getNode(node.getID());
        // check if changing the name, if not, give the node the existing name
        if (node.getName() == null || node.getName().isEmpty()) {
            node.name(exNode.getName());
        }

        // set the type of the updated node
        node.type(exNode.getType());

        String cypher = String.format("match(n:NODE{id:%d}) set n={} ", node.getID());

        // have to reset the ID etc because neo4j will erase all properties
        // build a string for the node's name and properties
        StringBuilder propStr = new StringBuilder();
        propStr.append(String.format("id: %s", node.getID()));
        propStr.append(String.format(", name: '%s'", node.getName()));
        propStr.append(String.format(", type: '%s'", node.getType()));

        if (node.getProperties() != null) {
            for (String key : node.getProperties().keySet()) {
                if (propStr.length() == 0) {
                    propStr.append(String.format("%s: '%s'", key, node.getProperties().get(key)));
                }
                else {
                    propStr.append(String.format(", %s: '%s'", key, node.getProperties().get(key)));
                }
            }
        }

        // if name or properties are being updated, send to Neo4j
        if (propStr.length() > 0) {
            cypher += String.format(" set n += {%s}", propStr.toString());

            try (
                    Connection conn = neo4j.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(cypher)
            ) {
                stmt.executeQuery();
            }
            catch (SQLException e) {
                throw new PMDBException(e.getMessage());
            }
        }
    }

    /**
     * Delete a node from the graph.
     *
     * @param nodeID the ID of the node to delete.
     * @throws PMDBException if there is an error deleting the node from the database.
     */
    @Override
    public void deleteNode(long nodeID) throws PMDBException {
        String cypher = String.format("MATCH (n) where n.id=%d DETACH DELETE n", nodeID);
        try (
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher)
        ) {
            stmt.executeQuery();
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Check if a node with the given ID exists in the database.
     *
     * @param nodeID the ID of the node to check for.
     * @return true if a node with the given ID exists, false otherwise.
     * @throws PMDBException if there is an error check if the node exists in the database.
     */
    @Override
    public boolean exists(long nodeID) throws PMDBException {
        String cypher = String.format("match(n{id: %d}) return n", nodeID);
        try (
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
                ResultSet rs = stmt.executeQuery()
        ) {
            return rs.next();
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Get the policy class nodes in the graph.
     *
     * @return the set of policy class node IDs.
     * @throws PMDBException if there is an error getting the policy classes from the database.
     */
    @Override
    public HashSet<Long> getPolicies() throws PMDBException {
        String cypher = "match(n) where n:PC return n.id";
        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
                ResultSet rs = stmt.executeQuery()
        ) {
            HashSet<Long> nodeIDs = new HashSet<>();
            while (rs.next()) {
                nodeIDs.add(rs.getLong(1));
            }
            return nodeIDs;
        } catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Get the children of the node with the given ID.
     *
     * @param nodeID the ID of the node to get the children of.
     * @return the set of nodes that are assigned to the node with the given ID.
     * @throws PMGraphException if there is an error converting the ResultSet returned from the database to a set of Nodes.
     * @throws PMDBException if there is an error getting the children of the provided node from the database.
     */
    @Override
    public HashSet<NodeContext> getChildren(long nodeID) throws PMDBException, PMGraphException {
        String cypher = String.format("match(n{id:%d})<-[:assigned_to]-(m) return m", nodeID);
        try (
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
                ResultSet rs = stmt.executeQuery()
        ) {
            return Neo4jHelper.getNodesFromResultSet(rs);
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Get the parents of the node with the given ID.
     *
     * @param nodeID the ID of the node to get the children of.
     * @return the set of nodes that are assigned to the node with the given ID.
     * @throws PMGraphException if there is an error converting the ResultSet returned from the database to a set of Nodes.
     * @throws PMDBException if there is an error getting the parents of the provided node from the database.
     */
    @Override
    public HashSet<NodeContext> getParents(long nodeID) throws PMDBException, PMGraphException {
        String cypher = String.format("match(n{id:%d})-[:assigned_to]->(m) return m", nodeID);
        try (
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
                ResultSet rs = stmt.executeQuery()
        ) {
            return Neo4jHelper.getNodesFromResultSet(rs);
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Assign the child node to the parent node.
     *
     * @param childCtx the ID and type of the child node.
     * @param parentCtx the ID and type of the parent node.
     * @throws PMDBException if there is an error assigning the child to the parent in the database.
     * @throws IllegalArgumentException if the child node is null.
     * @throws IllegalArgumentException if the parent node is null.
     * @throws IllegalArgumentException if the child node type is null.
     * @throws IllegalArgumentException if the parent node type is null.
     */
    @Override
    public void assign(NodeContext childCtx, NodeContext parentCtx) throws PMDBException {
        if(childCtx == null) {
            throw new IllegalArgumentException("child node context was null");
        } else if (parentCtx == null) {
            throw new IllegalArgumentException("parent node context was null");
        } else if(childCtx.getType() == null) {
            throw new IllegalArgumentException("a null type was provided for the child of the assignment to create.");
        } else if (parentCtx.getType() == null) {
            throw new IllegalArgumentException("a null type was provided for the parent of the assignment to create.");
        }

        String cypher = String.format("MATCH (a:%s{id: %d}), (b:%s{id: %d}) " +
                        "CREATE (a)-[:assigned_to]->(b)", childCtx.getType(), childCtx.getID(),
                parentCtx.getType(), parentCtx.getID());
        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher)
        ){
            stmt.executeQuery();
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Deassign the child node from the parent node.
     * @param childCtx the context information for the child of the assignment to delete.
     * @param parentCtx the context information for the parent of the assignment to delete.
     * @throws PMDBException if there is an error deleting this assignment in the database.
     * @throws IllegalArgumentException if the child node is null.
     * @throws IllegalArgumentException if the parent node is null.
     * @throws IllegalArgumentException if the child node type is null.
     * @throws IllegalArgumentException if the parent node type is null.
     */
    @Override
    public void deassign(NodeContext childCtx, NodeContext parentCtx) throws PMDBException {
        if(childCtx == null) {
            throw new IllegalArgumentException("child node context was null");
        } else if (parentCtx == null) {
            throw new IllegalArgumentException("parent node context was null");
        } else if(childCtx.getType() == null) {
            throw new IllegalArgumentException("a null type was provided for the child of the assignment to delete.");
        } else if (parentCtx.getType() == null) {
            throw new IllegalArgumentException("a null type was provided for the parent of the assignment to delete.");
        }

        String cypher = String.format("match (a:%s{id: %d})-[r:assigned_to]->(b:%s{id: %d}) delete r",
                childCtx.getType(), childCtx.getID(), parentCtx.getType(), parentCtx.getID());
        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher)
        ){
            stmt.executeQuery();
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Create an association between the user attribute and the target node. If an association already exists, update
     * the operations to the ones provided.
     * @param uaCtx the context information for the user attribute of the association
     * @param targetCtx the context information for the target of the association.
     * @param operations A Set of operations to add to the Association.
     * @throws IllegalArgumentException if the user attribute node context is null.
     * @throws IllegalArgumentException if the target node context is null.
     * @throws IllegalArgumentException if the target node type is null.
     * @throws PMDBException if there is an error associating the two nodes in the database.
     */
    @Override
    public void associate(NodeContext uaCtx, NodeContext targetCtx, HashSet<String> operations) throws PMDBException {
        if(uaCtx == null) {
            throw new IllegalArgumentException("user attribute node context was null");
        } else if (targetCtx == null) {
            throw new IllegalArgumentException("target node context was null");
        } else if(targetCtx.getType() == null) {
            throw new IllegalArgumentException("a null type was provided for the target of the association to create.");
        }

        String operationsStr = Neo4jHelper.setToCypherArray(operations);
        String cypher = String.format("MATCH (ua:UA{id: %d}), (target:%s{id: %d}) " +
                        "merge (ua)-[a:associated_with]->(target) set a.operations = %s", uaCtx.getID(), targetCtx.getType(),
                targetCtx.getID(), operationsStr);
        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher)
        ) {
            stmt.executeQuery();
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Delete an association between the user attribute and target node.
     * @param uaCtx the information for the user attribute.
     * @param targetCtx the information for the target node.
     * @throws IllegalArgumentException if the user attribute node context is null.
     * @throws IllegalArgumentException if the target node context is null.
     * @throws IllegalArgumentException if the target node type is null.
     * @throws PMDBException if there is an error dissociating the two nodes in the database.
     */
    @Override
    public void dissociate(NodeContext uaCtx, NodeContext targetCtx) throws PMDBException {
        if(uaCtx == null) {
            throw new IllegalArgumentException("user attribute node context was null");
        } else if (targetCtx == null) {
            throw new IllegalArgumentException("target node context was null");
        } else if(targetCtx.getType() == null) {
            throw new IllegalArgumentException("a null type was provided for the target of the association to delete.");
        }

        String cypher = String.format("match (ua:UA{id:%d})-[r:associated_with]->(target%s{id:%d}) delete r", uaCtx.getID(),
                targetCtx.getType(), targetCtx.getID());
        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher)
        ) {
            stmt.executeQuery();
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Get the associations that the provided node is the source of. Note: Only user attributes can be source nodes in
     * an association.
     *
     * @param sourceID the ID of the source node.
     * @return a map of target node IDs and operations given to the source node for each association.
     * @throws PMDBException if there is an exception retrieving the associations for the source node in the database.
     */
    @Override
    public HashMap<Long, HashSet<String>> getSourceAssociations(long sourceID) throws PMDBException {
        String cypher = String.format("match(source:UA{id:%d})-[a:associated_with]->(target) return target.id, a.operations", sourceID);
        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
                ResultSet rs = stmt.executeQuery()
        ) {
            HashMap<Long, HashSet<String>> associations = new HashMap<>();
            while (rs.next()) {
                long targetID = rs.getLong(1);
                HashSet<String> opsSet = new HashSet<>((Collection<String>) rs.getObject(2));
                associations.put(targetID, opsSet);
            }

            return associations;
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Get the associations that the provided node is the target of. Note: Only user attributes and Object Attributes
     * can be target nodes in an association.
     *
     * @param targetID the ID of the target node.
     * @return a map of source node IDs and operations the source nodes have on the given target ID through each association.
     * @throws PMDBException if there is an exception retrieving the associations for the target node in the database.
     */
    @Override
    public HashMap<Long, HashSet<String>> getTargetAssociations(long targetID) throws PMDBException {
        String cypher = String.format("match(source)-[a:associated_with]->(target{id:%d}) return source.id, a.operations", targetID);
        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
                ResultSet rs = stmt.executeQuery()
        ) {
            HashMap<Long, HashSet<String>> associations = new HashMap<>();
            while (rs.next()) {
                long sourceID = rs.getLong(1);
                HashSet<String> opsSet = new HashSet<>((Collection) rs.getObject(2));
                associations.put(sourceID, opsSet);
            }

            return associations;
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }
}
