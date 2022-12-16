package gov.nist.csd.pm.pap.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import gov.nist.csd.pm.pap.store.*;
import gov.nist.csd.pm.policy.serializer.PolicyDeserializer;
import gov.nist.csd.pm.policy.serializer.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.events.PolicySynchronizationEvent;

import java.util.*;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;

public class MysqlPolicyStore extends PolicyStore {

    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String MYSQL_SCHEMA = "pm_core";

    static final ObjectMapper objectMapper = new ObjectMapper();
    static final ObjectReader hashmapReader = new ObjectMapper().readerFor(HashMap.class);
    static final ObjectReader arsetReader = new ObjectMapper().readerFor(AccessRightSet.class);
    static final ObjectReader userCtxReader = new ObjectMapper().readerFor(UserContext.class);

    private MysqlConnection connection;

    public MysqlPolicyStore(MysqlConnection connection) throws MysqlPolicyException {
        this.connection = connection;
    }

    @Override
    public MysqlGraphStore graph() {
        return new MysqlGraphStore(connection);
    }

    @Override
    public MysqlProhibitionsStore prohibitions() {
        return new MysqlProhibitionsStore(connection);
    }

    @Override
    public MysqlObligationsStore obligations() {
        return new MysqlObligationsStore(connection);
    }

    @Override
    public MysqlPALStore pal() {
        return new MysqlPALStore(connection);
    }

    @Override
    public PolicySynchronizationEvent policySync() throws PMException {
        return connection.policySync();
    }

    @Override
    public void beginTx() throws MysqlPolicyException {
        connection.beginTx();
    }

    @Override
    public void commit() throws PMException {
        connection.commit();
    }

    @Override
    public void rollback() throws PMException {
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
}
