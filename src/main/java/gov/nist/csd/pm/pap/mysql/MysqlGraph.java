package gov.nist.csd.pm.pap.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.nist.csd.pm.pap.GraphStore;
import gov.nist.csd.pm.policy.Graph;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.nodes.Properties;
import gov.nist.csd.pm.policy.model.graph.relationships.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.isAdminAccessRight;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.isWildcardAccessRight;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.U;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

public class MysqlGraph implements GraphStore {

    private MysqlConnection connection;

    public MysqlGraph(MysqlConnection connection) {
        this.connection = connection;
    }
    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet)
    throws MysqlPolicyException, AdminAccessRightExistsException {
        checkSetResourceAccessRightsInput(accessRightSet);

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
    public String createPolicyClass(String name, Map<String, String> properties)
    throws PMBackendException, NodeNameExistsException {
        return createPolicyClassNode(name, properties);
    }

    @Override
    public String createPolicyClass(String name) throws PMBackendException, NodeNameExistsException {
        return createPolicyClass(name, NO_PROPERTIES);
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents)
    throws PMBackendException, NodeDoesNotExistException, InvalidAssignmentException, NodeNameExistsException, AssignmentCausesLoopException {
        return createNode(name, UA, properties, parent, parents);
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents)
    throws PMBackendException, NodeDoesNotExistException, InvalidAssignmentException, NodeNameExistsException, AssignmentCausesLoopException {
        return createUserAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents)
    throws PMBackendException, NodeDoesNotExistException, InvalidAssignmentException, NodeNameExistsException, AssignmentCausesLoopException {
        return createNode(name, OA, properties, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents)
    throws PMBackendException, NodeDoesNotExistException, InvalidAssignmentException, NodeNameExistsException, AssignmentCausesLoopException {
        return createObjectAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents)
    throws PMBackendException, NodeDoesNotExistException, InvalidAssignmentException, NodeNameExistsException, AssignmentCausesLoopException {
        return createNode(name, O, properties, parent, parents);
    }

    @Override
    public String createObject(String name, String parent, String... parents)
    throws PMBackendException, NodeDoesNotExistException, InvalidAssignmentException, NodeNameExistsException, AssignmentCausesLoopException {
        return createObject(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents)
    throws PMBackendException, NodeDoesNotExistException, InvalidAssignmentException, NodeNameExistsException, AssignmentCausesLoopException {
        return createNode(name, U, properties, parent, parents);
    }

    @Override
    public String createUser(String name, String parent, String... parents)
    throws PMBackendException, NodeDoesNotExistException, InvalidAssignmentException, NodeNameExistsException, AssignmentCausesLoopException {
        return createUser(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties)
    throws PMBackendException, NodeDoesNotExistException {
        checkSetNodePropertiesInput(name);

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
    public Node getNode(String name) throws NodeDoesNotExistException, PMBackendException {
        checkGetNodeInput(name);

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

    protected static Node getNodeFromResultSet(ResultSet rs) throws MysqlPolicyException {
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
    public List<String> search(NodeType type, Map<String, String> properties) throws MysqlPolicyException {
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
    public void deleteNode(String name)
    throws PMBackendException, NodeHasChildrenException, NodeReferencedInProhibitionException, NodeReferencedInObligationException {
        if (!checkDeleteNodeInput(name, new MysqlProhibitions(connection), new MysqlObligations(connection))) {
            return;
        }

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
    public void assign(String child, String parent)
    throws PMBackendException, NodeDoesNotExistException, InvalidAssignmentException, AssignmentCausesLoopException {
        if (!checkAssignInput(child, parent)) {
            return;
        }

        String sql = """
            INSERT INTO assignment (start_node_id, end_node_id) VALUES (
              (SELECT id FROM node WHERE name=?), (SELECT id FROM node WHERE name=?)
            ) ON DUPLICATE KEY UPDATE start_node_id=start_node_id
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
    public void deassign(String child, String parent) throws PMBackendException, NodeDoesNotExistException {
        if (!checkDeassignInput(child, parent)) {
            return;
        }

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
    public void assignAll(List<String> children, String target)
    throws PMBackendException, NodeDoesNotExistException, InvalidAssignmentException, AssignmentCausesLoopException {
        try {
            connection.beginTx();

            for (String c : children) {
                assign(c, target);
            }

            connection.commit();
        } catch (MysqlPolicyException e) {
            connection.rollback();
            throw e;
        }
    }

    @Override
    public void deassignAll(List<String> children, String target) throws PMBackendException, NodeDoesNotExistException {
        try {
            connection.beginTx();

            for (String c : children) {
                deassign(c, target);
            }

            connection.commit();
        } catch (MysqlPolicyException e) {
            connection.rollback();
            throw e;
        }
    }

    @Override
    public void deassignAllFromAndDelete(String target)
    throws PMBackendException, NodeDoesNotExistException, NodeHasChildrenException, NodeReferencedInProhibitionException, NodeReferencedInObligationException {
        try {
            connection.beginTx();

            deassignAll(getChildren(target), target);
            deleteNode(target);

            connection.commit();
        } catch (MysqlPolicyException e) {
            connection.rollback();
            throw e;
        }
    }

    @Override
    public List<String> getParents(String node) throws PMBackendException, NodeDoesNotExistException {
        checkGetParentsInput(node);

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
    public List<String> getChildren(String node) throws PMBackendException, NodeDoesNotExistException {
        checkGetChildrenInput(node);

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
    public void associate(String ua, String target, AccessRightSet accessRights)
    throws PMBackendException, UnknownAccessRightException, NodeDoesNotExistException, InvalidAssociationException {
        checkAssociateInput(ua, target, accessRights);

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
    public void dissociate(String ua, String target) throws PMBackendException, NodeDoesNotExistException {
        if (!checkDissociateInput(ua, target)) {
            return;
        }

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
    public List<Association> getAssociationsWithSource(String ua) throws PMBackendException, NodeDoesNotExistException {
        checkGetAssociationsWithSourceInput(ua);

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
    public List<Association> getAssociationsWithTarget(String target)
    throws PMBackendException, NodeDoesNotExistException {
        checkGetAssociationsWithTargetInput(target);

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

    private String createPolicyClassNode(String name, Map<String, String> properties)
    throws PMBackendException, NodeNameExistsException {
        checkCreatePolicyClassInput(name);

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
                              String initialParent, String ... parents)
    throws PMBackendException, NodeDoesNotExistException, InvalidAssignmentException, NodeNameExistsException, AssignmentCausesLoopException {
        checkCreateNodeInput(name, type, initialParent, parents);

        connection.beginTx();

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

            connection.commit();
        } catch (SQLException | JsonProcessingException | MysqlPolicyException e) {
            connection.rollback();
            throw new MysqlPolicyException(e.getMessage());
        }

        return name;
    }
}
