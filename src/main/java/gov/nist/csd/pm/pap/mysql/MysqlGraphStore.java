package gov.nist.csd.pm.pap.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.nist.csd.pm.pap.store.GraphStore;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.exceptions.NodeDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.Graph;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.nodes.Properties;
import gov.nist.csd.pm.policy.model.graph.relationships.Assignment;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.graph.relationships.Relationship;
import gov.nist.csd.pm.policy.tx.TxCommitException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.PC;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;

class MysqlGraphStore extends GraphStore {

    private final MysqlConnection connection;

    public MysqlGraphStore(MysqlConnection connection) {
        this.connection = connection;
    }

    Graph getGraph() throws MysqlPolicyException {
        Graph graph = new Graph();

        List<Node> nodes = getNodes();
        for (Node node : nodes) {
            graph.addNode(node.getName(), node.getType(), node.getProperties());
        }

        List<Assignment> assignments = getAssignments();
        for (Relationship assignment : assignments) {
            graph.assign(assignment.getSource(), assignment.getTarget());
        }

        List<Association> associations = getAssociations();
        for (Relationship association : associations) {
            graph.associate(association.getSource(), association.getTarget(), association.getAccessRightSet());
        }

        return graph;
    }

    private List<Node> getNodes() throws MysqlPolicyException {
        List<Node> nodes = new ArrayList<>();
        String sql = """
                    SELECT name, node_type_id, properties from node
                    """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                nodes.add(getNodeFromResultSet(rs));
            }

            rs.close();

            return nodes;
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    private List<Assignment> getAssignments() throws MysqlPolicyException {
        List<Assignment> assignments = new ArrayList<>();
        String sql = """
                    SELECT child.name, parent.name FROM assignment
                    join node as child on assignment.start_node_id=child.id
                    join node as parent on assignment.end_node_id=parent.id;
                    """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                assignments.add(getAssignmentFromResultSet(rs));
            }

            rs.close();

            return assignments;
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    private Assignment getAssignmentFromResultSet(ResultSet rs) throws MysqlPolicyException {
        try {
            String child = rs.getString(1);
            String parent = rs.getString(2);
            return new Assignment(child, parent);
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    private List<Association> getAssociations() throws MysqlPolicyException {
        List<Association> associations = new ArrayList<>();
        String sql = """
                    SELECT ua.name, target.name, operation_set FROM association
                    join node as ua on association.start_node_id=ua.id
                    join node as target on association.end_node_id=target.id;
                    """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                associations.add(getAssociationFromResultSet(rs));
            }

            rs.close();

            return associations;
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    private Association getAssociationFromResultSet(ResultSet rs) throws MysqlPolicyException {
        try {
            String ua = rs.getString(1);
            String target = rs.getString(2);
            AccessRightSet arset = MysqlPolicyStore.arsetReader.readValue(rs.getString(3));
            return new Association(ua, target, arset);
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet) throws PMException {
        try {
            String sql = """
                insert into resource_access_rights (id, access_rights) values (1, ?) ON DUPLICATE KEY UPDATE access_rights = (?);
                """;
            try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
                String arJson = MysqlPolicyStore.arsetToJson(accessRightSet);
                ps.setString(1, arJson);
                ps.setString(2, arJson);
                ps.execute();
            }
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public AccessRightSet getResourceAccessRights() throws MysqlPolicyException {
        AccessRightSet arset = new AccessRightSet();
        String sql = """
                    select access_rights from resource_access_rights;
                    """;

        try(Statement stmt = connection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                arset = MysqlPolicyStore.arsetReader.readValue(rs.getString(1));
            }

            return arset;
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public String createPolicyClass(String name, Map<String, String> properties) throws PMException {
        return createNode(name, properties);
    }

    @Override
    public String createPolicyClass(String name) throws PMException {
        return createPolicyClass(name, noprops());
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, OA, properties, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents) throws PMException {
        return createObjectAttribute(name, noprops(), parent, parents);
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, UA, properties, parent, parents);
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents) throws PMException {
        return createUserAttribute(name, noprops(), parent, parents);
    }


    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, O, properties, parent, parents);
    }

    @Override
    public String createObject(String name, String parent, String... parents) throws PMException {
        return createObject(name, noprops(), parent, parents);
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents) throws PMException {
        return createNode(name, U, properties, parent, parents);
    }

    @Override
    public String createUser(String name, String parent, String... parents) throws PMException {
        return createUser(name, noprops(), parent, parents);
    }

    private String createNode(String name, Map<String, String> properties) throws MysqlPolicyException {
        String sql = """
                    INSERT INTO node (node_type_id, name, properties) VALUES (?,?,?)
                    """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, MysqlPolicyStore.getNodeTypeId(PC));
            ps.setString(2, name);
            ps.setString(3, MysqlPolicyStore.toJSON(properties));
            ps.execute();
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }

        return name;
    }

