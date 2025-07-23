package gov.nist.csd.pm.core.pap.serialization.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.serialization.PolicyDeserializer;

import gov.nist.csd.pm.core.pap.serialization.json.JSONProhibition.JSONSubject;
import java.util.*;

import static gov.nist.csd.pm.core.common.graph.node.NodeType.*;

public class JSONDeserializer implements PolicyDeserializer {

    @Override
    public void deserialize(PAP pap, String input) throws PMException {
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

        List<JSONProhibition> prohibitions = jsonPolicy.getProhibitions();
        if (prohibitions == null) {
            prohibitions = new ArrayList<>();
        }

        List<JSONObligation> obligations = jsonPolicy.getObligations();
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

        pap.modify().operations().setResourceOperations(resourceOperations);
        createGraph(pap, graph);
        createProhibitions(pap, prohibitions);
        createOperations(pap, operations);
        createRoutines(pap, routines);
        createObligations(pap, obligations);
    }

    private void createProhibitions(PAP pap, List<JSONProhibition> prohibitions) throws PMException {
        for (JSONProhibition prohibition : prohibitions) {
            JSONSubject subject = prohibition.getSubject();
            pap.modify().prohibitions().createProhibition(
                prohibition.getName(),
                subject.getNode() != null ? new ProhibitionSubject(subject.getNode()) : new ProhibitionSubject(subject.getProcess()),
                new AccessRightSet(prohibition.getArset()),
                prohibition.getIntersection(),
                prohibition.getContainers()
            );
        }
    }

    private void createObligations(PAP pap, List<JSONObligation> obligations) throws PMException {
        for (JSONObligation obligation : obligations) {
            pap.executePML(new UserContext(obligation.getAuthor()), obligation.getPml());
        }
    }

    private void createOperations(PAP pap, List<String> operations) throws PMException {
        String pml = "";
        for (String operation : operations) {
            pml += operation + "\n";
        }

        // author doesnt matter when executing create operation statements
        pap.executePML(new UserContext(0), pml);
    }

    private void createRoutines(PAP pap, List<String> routines) throws PMException {
        String pml = "";
        for (String routine : routines) {
            pml += routine + "\n";
        }

        // author doesnt matter when executing create routine statements
        pap.executePML(new UserContext(0), pml);
    }

    private void createGraph(PAP pap, JSONGraph graph)
            throws PMException {
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

        createNodes(pap, PC, graph.pcs);
        createNodes(pap, UA, graph.uas);
        createNodes(pap, OA, graph.oas);
        createNodes(pap, U, graph.users);
        createNodes(pap, O, graph.objects);

        createAssignments(pap, graph.uas);
        createAssignments(pap, graph.oas);
        createAssignments(pap, graph.users);
        createAssignments(pap, graph.objects);

        createAssociations(pap, graph.uas);
    }

    private void createAssignments(PAP pap, List<JSONNode> nodes) throws PMException {
        for (JSONNode node : nodes) {
            Collection<Long> assignments = node.getAssignments();
            if (assignments == null) {
                continue;
            }

            for (Long assignment : assignments) {
                pap.policyStore().graph().createAssignment(node.getId(), assignment);
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

    private void createAssociations(PAP pap, List<JSONNode> uas) throws PMException {
        for (JSONNode ua : uas) {
            List<JSONAssociation> associations = ua.getAssociations();
            if (associations == null) {
                continue;
            }

            for (JSONAssociation jsonAssociation : associations) {
                pap.modify().graph().associate(ua.getId(), jsonAssociation.getTarget(), jsonAssociation.getArset());
            }
        }
    }

    private void createNodes(PAP pap, NodeType type, List<JSONNode> nodes) throws PMException {
        for (JSONNode node : nodes) {
            pap.policyStore().graph().createNode(node.getId(), node.getName(), type);
            pap.policyStore().graph().setNodeProperties(node.getId(), jsonPropertiesToMap(node.getProperties()));
        }
    }
}
