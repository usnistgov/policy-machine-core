package gov.nist.csd.pm.graph.search;

import gov.nist.csd.pm.db.DatabaseContext;
import gov.nist.csd.pm.db.neo4j.Neo4jConnection;
import gov.nist.csd.pm.db.neo4j.Neo4jHelper;
import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.exceptions.PMGraphException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static gov.nist.csd.pm.db.neo4j.Neo4jHelper.mapToNode;

/**
 * Neo4j extension of the Search class.
 */
public class Neo4jSearch implements Search {

    /**
     * Object to hold connection to Neo4j instance.
     */
    protected Neo4jConnection neo4j;

    public Neo4jSearch(DatabaseContext ctx) throws PMDBException {
        this.neo4j = new Neo4jConnection(ctx.getHost(), ctx.getPort(), ctx.getUsername(), ctx.getPassword());
    }

    public Neo4jSearch(Neo4jConnection neo4j) {
        this.neo4j = neo4j;
    }

    /**
     * Search the neo4j database for nodes based on name, type, and properties. This implementation does not support
     * wildcard searching.
     * @param name the name of the nodes to search for.
     * @param type the type of the nodes to search for.
     * @param properties the properties of the nodes to search for.
     * @return the set of nodes that match the search parameters.
     * @throws PMDBException if there is an error retrieving the nodes from the database.
     * @throws PMGraphException if there is an error converting the ResultSet to a set of nodes.
     */
    @Override
    public HashSet<NodeContext> search(String name, String type, Map<String, String> properties) throws PMDBException, PMGraphException {
        // get the cypher query
        String cypher = getSearchCypher(name, type, properties);
        // query neo4j for the nodes
        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
                ResultSet rs = stmt.executeQuery()
        ) {
            return Neo4jHelper.getNodesFromResultSet(rs);
        } catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Retrieve all the nodes from the database.
     *
     * @return the set of all nodes in the database.
     * @throws PMDBException if there is an error retrieving the nodes from the database.
     */
    @Override
    public HashSet<NodeContext> getNodes() throws PMDBException, PMGraphException {
        String cypher = "match(n:NODE) return n";
        try (
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
                ResultSet rs = stmt.executeQuery()
        ) {
            HashSet<NodeContext> nodes = new HashSet<>();
            while (rs.next()) {
                HashMap map = (HashMap) rs.getObject(1);
                NodeContext node = mapToNode(map);
                if(node == null) {
                    throw new PMGraphException("received a null node from neo4j");
                }

                nodes.add(node);
            }
            return nodes;
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Get the node from the graph with the given ID.
     * @param id the ID of the node to get.
     * @return a NodeContext with the information of the node with the given ID.
     * @throws PMDBException if there is an error retrieving the node from the database.
     * @throws PMGraphException if there is an error converting the data returned from the database into a node.
     */
    @Override
    public NodeContext getNode(long id) throws PMDBException, PMGraphException {
        // get the cypher query
        String cypher = String.format("match(n{id:%d}) return n", id);
        // query neo4j for the nodes
        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
                ResultSet rs = stmt.executeQuery()
        ) {
            if(rs.next()) {
                HashMap map = (HashMap) rs.getObject(1);
                return mapToNode(map);
            }
            throw new PMGraphException(String.format("node with ID %d does not exist", id));
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    private static String getSearchCypher(String name, String type, Map<String,String> properties) {
        String propsStr = "";
        if (name != null && !name.isEmpty()){
            propsStr = String.format("name: \"%s\"", name);
        }

        String typeStr = "";
        if (type != null && !type.isEmpty()){
            typeStr = String.format(":%s", type);
        }

        if (properties != null) {
            for (String key : properties.keySet()) {
                String value = properties.get(key);
                if (propsStr.isEmpty()) {
                    propsStr += String.format("%s: \"%s\"", key, value);
                } else {
                    propsStr += String.format(", %s: \"%s\"", key, value);
                }
            }
        }

        return String.format("match(n%s{%s}) return n", typeStr, propsStr);
    }
}
