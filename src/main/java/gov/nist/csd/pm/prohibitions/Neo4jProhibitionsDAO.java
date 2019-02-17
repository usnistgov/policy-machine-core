package gov.nist.csd.pm.prohibitions;

import gov.nist.csd.pm.db.DatabaseContext;
import gov.nist.csd.pm.db.neo4j.Neo4jConnection;
import gov.nist.csd.pm.db.neo4j.Neo4jHelper;
import gov.nist.csd.pm.exceptions.PMDBException;
import gov.nist.csd.pm.exceptions.PMProhibitionException;
import gov.nist.csd.pm.graph.model.nodes.NodeContext;
import gov.nist.csd.pm.prohibitions.loader.Neo4jProhibitionsLoader;
import gov.nist.csd.pm.prohibitions.model.Prohibition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static gov.nist.csd.pm.db.neo4j.Neo4jHelper.setToCypherArray;

/**
 * Neo4j implementation of the ProhibitionsDAO interface.
 */
public class Neo4jProhibitionsDAO implements ProhibitionsDAO {

    private Neo4jConnection neo4j;
    private DatabaseContext ctx;

    /**
     * Create a new Neo4jProhibitionsDAO with the given database context information.
     * @param ctx the database connection information.
     * @throws PMDBException if there is an error establishing a connection to the Neo4j instance.
     */
    public Neo4jProhibitionsDAO(DatabaseContext ctx) throws PMDBException {
        this.ctx = ctx;
        this.neo4j = new Neo4jConnection(ctx.getHost(), ctx.getPort(), ctx.getUsername(), ctx.getPassword());
    }

    /**
     * Create a prohibition in Neo4j.
     *
     * 1. Create a base neo4j node for the prohibition.
     *      - store the name of the prohibition, the operations, and a boolean indicating the intersection value.
     * 2. Create neo4j nodes to represent each node in the prohibition.
     *      - store the ID fo the node and a boolean complement value.
     *      - create a relationship between these nodes and the base node.
     * 3. Create a neo4j node to represent the subject of the prohibition.
     *      - store the ID and type of the subject.
     *
     * Prohibitions are tagged with 'prohibition' and are stored separately from the rest of the graph.
     *
     * @param prohibition the prohibition to be created.
     * @throws PMDBException if there is an error creating in the prohibition in the database.
     */
    @Override
    public void createProhibition(Prohibition prohibition) throws PMDBException {
        String name = prohibition.getName();
        Prohibition.Subject subject = prohibition.getSubject();
        HashSet<String> operations = prohibition.getOperations();
        List<NodeContext> nodes = prohibition.getNodes();
        boolean intersection = prohibition.isIntersection();

        String nodesStr = "";
        for (NodeContext node : nodes) {
            nodesStr += String.format(" with p create(p)<-[:prohibition]-(pn:prohibition_node:%s{id:%d, complement:%s})", name, node.getID(), node.isComplement());
        }

        String cypher =
                String.format("create (p:prohibition:%s{name: '%s', operations: %s, intersection: %b}) ", name, name, setToCypherArray(operations), intersection) +
                        " with p " +
                        String.format("create(p)<-[:prohibition]-(ps:prohibition_subject:%s{subjectID:%d, subjectType:'%s'})", name, subject.getSubjectID(), subject.getSubjectType()) +
                        nodesStr;

        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
        ) {
            stmt.executeQuery();
        }catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    /**
     * Get all prohibitions from the database. Pass the current database context information to a Prohibitions loader
     * to do the loading.
     *
     * @return a List of the prohibitions from the database.
     * @throws PMDBException if there is an error retrieving the prohibitions from the database.
     * @throws PMProhibitionException if there is an error converting the data n the database to a Prohibition object.
     */
    @Override
    public List<Prohibition> getProhibitions() throws PMDBException, PMProhibitionException {
        return new Neo4jProhibitionsLoader(ctx).loadProhibitions();
    }

    @Override
    public Prohibition getProhibition(String prohibitionName) throws PMDBException, PMProhibitionException {
        Prohibition prohibition = null;

        String cypher = "match(p:prohibition{name: " + prohibitionName + "}) return p.name, p.operations, p.intersection";
        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
                ResultSet rs = stmt.executeQuery()
        ) {
            if (rs.next()) {
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
                cypher = "match(d:prohibition{name:'" + name + "'})-[r:prohibition]->(s:prohibition_node) return s.id, r.complement";
                try(
                        Connection resConn = neo4j.getConnection();
                        PreparedStatement resStmt = resConn.prepareStatement(cypher);
                        ResultSet resRs = resStmt.executeQuery()
                ) {
                    while(resRs.next()) {
                        long id = resRs.getLong(1);
                        boolean comp = resRs.getBoolean(2);
                        nodes.add(new NodeContext().id(id).complement(comp));
                    }
                }

                prohibition = new Prohibition(name, subject, nodes, ops, inter);
            }
        }
        catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }

        return prohibition;
    }

    @Override
    public void deleteProhibition(String prohibitionName) throws PMDBException {
        String cypher = "match(p:" + prohibitionName + ") detach delete p";

        try(
                Connection conn = neo4j.getConnection();
                PreparedStatement stmt = conn.prepareStatement(cypher);
        ) {
            stmt.executeQuery();


        }catch (SQLException e) {
            throw new PMDBException(e.getMessage());
        }
    }

    @Override
    public void updateProhibition(Prohibition prohibition) throws PMDBException {
        deleteProhibition(prohibition.getName());
        createProhibition(prohibition);
    }
}
