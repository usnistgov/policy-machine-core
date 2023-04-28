package gov.nist.csd.pm.pap.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.json.JSONGraph;
import gov.nist.csd.pm.policy.json.JSONPolicy;
import gov.nist.csd.pm.policy.json.JSONUserDefinedPML;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.pml.PMLSerializer;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import org.apache.commons.lang3.SerializationUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.mysql.MysqlGraph.getNodeFromResultSet;

public class MysqlPolicySerializer implements PolicySerializer {

    private MysqlPolicyStore policyStore;

    public MysqlPolicySerializer(MysqlPolicyStore policyStore) {
        this.policyStore = policyStore;
    }

    @Override
    public String toJSON() throws PMException {
        String jsonGraph = graphToJson();
        String jsonProhibitions = prohibitionsToJson();
        String jsonObligations = obligationsToJson();
        String jsonUserDefinedPML = userDefinedPMLToJson();

        return new Gson().toJson(new JSONPolicy(jsonGraph, jsonProhibitions, jsonObligations, jsonUserDefinedPML));
    }

    @Override
    public String toPML() throws PMException {
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore(
                policyStore.graph(),
                policyStore.prohibitions(),
                policyStore.obligations(),
                policyStore.userDefinedPML()
        );

        return new PMLSerializer(memoryPolicyStore).toPML(false);
    }

    private String userDefinedPMLToJson() throws PMException {
        Map<String, byte[]> functionBytes = new HashMap<>();
        Map<String, FunctionDefinitionStatement> functions = policyStore.userDefinedPML().getFunctions();
        for (Map.Entry<String, FunctionDefinitionStatement> f : functions.entrySet()) {
            functionBytes.put(f.getKey(), SerializationUtils.serialize(f.getValue()));
        }

        Map<String, byte[]> constantBytes = new HashMap<>();
        Map<String, Value> constants = policyStore.userDefinedPML().getConstants();
        for (Map.Entry<String, Value> c : constants.entrySet()) {
            constantBytes.put(c.getKey(), SerializationUtils.serialize(c.getValue()));
        }

        return new Gson().toJson(new JSONUserDefinedPML(
                functionBytes,
                constantBytes
        ));
    }

    private String obligationsToJson() throws PMException {
        List<Obligation> obligations = policyStore.obligations().getAll();
        List<byte[]> bytes = new ArrayList<>();
        for (Obligation o : obligations) {
            bytes.add(SerializationUtils.serialize(o));
        }

        return new Gson().toJson(bytes);
    }

    private String prohibitionsToJson() throws PMException {
        Map<String, List<Prohibition>> prohibitions = policyStore.prohibitions().getAll();
        List<byte[]> bytes = new ArrayList<>();
        for (List<Prohibition> proList : prohibitions.values()) {
            for (Prohibition p : proList) {
                bytes.add(SerializationUtils.serialize(p));
            }
        }

        return new Gson().toJson(bytes);
    }

    private String graphToJson() throws PMException {
        AccessRightSet accessRightSet = policyStore.graph().getResourceAccessRights();
        JSONGraph jsonGraph = new JSONGraph(accessRightSet, getNodes(), getAssignments(), getAssociations());
        return new Gson().toJson(jsonGraph);
    }

    private List<Node> getNodes() throws MysqlPolicyException {
        String sql = """
                select name, node_type_id, properties from node
                """;
        List<Node> results = new ArrayList<>();
        try (Statement stmt = policyStore.connection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Node node = getNodeFromResultSet(rs);
                results.add(node);
            }
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }

        return results;
    }

    private List<String[]> getAssignments() throws MysqlPolicyException {
        List<String[]> assignments = new ArrayList<>();
        String sql = """
                    SELECT child.name, parent.name FROM assignment
                    join node as child on assignment.start_node_id=child.id
                    join node as parent on assignment.end_node_id=parent.id;
                    """;
        try(PreparedStatement ps = policyStore.connection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                assignments.add(new String[]{rs.getString(1), rs.getString(2)});
            }
        } catch (SQLException e) {
            throw new MysqlPolicyException(e.getMessage());
        }

        return assignments;
    }

    private Map<String, Map<String, AccessRightSet>> getAssociations() throws PMException {
        Map<String, Map<String, AccessRightSet>> associations = new HashMap<>();

        String sql = """
                    SELECT ua.name, target.name, operation_set FROM association
                    join node as ua on association.start_node_id=ua.id
                    join node as target on association.end_node_id=target.id;
                    """;
        try (Statement stmt = policyStore.connection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String ua = rs.getString(1);
                String target = rs.getString(2);
                AccessRightSet arset = MysqlPolicyStore.arsetReader.readValue(rs.getString(3));

                Map<String, AccessRightSet> uaAssocs = associations.getOrDefault(ua, new HashMap<>());
                uaAssocs.put(target, arset);
                associations.put(ua, uaAssocs);
            }
        } catch (SQLException | JsonProcessingException e) {
            throw new MysqlPolicyException(e.getMessage());
        }

        return associations;
    }
}
