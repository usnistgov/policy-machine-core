/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.serialization.json;

import static gov.nist.ngac.pm.core.common.graph.node.NodeType.O;
import static gov.nist.ngac.pm.core.common.graph.node.NodeType.OA;
import static gov.nist.ngac.pm.core.common.graph.node.NodeType.PC;
import static gov.nist.ngac.pm.core.common.graph.node.NodeType.U;
import static gov.nist.ngac.pm.core.common.graph.node.NodeType.UA;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.serialization.PolicyDeserializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link PolicyDeserializer} that rebuilds policy state from a {@link JSONPolicy} document, tolerating
 * null sections in the JSON schema.
 */
public class JSONDeserializer implements PolicyDeserializer {

    @Override
    public void deserialize(PAP pap, String input) throws PMException {
        Gson gson = new Gson();
        JSONPolicy jsonPolicy = gson.fromJson(input, new TypeToken<JSONPolicy>() {}.getType());

        // account for the json schema allowing null properties
        AccessRightSet resourceOperations = jsonPolicy.getResourceAccessRights();
        if (resourceOperations == null) {
            resourceOperations = new AccessRightSet();
        }

        JSONGraph graph = jsonPolicy.getGraph();
        if (graph == null) {
            graph = new JSONGraph();
        }

        List<Prohibition> prohibitions = jsonPolicy.getProhibitions();
        if (prohibitions == null) {
            prohibitions = new ArrayList<>();
        }

        List<JSONObligation> obligations = jsonPolicy.getObligations();
        if (obligations == null) {
            obligations = new ArrayList<>();
        }

        JSONOperations operations = jsonPolicy.getOperations();
        if (operations == null) {
            operations = new JSONOperations();
        }

        pap.modify().operations().setResourceAccessRights(resourceOperations);
        createGraph(pap, graph);
        createProhibitions(pap, prohibitions);
        createOperations(pap, operations);
        // do obligations last in case they reference operations
        createObligations(pap, obligations);
    }

    private void createProhibitions(PAP pap, List<Prohibition> prohibitions) throws PMException {
        for (Prohibition prohibition : prohibitions) {
            if (prohibition instanceof NodeProhibition nodeProhibition) {
                pap.modify().prohibitions().createNodeProhibition(
                    nodeProhibition.getName(),
                    nodeProhibition.getNodeId(),
                    new AccessRightSet(nodeProhibition.getAccessRightSet()),
                    nodeProhibition.getInclusionSet(),
                    nodeProhibition.getExclusionSet(),
                    nodeProhibition.isConjunctive()
                );
            } else if (prohibition instanceof ProcessProhibition processProhibition) {
                pap.modify().prohibitions().createProcessProhibition(
                    processProhibition.getName(),
                    processProhibition.getUserId(),
                    processProhibition.getProcess(),
                    new AccessRightSet(processProhibition.getAccessRightSet()),
                    processProhibition.getInclusionSet(),
                    processProhibition.getExclusionSet(),
                    processProhibition.isConjunctive()
                );
            }
        }
    }

    private void createObligations(PAP pap, List<JSONObligation> obligations) throws PMException {
        for (JSONObligation obligation : obligations) {
            pap.executePML(NodeUserContext.of(obligation.getAuthor()), obligation.getPml());
        }
    }

    private void createOperations(PAP pap, JSONOperations operations) throws PMException {
        List<String> all = operations.getAll();

        // combine all operation strings into a single PML block that will be executed
        // this will ensure any operation dependencies are resolved
        String pml = "";
        for (String operation : all) {
            pml += operation + "\n";
        }

        // author doesnt matter when executing create operation statements, only obligations
        pap.executePML(NodeUserContext.of(0), pml);
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
