package gov.nist.csd.pm.prohibitions.loader;

import gov.nist.csd.pm.db.DatabaseContext;
import gov.nist.csd.pm.db.neo4j.Neo4jConnection;
import gov.nist.csd.pm.db.neo4j.Neo4jHelper;
import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMProhibitionException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.prohibitions.model.Prohibition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Neo4j implementation of the ProhibitionsLoader interface. Load prohibitions from a Neo4j instance into memory.
 */
public class Neo4jProhibitionsLoader implements ProhibitionsLoader {

    /**
     * Object to hold connection to Neo4j instance.
     */
    private Neo4jConnection neo4j;

    /**
     * Create a new ProhibitionsLoader from Neo4j, using the provided database connection parameters.
     * @param ctx the parameters to connect to the database
     * @throws PMDBException if a connection cannot be made to the database
     */
    public Neo4jProhibitionsLoader(DatabaseContext ctx) throws PMDBException {
        neo4j = new Neo4jConnection(ctx.getHost(), ctx.getPort(), ctx.getUsername(), ctx.getPassword());
    }

    /**
     * Load all of the  prohibitions in the database into a memory structure.
     * @return the list of all prohibitions.
     * @throws PMDBException if there is an error getting a prohibition form the database.
     * @throws PMProhibitionException if there is an error constructing the prohibition objects.
     */
    @Override
    public List<Prohibition> loadProhibitions() throws PMDBException, PMProhibitionException {
        List<Prohibition> prohibitions = new ArrayList<>();

        String cypher = "match(p:prohibition) return p.name, p.operations, p.intersection";
        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                String name = rs.getString(1);
                HashSet<String> ops = Neo4jHelper.getStringSetFromJson(rs.getString(2));
                boolean inter = rs.getBoolean(3);

                //get subject
                Prohibition.Subject subject = null;
                cypher = "match(d:prohibition{name:'" + name + "'})<-[:prohibition]-(s:prohibition_subject) return s.subjectID, s.subjectType";
                try(
                        Connection subjectConn = neo4j.getConnection();
                        PreparedStatement subjectStmt = subjectConn.prepareStatement(cypher);
                        ResultSet subjectRs = subjectStmt.executeQuery()
                ) {
                    if (subjectRs.next()) {
                        long subjectID = subjectRs.getLong(1);
                        String subjectType = subjectRs.getString(2);
                        subject = new Prohibition.Subject(subjectID, Prohibition.SubjectType.toType(subjectType));
                    }
                }

                //get nodes
                List<NodeContext> nodes = new ArrayList<>();
                cypher = "match(d:prohibition{name:'" + name + "'})<-[r:prohibition]-(s:prohibition_node) return s.id, r.complement";
                try(
                        Connection resConn = neo4j.getConnection();
                        PreparedStatement resStmt = resConn.prepareStatement(cypher);
                        ResultSet resRs = resStmt.executeQuery()
                ) {
                    while(resRs.next()) {
                        long resourceID = resRs.getLong(1);
                        boolean comp = resRs.getBoolean(2);
                        nodes.add(new NodeContext().id(resourceID).complement(comp));
                    }
                }

                prohibitions.add(new Prohibition(name, subject, nodes, ops, inter));
            }
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }

        return prohibitions;
    }
}
