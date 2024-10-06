package gov.nist.csd.pm.pap.serialization.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.serialization.PolicyDeserializer;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.graph.node.NodeType;

import java.util.*;

import static gov.nist.csd.pm.pap.graph.node.NodeType.*;

public class JSONDeserializer implements PolicyDeserializer {

    @Override
    public void deserialize(PAP pap, UserContext author, String input) throws PMException {
        Gson gson = new Gson();
        JSONPolicy jsonPolicy = gson.fromJson(input, new TypeToken<JSONPolicy>() {}.getType());

        // account for the json schema allowing null properties
        AccessRightSet resourceOperations = jsonPolicy.getResourceOperations();
        if (resourceOperations == null) {
            resourceOperations = new AccessRightSet();
        }

        JSONGraph graph = jsonPolicy.getGraph();
        if (graph == null) {
            graph = new JSONGraph();
        }

        List<String> prohibitions = jsonPolicy.getProhibitions();
        if (prohibitions == null) {
            prohibitions = new ArrayList<>();
        }

        List<String> obligations = jsonPolicy.getObligations();
        if (obligations == null) {
            obligations = new ArrayList<>();
        }

        List<String> operations = jsonPolicy.getOperations();
        if (operations == null) {
            operations = new ArrayList<>();
        }

        List<String> routines = jsonPolicy.getRoutines();
        if (routines == null) {
            routines = new ArrayList<>();
        }

        // set resource operations
        pap.modify().operations().setResourceOperations(resourceOperations);

        // create the graph
        createGraph(pap, graph);

        // create prohibitions, obligations, operations, and routines
        createRestOfPolicy(
                pap,
                author,
                prohibitions,
                obligations,
                operations,
                routines
        );
    }

    private void createRestOfPolicy(PAP pap,
                                    UserContext author,
                                    List<String> prohibitions,
                                    List<String> obligations,
                                    List<String> operations,
                                    List<String> routines) throws PMException {
        StringBuilder sb = new StringBuilder();
        for (String prohibition : prohibitions) {
            sb.append(prohibition).append("\n");
        }

        for (String obligation : obligations) {
            sb.append(obligation).append("\n");
        }

        for (String operation : operations) {
            sb.append(operation).append("\n");
        }

        for (String routine : routines) {
            sb.append(routine).append("\n");
        }

        pap.executePML(author, sb.toString());
    }

    private void createGraph(PAP pap, JSONGraph graph)
            throws PMException {
        Map<String, JSONNode> uaMap = new HashMap<>();
        Map<String, JSONNode> oaMap = new HashMap<>();
        Map<String, JSONNode> uMap = new HashMap<>();
        Map<String, JSONNode> oMap = new HashMap<>();

        // account for type maps in json graph being null
        if (graph.pcs == null) {
            graph.pcs = new ArrayList<>();
        }
        if (graph.uas == null) {
            graph.uas = new ArrayList<>();
        }
        if (graph.oas == null) {
            graph.oas = new ArrayList<>();
        }
        if (graph.users == null) {
            graph.users = new ArrayList<>();
        }
        if (graph.objects == null) {
            graph.objects = new ArrayList<>();
        }


        // organize nodes into a map for fast look up during creation
        for (JSONNode jsonNode : graph.uas) {
            uaMap.put(jsonNode.getName(), jsonNode);
        }
        for (JSONNode jsonNode : graph.oas) {
            oaMap.put(jsonNode.getName(), jsonNode);
        }
        for (JSONNode jsonNode : graph.users) {
            uMap.put(jsonNode.getName(), jsonNode);
        }
        for (JSONNode jsonNode : graph.objects) {
            oMap.put(jsonNode.getName(), jsonNode);
        }

        createPCs(pap, graph.pcs);

        // create uas
        createNodes(pap, UA, uaMap);

        // create oas
        createNodes(pap, OA, oaMap);

        // associate uas and uas/oas
        createAssociations(pap, uaMap);

        // create u and o
        createNodes(pap, U, uMap);
        createNodes(pap, O, oMap);
    }

    private void createPCs(PAP pap, List<JSONNode> nodes) throws PMException {
        for (JSONNode policyClass :  nodes) {
            // create node
            pap.modify().graph().createPolicyClass(policyClass.getName());

            // set properties if any
            Map<String, String> properties = jsonPropertiesToMap(policyClass.getProperties());
            if (!properties.isEmpty()) {
                pap.modify().graph().setNodeProperties(policyClass.getName(), properties);
            }
        }
    }

    private Map<String, String> jsonPropertiesToMap(List<JSONProperty> jsonProperties) {
        Map<String, String> properties = new HashMap<>();

        if (jsonProperties == null) {
            return properties;
        }

        for (JSONProperty jsonProperty : jsonProperties) {
            properties.put(jsonProperty.getKey(), jsonProperty.getValue());
        }
        return properties;
    }

    private void createAssociations(PAP pap, Map<String, JSONNode> uas) throws PMException {
        for (Map.Entry<String, JSONNode> entry : uas.entrySet()) {
            JSONNode jsonNode = entry.getValue();
            List<JSONAssociation> associations = jsonNode.getAssociations();
            if (associations == null) {
                continue;
            }

            for (JSONAssociation jsonAssociation : associations) {
                pap.modify().graph().associate(jsonNode.getName(), jsonAssociation.getTarget(), jsonAssociation.getArset());
            }
        }
    }

    private void createNodes(PAP pap, NodeType type, Map<String, JSONNode> nodes)
            throws PMException {
        Set<Map.Entry<String, JSONNode>> entries = nodes.entrySet();
        Set<String> createdNodes = new HashSet<>();
        for (Map.Entry<String, JSONNode> entry : entries) {
            createNode(pap, entry.getValue(), type, nodes, createdNodes);
        }
    }

    private void createNode(PAP pap, JSONNode jsonNode, NodeType type, Map<String, JSONNode> nodes, Set<String> createdNodes) throws PMException {
        if (createdNodes.contains(jsonNode.getName())) {
            return;
        }

        Collection<String> assignments = jsonNode.getAssignments();
        for (String assignment : assignments) {
            if (!pap.query().graph().nodeExists(assignment)) {
                createNode(pap, nodes.get(assignment), type, nodes, createdNodes);
            }

            createOrAssign(pap, createdNodes, jsonNode.getName(), type, jsonNode, assignment);

            createdNodes.add(jsonNode.getName());
        }
    }

    private void createOrAssign(PAP pap, Set<String> createdNodes, String name, NodeType type, JSONNode node, String assignment) throws PMException {
        if (!createdNodes.contains(name)) {
            // create node
            createNode(pap, type, name, node, List.of(assignment));

            // set properties
            if (node.getProperties() != null) {
                pap.modify().graph().setNodeProperties(name, jsonPropertiesToMap(node.getProperties()));
            }
        } else {
            pap.modify().graph().assign(name, List.of(assignment));
        }
    }

    private void createNode(PAP pap, NodeType type, String key, JSONNode value, List<String> existingAssignmentNodes)
            throws PMException {
        switch (type) {
            case OA -> pap.modify().graph().createObjectAttribute(key, existingAssignmentNodes);
            case UA -> pap.modify().graph().createUserAttribute(key, existingAssignmentNodes);
            case O -> pap.modify().graph().createObject(key, value.getAssignments());
            case U -> pap.modify().graph().createUser(key, value.getAssignments());
        }
    }
}
