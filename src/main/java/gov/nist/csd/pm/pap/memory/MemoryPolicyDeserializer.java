package gov.nist.csd.pm.pap.memory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gov.nist.csd.pm.policy.PolicyDeserializer;
import gov.nist.csd.pm.policy.exceptions.NodeNameExistsException;
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
import gov.nist.csd.pm.policy.pml.PMLSerializer;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import org.apache.commons.lang3.SerializationUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class MemoryPolicyDeserializer implements PolicyDeserializer {

    private static ObjectMapper mapper = new ObjectMapper();

    private MemoryPolicyStore memoryPolicyStore;

    public MemoryPolicyDeserializer(MemoryPolicyStore memoryPolicyStore) {
        this.memoryPolicyStore = memoryPolicyStore;
    }

    @Override
    public void fromJSON(String json) throws PMException {
        JSONPolicy jsonPolicy = JSONPolicy.fromJson(json);

        memoryPolicyStore.beginTx();
        memoryPolicyStore.reset();

        graphFromJson(jsonPolicy.getGraph());
        prohibitionsFromJson(jsonPolicy.getProhibitions());
        obligationsFromJson(jsonPolicy.getObligations());
        userDefinedPMLFromJson(jsonPolicy.getUserDefinedPML());

        memoryPolicyStore.commit();
    }

    @Override
    public void fromPML(UserContext author, String pml, FunctionDefinitionStatement... customFunctions) throws PMException {
        PMLSerializer pmlSerializer = new PMLSerializer(memoryPolicyStore);
        pmlSerializer.fromPML(author, pml, customFunctions);
    }

    private void userDefinedPMLFromJson(String userDefinedPML) throws PMException {
        JSONUserDefinedPML jsonPML =
                new Gson().fromJson(userDefinedPML, JSONUserDefinedPML.class);

        for (Map.Entry<String, byte[]> e : jsonPML.getFunctions().entrySet()) {
            memoryPolicyStore.userDefinedPML().createFunction(SerializationUtils.deserialize(e.getValue()));
        }

        for (Map.Entry<String, byte[]> e : jsonPML.getConstants().entrySet()) {
            memoryPolicyStore.userDefinedPML().createConstant(e.getKey(), SerializationUtils.deserialize(e.getValue()));
        }
    }

    private void obligationsFromJson(String obligations) throws PMException {
        Type type = new TypeToken<List<byte[]>>() {}.getType();
        List<byte[]> list = new Gson().fromJson(obligations, type);

        for (byte[] b : list) {
            Obligation obligation = SerializationUtils.deserialize(b);

            List<Rule> rules = obligation.getRules();
            memoryPolicyStore.obligations().create(
                    obligation.getAuthor(),
                    obligation.getId(),
                    rules.toArray(new Rule[]{})
            );
        }
    }

    private void prohibitionsFromJson(String prohibitions) throws PMException {
        Type type = new TypeToken<List<byte[]>>() {}.getType();
        List<byte[]> list = new Gson().fromJson(prohibitions, type);

        for (byte[] b : list) {
            Prohibition prohibition = SerializationUtils.deserialize(b);

            memoryPolicyStore.prohibitions().create(
                    prohibition.getId(),
                    prohibition.getSubject(),
                    prohibition.getAccessRightSet(),
                    prohibition.isIntersection(),
                    prohibition.getContainers().toArray(new ContainerCondition[0])
            );
        }
    }

    private void graphFromJson(String json) throws NodeNameExistsException {
        JSONGraph jsonGraph = new Gson().fromJson(json, JSONGraph.class);

        MemoryGraph graph = memoryPolicyStore.graph();

        for (Node node : jsonGraph.getNodes()) {
            graph.createNodeInternal(node.getName(), node.getType(), node.getProperties());
        }

        for (String[] assignment : jsonGraph.getAssignments()) {
            graph.assignInternal(assignment[0], assignment[1]);
        }

        for (Map.Entry<String, Map<String, AccessRightSet>> e : jsonGraph.getAssociations().entrySet()) {
            for (Map.Entry<String, AccessRightSet> e2 : e.getValue().entrySet()) {
                graph.associateInternal(e.getKey(), e2.getKey(), e2.getValue());
            }
        }
    }
}
