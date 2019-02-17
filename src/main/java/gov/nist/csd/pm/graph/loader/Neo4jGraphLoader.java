package gov.nist.csd.pm.graph.loader;

import gov.nist.csd.pm.db.DatabaseContext;
import gov.nist.csd.pm.db.neo4j.Neo4jConnection;
import gov.nist.csd.pm.db.neo4j.Neo4jHelper;
import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMGraphException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.graph.model.relationships.Assignment;
import gov.nist.csd.pm.graph.model.relationships.Association;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

/**
 * A Neo4j implementation of the GraphLoader interface.
 */
public class Neo4jGraphLoader implements GraphLoader {

    /**
     * Object to hold connection to Neo4j instance.
     */
    protected Neo4jConnection neo4j;

    /**
     * Create a new GraphLoader from Neo4j, using the provided database connection parameters.
     * @param ctx the parameters to connect to the database
     * @throws PMDBException if a connection cannot be made to the database.
     */
    public Neo4jGraphLoader(DatabaseContext ctx) throws PMDBException {
        neo4j = new Neo4jConnection(ctx.getHost(), ctx.getPort(), ctx.getUsername(), ctx.getPassword());
    }

    @Override
    public HashSet<NodeContext> getNodes() throws PMDBException, PMGraphException {
        String cypher = "match(n) where n:NODE return n";
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

    @Override
    public HashSet<Assignment> getAssignments() throws PMDBException {
        String cypher = "match(n)-[r:assigned_to]->(m) return n.id, m.id";
        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
                ResultSet rs = stmt.executeQuery()
        ) {
            HashSet<Assignment> assignments = new HashSet<>();
            while (rs.next()) {
                assignments.add(new Assignment(rs.getLong(1), rs.getLong(2)));
            }
            return assignments;
        } catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    @Override
    public HashSet<Association> getAssociations() throws PMDBException {
        String cypher = "match(ua:UA)-[a:associated_with]->(target:NODE) return ua.id,target.id,a.operations";
        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
                ResultSet rs = stmt.executeQuery()
        ) {
            HashSet<Association> associations = new HashSet<>();
            while (rs.next()) {
                Collection<String> ops = (Collection) rs.getObject(3);
                associations.add(new Association(rs.getLong(1), rs.getLong(2), new HashSet<>(ops)));
            }
            return associations;
        }  catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }
}
