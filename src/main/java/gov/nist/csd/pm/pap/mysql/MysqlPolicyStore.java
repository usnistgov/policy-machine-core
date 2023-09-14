package gov.nist.csd.pm.pap.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.policy.PolicyDeserializer;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.PMLConstantAlreadyDefinedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.pml.model.expression.Value;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminPolicy.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;

public class MysqlPolicyStore extends PolicyStore implements Verifier {

    static final ObjectMapper objectMapper = new ObjectMapper();
    static final ObjectReader hashmapReader = new ObjectMapper().readerFor(HashMap.class);
    static final ObjectReader arsetReader = new ObjectMapper().readerFor(AccessRightSet.class);
    static final ObjectReader userCtxReader = new ObjectMapper().readerFor(UserContext.class);

    protected final MysqlConnection connection;

    private final MysqlGraphStore graph;
    private final MysqlProhibitionsStore prohibitions;
    private final MysqlObligationsStore obligations;
    private final MysqlUserDefinedPMLStore userDefinedPML;

    public MysqlPolicyStore(Connection connection) throws PMException {
        this.connection = new MysqlConnection(connection);

        this.graph = new MysqlGraphStore(this.connection);
        this.prohibitions = new MysqlProhibitionsStore(this.connection);
        this.obligations = new MysqlObligationsStore(this.connection);
        this.userDefinedPML = new MysqlUserDefinedPMLStore(this.connection);

        verify(this, graph);
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
    public void reset() throws PMException {
        beginTx();

        List<String> sequence = PolicyResetSequence.getSequence();
        try (Statement stmt = connection.getConnection().createStatement()) {
            for (String s : sequence) {
                stmt.executeUpdate(s);
            }
        } catch (SQLException e) {
            rollback();
            throw new MysqlPolicyException(e.getMessage());
        }

        verify(this, graph);

        commit();
    }

    @Override
    public MysqlGraphStore graph() {
        return graph;
    }

    @Override
    public MysqlProhibitionsStore prohibitions() {
        return prohibitions;
    }

    @Override
    public MysqlObligationsStore obligations() {
        return obligations;
    }

    @Override
    public MysqlUserDefinedPMLStore userDefinedPML() {
        return userDefinedPML;
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
    public void verifyAdminPolicyClassNode() throws PMException {
        if (!graph.nodeExists(AdminPolicyNode.ADMIN_POLICY_TARGET.nodeName())) {
            graph.createNodeInternal(AdminPolicyNode.ADMIN_POLICY.nodeName(), PC, new HashMap<>());
        }
    }

    @Override
    public void verifyAdminPolicyAttribute(AdminPolicyNode node, AdminPolicyNode parent) throws PMException {
        if (!graph.nodeExists(node.nodeName())) {
            graph.createNodeInternal(node.nodeName(), OA, new HashMap<>());
        }

        if (!graph.getParents(node.nodeName()).contains(parent.nodeName())) {
            graph.assignInternal(node.nodeName(), parent.nodeName());
        }
    }

    @Override
    public void verifyAdminPolicyConstant(AdminPolicyNode constant) throws PMException {
        try {
            userDefinedPML.createConstant(constant.constantName(), new Value(constant.nodeName()));
        } catch (PMLConstantAlreadyDefinedException e) {
            // ignore this exception as the admin policy constant already existing is not an error
        }
    }
}