    private String createNode(String name, NodeType type, Map<String, String> properties,
                              String initialParent, String ... parents) throws MysqlPolicyException, TxCommitException {
        beginTx();

        String sql = """
                    INSERT INTO node (node_type_id, name, properties) VALUES (?,?,?)
                    """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, MysqlPolicyStore.getNodeTypeId(type));
            ps.setString(2, name);
            ps.setString(3, MysqlPolicyStore.toJSON(properties));
            ps.execute();

            assign(name, initialParent);

            for (String parent : parents) {
                assign(name, parent);
            }

            commit();
        } catch (SQLException | JsonProcessingException | MysqlPolicyException e) {
            rollback();
            throw new MysqlPolicyException(e.getMessage());
        }

        return name;
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties) throws MysqlPolicyException {
        String sql = """
                    UPDATE node SET properties=? WHERE NAME=?
                    """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, MysqlPolicyStore.toJSON(properties));
            ps.setString(2, name);
            ps.execute();
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void deleteNode(String name) throws MysqlPolicyException {
        String sql = """
                    DELETE FROM node WHERE NAME=?
                    """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.execute();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public boolean nodeExists(String name) throws MysqlPolicyException {
        String sql = """
                    SELECT count(*) FROM node WHERE name = ?
                    """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return false;
            }

            int anInt = rs.getInt(1);
            boolean exists =  anInt == 1;

            rs.close();

            return exists;
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public Node getNode(String name) throws NodeDoesNotExistException, MysqlPolicyException {
        String sql = """
                    SELECT name, node_type_id, properties FROM node WHERE name = ?
                    """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NodeDoesNotExistException(name);
            }

            Node node = getNodeFromResultSet(rs);

            rs.close();

            return node;
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    private Node getNodeFromResultSet(ResultSet rs) throws MysqlPolicyException {
        try {
            String name = rs.getString(1);
            NodeType type = MysqlPolicyStore.getNodeTypeFromId(rs.getInt(2));
            Map<String, String> props = MysqlPolicyStore.hashmapReader.readValue(rs.getString(3));
            return new Node(name, type, props);
        } catch (JsonProcessingException | SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public List<String> search(NodeType type, Map<String, String> properties) throws PMException {
        String sql = "select name from node";
        StringBuilder where = new StringBuilder();
        if (type != ANY) {
            where = new StringBuilder("node_type_id = " + MysqlPolicyStore.getNodeTypeId(type));
        }

        if (properties != null && !properties.isEmpty()) {
            for (String key : properties.keySet()) {
                if (where.length() > 0) {
                    where.append(" AND ");
                }

                String value = properties.get(key);

                where.append("properties -> '$.")
                        .append(key)
                        .append("' like '\"")
                        .append(value.equals(Properties.WILDCARD) ? "%%" : value)
                        .append("\"'");
            }
        }

        if (!where.isEmpty()) {
            sql = sql + " where " + where;
        }

        List<String> results = new ArrayList<>();
        try (Statement stmt = connection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                results.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }

        return results;
    }

    @Override
    public List<String> getPolicyClasses() throws MysqlPolicyException {
        List<String> policyClasses = new ArrayList<>();
        String sql = """
                    SELECT name FROM node WHERE node_type_id = ?
                    """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, MysqlPolicyStore.getNodeTypeId(PC));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                policyClasses.add(rs.getString(1));
            }

            rs.close();

            return policyClasses;
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void assign(String child, String parent) throws MysqlPolicyException {
        String sql = """
            INSERT INTO assignment (start_node_id, end_node_id) VALUES (
              (SELECT id FROM node WHERE name=?), (SELECT id FROM node WHERE name=?)
            )
            """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, child);
            ps.setString(2, parent);
            ps.execute();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void deassign(String child, String parent) throws PMException {
        String sql = """
            DELETE FROM assignment
            WHERE start_node_id = (SELECT id FROM node WHERE name=?)
            AND end_node_id = (SELECT id FROM node WHERE name=?)
            """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, child);
            ps.setString(2, parent);
            ps.execute();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public List<String> getChildren(String node) throws PMException {
        List<String> children = new ArrayList<>();

        String sql = """
                    select children.name from node
                    join assignment on node.id=assignment.end_node_id
                    join node as children on children.id=assignment.start_node_id
                    where node.name = ?;
                    """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, node);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                children.add(rs.getString(1));
            }

            rs.close();

            return children;
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public List<String> getParents(String node) throws PMException {
        List<String> parents = new ArrayList<>();

        String sql = """
                    select parents.name from node
                    join assignment on node.id=assignment.start_node_id
                    join node as parents on parents.id=assignment.end_node_id
                    where node.name = ?;
                    """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, node);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                parents.add(rs.getString(1));
            }

            rs.close();

            return parents;
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void associate(String ua, String target, AccessRightSet accessRights) throws MysqlPolicyException {
        String sql = """
            INSERT INTO association (start_node_id, end_node_id, operation_set) VALUES (
              (SELECT id FROM node WHERE name=?), (SELECT id FROM node WHERE name=?), ?
            ) ON DUPLICATE KEY UPDATE operation_set=?
            """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, ua);
            ps.setString(2, target);

            String json = MysqlPolicyStore.arsetToJson(accessRights);
            ps.setString(3, json);
            ps.setString(4, json);
            ps.execute();
        }catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void dissociate(String ua, String target) throws MysqlPolicyException {
        String sql = """
            DELETE FROM association
            WHERE start_node_id = (SELECT id FROM node WHERE name=?)
            AND end_node_id = (SELECT id FROM node WHERE name=?)
            """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, ua);
            ps.setString(2, target);
            ps.execute();
        }catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public List<Association> getAssociationsWithSource(String ua) throws MysqlPolicyException {
        List<Association> associations = new ArrayList<>();

        String sql = """
                    select targets.name, association.operation_set from node
                    join association on node.id=association.start_node_id
                    join node as targets on targets.id=association.end_node_id
                    where node.name = ?;
                    """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, ua);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String target = rs.getString(1);
                AccessRightSet arset = MysqlPolicyStore.arsetReader.readValue(rs.getString(2));
                associations.add(new Association(ua, target, arset));
            }

            rs.close();
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }

        return associations;
    }

    @Override
    public List<Association> getAssociationsWithTarget(String target) throws MysqlPolicyException {
        List<Association> associations = new ArrayList<>();

        String sql = """
                    select sources.name, association.operation_set from node
                    join association on node.id=association.end_node_id
                    join node as sources on sources.id=association.start_node_id
                    where node.name = ?;
                    """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, target);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String source = rs.getString(1);
                AccessRightSet opSet = MysqlPolicyStore.arsetReader.readValue(rs.getString(2));
                associations.add(new Association(source, target, opSet));
            }

            rs.close();
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }

        return associations;
    }

    @Override
    public void beginTx() throws MysqlPolicyException {
        connection.beginTx();
    }

    @Override
    public void commit() throws MysqlPolicyException {
        connection.commit();
    }

    @Override
    public void rollback() throws MysqlPolicyException {
        connection.rollback();
    }
}
