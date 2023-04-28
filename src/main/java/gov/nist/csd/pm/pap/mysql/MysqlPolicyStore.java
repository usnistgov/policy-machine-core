package gov.nist.csd.pm.pap.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.*;
import gov.nist.csd.pm.policy.events.PolicySynchronizationEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;

public class MysqlPolicyStore extends PolicyStore {

    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String MYSQL_SCHEMA = "pm_core";

    static final ObjectMapper objectMapper = new ObjectMapper();
    static final ObjectReader hashmapReader = new ObjectMapper().readerFor(HashMap.class);
    static final ObjectReader arsetReader = new ObjectMapper().readerFor(AccessRightSet.class);
    static final ObjectReader userCtxReader = new ObjectMapper().readerFor(UserContext.class);

    protected final MysqlConnection connection;

    private final MysqlGraph graph;
    private final MysqlProhibitions prohibitions;
    private final MysqlObligations obligations;
    private final MysqlUserDefinedPML userDefinedPML;

    public MysqlPolicyStore(Connection connection) {
        this.connection = new MysqlConnection(connection);

        this.graph = new MysqlGraph(this.connection);
        this.prohibitions = new MysqlProhibitions(this.connection);
        this.obligations = new MysqlObligations(this.connection);
        this.userDefinedPML = new MysqlUserDefinedPML(this.connection);
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

    @Override
    public PolicySynchronizationEvent policySync() throws PMException {
        return new PolicySynchronizationEvent(
                new MemoryPolicyStore(graph, prohibitions, obligations, userDefinedPML)
        );
    }

    @Override
    protected void reset() throws MysqlPolicyException {
        List<String> sequence = PolicyResetSequence.getSequence();
        try (Statement stmt = connection.getConnection().createStatement()) {
            for (String s : sequence) {
                stmt.executeUpdate(s);
            }
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }

    @Override
    public Graph graph() {
        return graph;
    }

    @Override
    public Prohibitions prohibitions() {
        return prohibitions;
    }

    @Override
    public Obligations obligations() {
        return obligations;
    }

    @Override
    public UserDefinedPML userDefinedPML() {
        return userDefinedPML;
    }

    @Override
    public PolicySerializer serialize() throws PMException {
        return new MysqlPolicySerializer(this);
    }

    @Override
    public PolicyDeserializer deserialize() throws PMException {
        return new MysqlPolicyDeserializer(this);
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
}
