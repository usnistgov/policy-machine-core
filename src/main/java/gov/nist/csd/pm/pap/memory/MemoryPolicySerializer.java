package gov.nist.csd.pm.pap.memory;

import com.google.gson.Gson;
import gov.nist.csd.pm.policy.Graph;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.json.JSONGraph;
import gov.nist.csd.pm.policy.json.JSONPolicy;
import gov.nist.csd.pm.policy.json.JSONUserDefinedPML;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.pml.PMLSerializer;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.ANY;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.UA;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

public class MemoryPolicySerializer implements PolicySerializer {

    private MemoryPolicyStore memoryPolicyStore;

    public MemoryPolicySerializer(MemoryPolicyStore memoryPolicyStore) {
        this.memoryPolicyStore = memoryPolicyStore;
    }

    @Override
    public String toJSON() throws PMException {
        return new Gson().toJson(toJSONPolicy());
    }

    @Override
    public String toPML() throws PMException {
        return new PMLSerializer(memoryPolicyStore).toPML(false);
    }

    public JSONPolicy toJSONPolicy() throws PMException {
        String jsonGraph = graphToJson();
        String jsonProhibitions = prohibitionsToJson();
        String jsonObligations = obligationsToJson();
        String jsonUserDefinedPML = userDefinedPMLToJson();

        return new JSONPolicy(jsonGraph, jsonProhibitions, jsonObligations, jsonUserDefinedPML);
    }

    private String userDefinedPMLToJson() throws PMException {
        Map<String, byte[]> functionBytes = new HashMap<>();
        Map<String, FunctionDefinitionStatement> functions = memoryPolicyStore.userDefinedPML().getFunctions();
        for (Map.Entry<String, FunctionDefinitionStatement> f : functions.entrySet()) {
            functionBytes.put(f.getKey(), SerializationUtils.serialize(f.getValue()));
        }

        Map<String, byte[]> constantBytes = new HashMap<>();
        Map<String, Value> constants = memoryPolicyStore.userDefinedPML().getConstants();
        for (Map.Entry<String, Value> c : constants.entrySet()) {
            constantBytes.put(c.getKey(), SerializationUtils.serialize(c.getValue()));
        }

        return new Gson().toJson(new JSONUserDefinedPML(
                functionBytes,
                constantBytes
        ));
    }

    private String obligationsToJson() throws PMException {
        List<Obligation> obligations = memoryPolicyStore.obligations().getObligations();
        List<byte[]> bytes = new ArrayList<>();
        for (Obligation o : obligations) {
            bytes.add(SerializationUtils.serialize(o));
        }

        return new Gson().toJson(bytes);
    }

    private String prohibitionsToJson() throws PMException {
        Map<String, List<Prohibition>> prohibitions = memoryPolicyStore.prohibitions().getProhibitions();
        List<byte[]> bytes = new ArrayList<>();
        for (List<Prohibition> proList : prohibitions.values()) {
            for (Prohibition p : proList) {
                bytes.add(SerializationUtils.serialize(p));
            }
        }

        return new Gson().toJson(bytes);
    }

    private String graphToJson() throws PMException {
        Graph graph = memoryPolicyStore.graph();
        AccessRightSet accessRightSet = graph.getResourceAccessRights();
        List<Node> nodes = new ArrayList<>();
        List<String[]> assignments = new ArrayList<>();
        Map<String, Map<String, AccessRightSet>> associations = new HashMap<>();

        List<String> search = graph.search(ANY, NO_PROPERTIES);
        for (String s : search) {
            Node node = graph.getNode(s);
            nodes.add(node);

            List<String> children = graph.getChildren(s);
            for (String c : children) {
                assignments.add(new String[]{c, s});
            }

            if (node.getType() == UA) {
                List<Association> assocs = graph.getAssociationsWithSource(s);

                Map<String, AccessRightSet> assocsMap = associations.getOrDefault(s, new HashMap<>());
                for (Association a : assocs) {
                    assocsMap.put(a.getTarget(), a.getAccessRightSet());
                }

                associations.put(s, assocsMap);
            }
        }

        JSONGraph jsonGraph = new JSONGraph(accessRightSet, nodes, assignments, associations);
        return new Gson().toJson(jsonGraph);
    }

}
