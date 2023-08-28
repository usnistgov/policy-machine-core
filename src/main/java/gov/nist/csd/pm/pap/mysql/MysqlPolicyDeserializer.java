package gov.nist.csd.pm.pap.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gov.nist.csd.pm.policy.PolicyDeserializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.json.JSONGraph;
import gov.nist.csd.pm.policy.json.JSONPolicy;
import gov.nist.csd.pm.policy.json.JSONUserDefinedPML;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import org.apache.commons.lang3.SerializationUtils;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MysqlPolicyDeserializer implements PolicyDeserializer {

    private MysqlPolicyStore policyStore;

    private static ObjectMapper mapper = new ObjectMapper();

    public MysqlPolicyDeserializer(MysqlPolicyStore policyStore) {
        this.policyStore = policyStore;
    }

    @Override
    public void fromJSON(String json) throws PMException {
        JSONPolicy jsonPolicy = new Gson().fromJson(json, JSONPolicy.class);

        policyStore.beginTx();
        policyStore.reset();

        insertGraph(new Gson().fromJson(jsonPolicy.getGraph(), JSONGraph.class));
        insertProhibitions(jsonPolicy.getProhibitions());
        insertObligations(jsonPolicy.getObligations());
        insertUserDefinedPML(jsonPolicy.getUserDefinedPML());

        policyStore.commit();
    }

    @Override
    public void fromPML(UserContext author, String pml, FunctionDefinitionStatement... customFunctions) throws PMException {
        policyStore.beginTx();

        PMLExecutor.compileAndExecutePML(policyStore, author, pml, customFunctions);

        policyStore.commit();
    }

    private void insertUserDefinedPML(String userDefinedPML) throws PMException {
        JSONUserDefinedPML jsonPML =
                new Gson().fromJson(userDefinedPML, JSONUserDefinedPML.class);

        for (Map.Entry<String, byte[]> e : jsonPML.getFunctions().entrySet()) {
            policyStore.userDefinedPML().createFunction(SerializationUtils.deserialize(e.getValue()));
        }

        for (Map.Entry<String, byte[]> e : jsonPML.getConstants().entrySet()) {
            policyStore.userDefinedPML().createConstant(e.getKey(), SerializationUtils.deserialize(e.getValue()));
        }
    }

    private void insertObligations(String obligations) throws PMException {
        Type type = new TypeToken<List<byte[]>>() {}.getType();
        List<byte[]> list = new Gson().fromJson(obligations, type);

        for (byte[] b : list) {
            Obligation obligation = SerializationUtils.deserialize(b);

            List<Rule> rules = obligation.getRules();
            policyStore.obligations().create(
                    obligation.getAuthor(),
                    obligation.getName(),
                    rules.toArray(new Rule[]{})
            );
        }
    }

    private void insertProhibitions(String prohibitions) throws PMException {
        Type type = new TypeToken<List<byte[]>>() {}.getType();
        List<byte[]> list = new Gson().fromJson(prohibitions, type);

        for (byte[] b : list) {
            Prohibition prohibition = SerializationUtils.deserialize(b);

            policyStore.prohibitions().create(
                    prohibition.getName(),
                    prohibition.getSubject(),
                    prohibition.getAccessRightSet(),
                    prohibition.isIntersection(),
                    prohibition.getContainers().toArray(new ContainerCondition[0])
            );
        }
    }

    private void insertGraph(JSONGraph jsonGraph) throws MysqlPolicyException {
        String sql = """
                    INSERT INTO node (node_type_id, name, properties) VALUES (?,?,?)
                    """;
        try(PreparedStatement ps = policyStore.connection.getConnection().prepareStatement(sql)) {
            for (Node node : jsonGraph.getNodes()) {
                ps.setInt(1, MysqlPolicyStore.getNodeTypeId(node.getType()));
                ps.setString(2, node.getName());
                ps.setString(3, MysqlPolicyStore.toJSON(node.getProperties()));
                ps.addBatch();
            }

            ps.executeBatch();
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }

        sql = """
            INSERT INTO assignment (start_node_id, end_node_id) VALUES (
              (SELECT id FROM node WHERE name=?), (SELECT id FROM node WHERE name=?)
            ) ON DUPLICATE KEY UPDATE start_node_id=start_node_id
            """;
        try(PreparedStatement ps = policyStore.connection.getConnection().prepareStatement(sql)) {
            for (String[] s : jsonGraph.getAssignments()) {
                ps.setString(1, s[0]);
                ps.setString(2, s[1]);
                ps.addBatch();
            }

            ps.executeBatch();
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }

        sql = """
            INSERT INTO association (start_node_id, end_node_id, operation_set) VALUES (
              (SELECT id FROM node WHERE name=?), (SELECT id FROM node WHERE name=?), ?
            ) ON DUPLICATE KEY UPDATE operation_set=?
            """;

        try(PreparedStatement ps = policyStore.connection.getConnection().prepareStatement(sql)) {
            Map<String, Map<String, AccessRightSet>> associations = jsonGraph.getAssociations();
            for (Map.Entry<String, Map<String, AccessRightSet>> e1 : associations.entrySet()) {
                String ua = e1.getKey();

                for (Map.Entry<String, AccessRightSet> e2 : e1.getValue().entrySet()) {
                    String target = e2.getKey();
                    AccessRightSet arset = e2.getValue();
                    String json = MysqlPolicyStore.arsetToJson(arset);

                    ps.setString(1, ua);
                    ps.setString(2, target);
                    ps.setString(3, json);
                    ps.setString(4, json);
                    ps.addBatch();
                }
            }

            ps.executeBatch();
        }catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }
    }
}
