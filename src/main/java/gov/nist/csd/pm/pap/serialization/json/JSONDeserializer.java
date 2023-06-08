package gov.nist.csd.pm.pap.serialization.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.serialization.PolicyDeserializer;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.common.graph.node.NodeType;

import java.util.*;

import static gov.nist.csd.pm.common.graph.node.NodeType.*;

public class JSONDeserializer implements PolicyDeserializer {

    @Override
    public void deserialize(PAP pap, UserContext author, String input) throws PMException {
        Gson gson = new Gson();
        JSONPolicy jsonPolicy = gson.fromJson(input, new TypeToken<JSONPolicy>() {}.getType());

        pap.modify().operations().setResourceOperations(jsonPolicy.getResourceOperations());

        createGraph(pap, jsonPolicy.getGraph());
        createRestOfPolicy(
                pap,
                author,
                jsonPolicy.getProhibitions(),
                jsonPolicy.getObligations(),
                jsonPolicy.getOperations(),
                jsonPolicy.getRoutines()
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
        createPCs(pap, graph);

        // create uas
        createNodes(pap, UA, graph.uas);

        // create oas
        createNodes(pap, OA, graph.oas);

        // associate uas and uas/oas
        createAssociations(pap, graph.uas);

        // create u and o
        createNodes(pap, U, graph.users);
        createNodes(pap, O, graph.objects);
    }

    private void createPCs(PAP pap, JSONGraph graph) throws PMException {
        // create all policy class nodes first
        for (Map.Entry<String, JSONPolicyClass> policyClass : graph.pcs.entrySet()) {
            Map<String, String> properties = policyClass.getValue().getProperties();
            if (properties == null) {
                properties = new HashMap<>();
            }

            pap.modify().graph().createPolicyClass(policyClass.getKey());
        }
    }

    private void createAssociations(PAP pap, Map<String, JSONNode> uas) throws PMException {
        for (Map.Entry<String, JSONNode> entry : uas.entrySet()) {
            String ua = entry.getKey();
            JSONNode jsonNode = entry.getValue();
            Map<String, AccessRightSet> associations = jsonNode.getAssociations();
            if (associations == null) {
                continue;
            }

            for (Map.Entry<String, AccessRightSet> association : associations.entrySet()) {
                pap.modify().graph().associate(ua, association.getKey(), association.getValue());
            }
        }
    }

    private void createNodes(PAP pap, NodeType type, Map<String, JSONNode> nodes)
            throws PMException {
        Set<Map.Entry<String, JSONNode>> entries = nodes.entrySet();
        for (Map.Entry<String, JSONNode> entry : entries) {
            String name = entry.getKey();
            createNode(pap, name, type, nodes);
        }
    }

    private void createNode(PAP pap, String name, NodeType type, Map<String, JSONNode> nodes) throws PMException {
        if (pap.query().graph().nodeExists(name)) {
            return;
        }

        JSONNode jsonNode = nodes.get(name);

        Collection<String> assignments = jsonNode.getAssignments();
        boolean created = false;
        for (String assignment : assignments) {
            if (!pap.query().graph().nodeExists(assignment)) {
                createNode(pap, assignment, type, nodes);
            }
            createOrAssign(pap, created, name, type, jsonNode, assignment);
            created = true;
        }
    }

    private void createOrAssign(PAP pap, boolean create, String name, NodeType type, JSONNode node, String assignment) throws PMException {
        if (!create) {
            // create node
            createNode(pap, type, name, node, List.of(assignment));

            // set properties
            if (node.getProperties() != null) {
                pap.modify().graph().setNodeProperties(name, node.getProperties());
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
