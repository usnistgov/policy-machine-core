package gov.nist.csd.pm.pap.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.NodeDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.ObligationDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.policy.model.graph.Graph;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.Properties;
import gov.nist.csd.pm.policy.model.graph.relationships.Assignment;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.graph.relationships.Relationship;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.serializer.PolicyDeserializer;
import gov.nist.csd.pm.policy.serializer.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.events.PolicySynchronizationEvent;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject.Type.*;
import static gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject.Type.USER;

public class MysqlPolicyStore extends PolicyStore {

    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String MYSQL_SCHEMA = "pm_core";

    static final ObjectMapper objectMapper = new ObjectMapper();
    static final ObjectReader hashmapReader = new ObjectMapper().readerFor(HashMap.class);
    static final ObjectReader arsetReader = new ObjectMapper().readerFor(AccessRightSet.class);
    static final ObjectReader userCtxReader = new ObjectMapper().readerFor(UserContext.class);

    private MysqlConnection connection;

    public MysqlPolicyStore(Connection connection) {
        this.connection = new MysqlConnection(connection);
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

    static int getNodeTypeId(NodeType nodeType) {
        // values are mapped to values in node_type table
        return switch (nodeType) {
            case PC -> 5;
            case OA -> 1;
            case UA -> 2;
            case O -> 4;
            default -> 3; // U
        };
    }

    static NodeType getNodeTypeFromId(int id) {
        // values are mapped to values in node_type table
        return switch (id) {
            case 1 -> OA;
            case 2 -> UA;
            case 3 -> U;
            case 4 -> O;
            default -> PC;
        };
    }

    public static String toJSON(Map<String, String> map) throws JsonProcessingException {
        return objectMapper.writeValueAsString(map);
    }

    public static String arsetToJson(AccessRightSet set) throws JsonProcessingException {
        return objectMapper.writeValueAsString(set);
    }

    @Override
    public String toString(PolicySerializer policySerializer) throws PMException {
        return policySerializer.serialize(this);
    }

    @Override
    public void fromString(String s, PolicyDeserializer policyDeserializer) throws PMException {
        beginTx();
        policyDeserializer.deserialize(this, s);
        commit();
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
    public Node getNode(String name) throws PMException {
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
    public List<String> getChildren(String node) throws MysqlPolicyException {
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
    public List<String> getParents(String node) throws MysqlPolicyException {
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
    public Map<String, List<Prohibition>> getProhibitions() throws MysqlPolicyException {
        String sql = """
                select id, label, (select name from node where node.id=prohibition.node_id) as node, process_id, subject_type, access_rights, is_intersection from prohibition
                """;

        try(Statement stmt = connection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            List<Prohibition> prohibitions = getProhibitionsFromResultSet(rs);

            rs.close();

            Map<String, List<Prohibition>> prohibitionsMap = new HashMap<>();
            for (Prohibition p : prohibitions) {
                List<Prohibition> subjPros = prohibitionsMap.getOrDefault(p.getSubject().name(), new ArrayList<>());
                subjPros.add(p);
                prohibitionsMap.put(p.getSubject().name(), subjPros);
            }

            return prohibitionsMap;
        } catch (SQLException  e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    private List<ContainerCondition> getContainerConditions(int id) throws SQLException {
        List<ContainerCondition> containers = new ArrayList<>();
        String containerSql = """
                        select (select name from node where node.id = prohibition_container.container_id) as container, is_complement 
                        from prohibition_container 
                        where prohibition_id=?
                        """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(containerSql)) {
            ps.setInt(1, id);
            ResultSet containerRs = ps.executeQuery();
            while (containerRs.next()) {
                String container = containerRs.getString(1);
                boolean isComplement = containerRs.getBoolean(2);
                containers.add(new ContainerCondition(container, isComplement));
            }

            containerRs.close();
        }

        return containers;
    }

    @Override
    public List<Prohibition> getProhibitionsWithSubject(String subject) throws MysqlPolicyException {
        String sql = """
                select id, label, (select name from node where node.id=prohibition.node_id) as node, process_id, subject_type, access_rights, is_intersection 
                from prohibition 
                where node_id = (select id from node where name = ?) || process_id = ?
                """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, subject);
            ps.setString(2, subject);
            ResultSet rs = ps.executeQuery();

            List<Prohibition> prohibitions = getProhibitionsFromResultSet(rs);

            rs.close();

            return prohibitions;
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public Prohibition getProhibition(String label) throws PMException {
        String sql = """
                select id, label, (select name from node where node.id=prohibition.node_id) as node, process_id, subject_type, access_rights, is_intersection from prohibition where label = ?
                """;

        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, label);
            ResultSet rs = ps.executeQuery();

            List<Prohibition> prohibitions = getProhibitionsFromResultSet(rs);
            if (prohibitions.isEmpty()) {
                throw new ProhibitionDoesNotExistException(label);
            }

            rs.close();

            return prohibitions.get(0);
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    private List<Prohibition> getProhibitionsFromResultSet(ResultSet rs) throws MysqlPolicyException {
        List<Prohibition> prohibitions = new ArrayList<>();

        try {
            while (rs.next()) {
                int id = rs.getInt(1);
                String label = rs.getString(2);
                String node = rs.getString(3);
                String process = rs.getString(4);
                ProhibitionSubject.Type type = getProhibitionSubjectTypeFromId(rs.getInt(5));
                AccessRightSet arset = MysqlPolicyStore.arsetReader.readValue(rs.getString(6));
                boolean isIntersection = rs.getBoolean(7);

                List<ContainerCondition> containers = getContainerConditions(id);

                prohibitions.add(new Prohibition(label, new ProhibitionSubject(type == PROCESS ? process : node, type), arset, isIntersection, containers));
            }

            return prohibitions;
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public List<Obligation> getObligations() throws MysqlPolicyException {
        List<Obligation> obligations = new ArrayList<>();

        String sql = """
                select label, author, rules from obligation;
                """;

        try(Statement stmt = connection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String label = rs.getString(1);
                UserContext author = MysqlPolicyStore.userCtxReader.readValue(rs.getString(2));
                Rule[] rules = deserializeRules(rs.getBlob(3).getBinaryStream().readAllBytes());

                obligations.add(new Obligation(author, label, List.of(rules)));
            }

            return obligations;
        } catch (SQLException | IOException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public Obligation getObligation(String label) throws PMException {
        String sql = """
                select author, rules from obligation where label = label
                """;

        try(Statement stmt = connection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            if (!rs.next()) {
                throw new ObligationDoesNotExistException(label);
            }

            UserContext author = MysqlPolicyStore.userCtxReader.readValue(rs.getString(1));
            Rule[] rules = deserializeRules(rs.getBlob(2).getBinaryStream().readAllBytes());

            return new Obligation(author, label, List.of(rules));
        } catch (SQLException | IOException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    private static byte[] serializeRules(Rule[] rules) {
        return SerializationUtils.serialize(rules);
    }

    private static Rule[] deserializeRules(byte[] b) {
        return SerializationUtils.deserialize(b);
    }


    @Override
    public Map<String, FunctionDefinitionStatement> getPALFunctions() throws MysqlPolicyException {
        String sql = """
                select bytes from pal_function
                """;

        Map<String, FunctionDefinitionStatement> functionDefinitionStatements = new HashMap<>();
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next()) {
                FunctionDefinitionStatement funcDef = SerializationUtils.deserialize(resultSet.getBlob(1).getBinaryStream().readAllBytes());
                functionDefinitionStatements.put(funcDef.getFunctionName(), funcDef);
            }
        } catch (SQLException | IOException e) {
            throw new MysqlPolicyException(e.getMessage());
        }

        return functionDefinitionStatements;
    }

    @Override
    public Map<String, Value> getPALConstants() throws MysqlPolicyException {
        String sql = """
                select name, value from pal_constant
                """;

        Map<String, Value> constants = new HashMap<>();
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next()) {
                String key = resultSet.getString(1);
                Value value = SerializationUtils.deserialize(resultSet.getBlob(2).getBinaryStream().readAllBytes());
                constants.put(key, value);
            }
        } catch (SQLException | IOException e) {
            throw new MysqlPolicyException(e.getMessage());
        }

        return constants;
    }

    @Override
    public PALContext getPALContext() throws MysqlPolicyException {
        return new PALContext(getPALFunctions(), getPALConstants());
    }

    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet) throws MysqlPolicyException {
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
    public String createPolicyClass(String name, Map<String, String> properties) throws MysqlPolicyException {
        return createNode(name, properties);
    }

    @Override
    public String createPolicyClass(String name) throws MysqlPolicyException {
        return createPolicyClass(name, noprops());
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) throws MysqlPolicyException {
        return createNode(name, OA, properties, parent, parents);
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents) throws MysqlPolicyException {
        return createUserAttribute(name, noprops(), parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) throws MysqlPolicyException {
        return createNode(name, OA, properties, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents) throws MysqlPolicyException {
        return createObjectAttribute(name, noprops(), parent, parents);
    }

    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents) throws MysqlPolicyException {
        return createNode(name, O, properties, parent, parents);
    }

    @Override
    public String createObject(String name, String parent, String... parents) throws MysqlPolicyException {
        return createObject(name, noprops(), parent, parents);
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents) throws MysqlPolicyException {
        return createNode(name, U, properties, parent, parents);
    }

    @Override
    public String createUser(String name, String parent, String... parents) throws MysqlPolicyException {
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
                              String initialParent, String ... parents) throws MysqlPolicyException {
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
    public void deassign(String child, String parent) throws MysqlPolicyException {
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
    public void createProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws MysqlPolicyException {
        beginTx();

        String sql;
        if (subject.type() == ProhibitionSubject.Type.PROCESS) {
            sql =
                    """
                    insert into prohibition (label, process_id, subject_type, access_rights, is_intersection) values (?,?,?,?,?)
                    """;
        } else {
            sql =
                    """
                    insert into prohibition (label, node_id, subject_type, access_rights, is_intersection) values (?,(select id from node where name = ?),?,?,?)
                    """;
        }

        int prohibitionID;
        try (PreparedStatement ps = connection.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, label);
            ps.setString(2, subject.name());
            ps.setInt(3, getProhibitionSubjectTypeId(subject.type()));
            ps.setString(4, MysqlPolicyStore.arsetToJson(accessRightSet));
            ps.setBoolean(5, intersection);

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                prohibitionID = generatedKeys.getInt(1);
            } else {
                throw new MysqlPolicyException("could not retrieve generated prohibition id after insert");
            }
        } catch (SQLException | JsonProcessingException e) {
            rollback();
            throw new MysqlPolicyException(e.getMessage());
        }

        sql = """
        insert into prohibition_container (prohibition_id, container_id, is_complement) values (?, (select id from node where name = ?), ?)
        """;
        try (PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            for (ContainerCondition containerCondition : containerConditions) {
                ps.setInt(1, prohibitionID);
                ps.setString(2, containerCondition.name());
                ps.setInt(3, containerCondition.complement() ? 1 : 0);

                ps.addBatch();
            }

            ps.executeBatch();
        } catch (SQLException e) {
            rollback();
            throw new MysqlPolicyException(e.getMessage());
        }

        commit();
    }

    private int getProhibitionSubjectTypeId(ProhibitionSubject.Type type) {
        switch (type) {
            case USER -> {
                return 1;
            }
            case USER_ATTRIBUTE -> {
                return 2;
            }
            case PROCESS -> {
                return 3;
            }
        }

        return 0;
    }

    private ProhibitionSubject.Type getProhibitionSubjectTypeFromId(int id) {
        switch (id) {
            case 1 -> {
                return USER;
            }
            case 2 -> {
                return USER_ATTRIBUTE;
            }
            case 3 -> {
                return PROCESS;
            }
        }

        return USER;
    }

    @Override
    public void updateProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws MysqlPolicyException {
        beginTx();

        try {
            deleteProhibition(label);
            createProhibition(label, subject, accessRightSet, intersection, containerConditions);
            commit();
        } catch (MysqlPolicyException e) {
            rollback();
            throw e;
        }
    }

    @Override
    public void deleteProhibition(String label) throws MysqlPolicyException {
        String sql = """
                delete from prohibition where label = ?
                """;

        try (PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, label);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void createObligation(UserContext author, String label, Rule... rules) throws MysqlPolicyException {
        String sql = """
                insert into obligation (label, author, rules) values (?, ?, ?)
                """;

        try (PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, label);
            ps.setString(2, MysqlPolicyStore.objectMapper.writeValueAsString(author));
            ps.setBytes(3, serializeRules(rules));

            ps.executeUpdate();
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void updateObligation(UserContext author, String label, Rule... rules) throws MysqlPolicyException {
        beginTx();

        try {
            deleteObligation(label);
            createObligation(author, label, rules);
            commit();
        } catch (MysqlPolicyException e) {
            rollback();
            throw e;
        }
    }

    @Override
    public void deleteObligation(String label) throws MysqlPolicyException {
        String sql = """
                delete from obligation where label = ?
                """;

        try (PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, label);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void addPALFunction(FunctionDefinitionStatement functionDefinitionStatement) throws MysqlPolicyException {
        String sql = """
                insert into pal_function (name, bytes) values (?,?)
                """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, functionDefinitionStatement.getFunctionName());
            ps.setBytes(2, SerializationUtils.serialize(functionDefinitionStatement));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void removePALFunction(String functionName) throws MysqlPolicyException {
        String sql = """
                delete from pal_function where name=?
                """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, functionName);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void addPALConstant(String constantName, Value constantValue) throws MysqlPolicyException {
        String sql = """
                insert into pal_constant (name, value) values (?,?)
                """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, constantName);
            ps.setBytes(2, SerializationUtils.serialize(constantValue));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public void removePALConstant(String constName) throws MysqlPolicyException {
        String sql = """
                delete from pal_constant where name=?
                """;
        try(PreparedStatement ps = connection.getConnection().prepareStatement(sql)) {
            ps.setString(1, constName);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public PolicySynchronizationEvent policySync() throws PMException {
        return new PolicySynchronizationEvent(
                getGraph(),
                getProhibitions(),
                getObligations(),
                getPALContext()
        );
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
}
