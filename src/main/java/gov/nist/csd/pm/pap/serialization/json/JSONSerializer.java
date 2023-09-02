package gov.nist.csd.pm.pap.serialization.json;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.policy.exceptions.NodeDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.PMBackendException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.*;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

public class JSONSerializer {

    public static String toJSON(PolicyStore policy) throws PMException {
        return new JSONPolicy(
                buildGraphJSON(policy),
                buildProhibitionsJSON(policy),
                buildObligationsJSON(policy),
                buildUserDefinedPML(policy)
        ).toString();
    }

    private static JSONUserDefinedPML buildUserDefinedPML(PolicyStore policy) throws PMException {
        Map<String, FunctionDefinitionStatement> functions = policy.userDefinedPML().getFunctions();
        Map<String, String> jsonFunctions = new HashMap<>();
        for (Map.Entry<String, FunctionDefinitionStatement> e : functions.entrySet()) {
            jsonFunctions.put(e.getKey(), e.getValue().toString());
        }

        Map<String, Value> constants = policy.userDefinedPML().getConstants();
        Map<String, String> jsonConstants = new HashMap<>();
        for (Map.Entry<String, Value> e : constants.entrySet()) {
            if (AdminPolicy.isAdminPolicyNodeConstantName(e.getKey())) {
                continue;
            }

            jsonConstants.put(e.getKey(), e.getValue().toString());
        }

        return new JSONUserDefinedPML(jsonFunctions, jsonConstants);
    }

    private static List<String> buildObligationsJSON(PolicyStore policy) throws PMException {
        List<String> jsonObligations = new ArrayList<>();
        List<Obligation> all = policy.obligations().getAll();
        for (Obligation obligation : all) {
            jsonObligations.add(obligation.toString());
        }

        return jsonObligations;
    }

    private static List<Prohibition> buildProhibitionsJSON(PolicyStore policy) throws PMException {
        List<Prohibition> prohibitions = new ArrayList<>();
        Map<String, List<Prohibition>> all = policy.prohibitions().getAll();
        for (List<Prohibition> value : all.values()) {
            prohibitions.addAll(value);
        }

        return prohibitions;
    }

    private static JSONGraph buildGraphJSON(PolicyStore policy) throws PMException {
        return new JSONGraph(
                policy.graph().getResourceAccessRights(),
                buildPolicyClasses(policy),
                buildUsersOrObjects(policy, U),
                buildUsersOrObjects(policy, O)
        );
    }

    private static List<JSONUserOrObject> buildUsersOrObjects(PolicyStore policy, NodeType type)
            throws PMBackendException, NodeDoesNotExistException {
        List<JSONUserOrObject> userOrObjects = new ArrayList<>();

        List<String> search = policy.graph().search(type, NO_PROPERTIES);
        for (String userOrObject : search) {
            JSONUserOrObject jsonUserOrObject = new JSONUserOrObject();
            jsonUserOrObject.setName(userOrObject);

            Node node = policy.graph().getNode(userOrObject);
            if (!node.getProperties().isEmpty()) {
                jsonUserOrObject.setProperties(node.getProperties());
            }

            jsonUserOrObject.setParents(policy.graph().getParents(userOrObject));

            userOrObjects.add(jsonUserOrObject);
        }

        return userOrObjects;
    }

    private static List<JSONPolicyClass> buildPolicyClasses(PolicyStore policy) throws PMException {
        List<JSONPolicyClass> policyClassesList = new ArrayList<>();

        List<String> policyClasses = policy.graph().getPolicyClasses();
        Set<String> attrs = new HashSet<>();
        Map<Association, List<String>> delayedAssociations = new HashMap<>();
        for (String pc : policyClasses) {
            JSONPolicyClass jsonPolicyClass = buildJSONPolicyCLass(policy, attrs, delayedAssociations, pc);
            policyClassesList.add(jsonPolicyClass);
        }

        return policyClassesList;
    }

    private static JSONPolicyClass buildJSONPolicyCLass(PolicyStore policy, Set<String> existingAttrs,
                                                        Map<Association, List<String>> delayedAssociations, String pc)
            throws PMException {
        List<Association> associations = new ArrayList<>();
        List<JSONNode> userAttributes = getAttributes(policy, associations, delayedAssociations, existingAttrs, pc, UA);
        List<JSONNode> objectAttributes = getAttributes(policy, associations, delayedAssociations, existingAttrs, pc, OA);

        // process associations
        Map<String, List<JSONAssociation>> jsonAssociations = new HashMap<>();
        for (Association association : associations) {
            List<String> waitingFor = delayedAssociations.getOrDefault(association, new ArrayList<>());
            waitingFor.removeAll(existingAttrs);

            if (waitingFor.isEmpty()) {
                List<JSONAssociation> nodeAssociations = jsonAssociations.getOrDefault(association.getSource(), new ArrayList<>());
                nodeAssociations.add(new JSONAssociation(association.getTarget(), association.getAccessRightSet()));
                jsonAssociations.put(association.getSource(), nodeAssociations);

                delayedAssociations.remove(association);
            } else {
                // update the list of nodes the association is waiting for
                delayedAssociations.put(association, waitingFor);
            }
        }

        Iterator<Map.Entry<Association, List<String>>> iterator = delayedAssociations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Association, List<String>> next = iterator.next();
            Association association = next.getKey();
            List<String> waitingFor = next.getValue();
            waitingFor.removeAll(existingAttrs);

            if (waitingFor.isEmpty()) {
                List<JSONAssociation> nodeAssociations = jsonAssociations.getOrDefault(association.getSource(), new ArrayList<>());
                nodeAssociations.add(new JSONAssociation(association.getTarget(), association.getAccessRightSet()));
                jsonAssociations.put(association.getSource(), nodeAssociations);

                iterator.remove();
            }
        }

        Node node = policy.graph().getNode(pc);

        JSONPolicyClass jsonPolicyClass = new JSONPolicyClass();
        jsonPolicyClass.setName(pc);
        if (!node.getProperties().isEmpty()) {
            jsonPolicyClass.setProperties(node.getProperties());
        }
        if (!userAttributes.isEmpty()) {
            jsonPolicyClass.setUserAttributes(userAttributes);
        }
        if (!objectAttributes.isEmpty()) {
            jsonPolicyClass.setObjectAttributes(objectAttributes);
        }
        if (!jsonAssociations.isEmpty()) {
            jsonPolicyClass.setAssociations(jsonAssociations);
        }

        return jsonPolicyClass;
    }

    private static List<JSONNode> getAttributes(PolicyStore policy, 
                                                List<Association> associations,
                                                Map<Association, List<String>> delayedAssociations,
                                                Set<String> existingAttrs,
                                                String start, NodeType type) throws PMException {
        List<JSONNode> jsonNodes = new ArrayList<>();
        List<String> children = policy.graph().getChildren(start);
        for(String child : children) {
            Node node = policy.graph().getNode(child);

            if (node.getType() != type) {
                continue;
            }

            List<Association> nodeAssociations = policy.graph().getAssociationsWithTarget(node.getName());
            for (Association association : nodeAssociations) {
                List<String> waitingFor = new ArrayList<>(List.of(association.getSource()));
                if (!AdminPolicy.isAdminPolicyNodeName(association.getTarget())) {
                    waitingFor.add(association.getTarget());
                }

                delayedAssociations.put(association, waitingFor);
            }

            associations.addAll(nodeAssociations);
            existingAttrs.add(child);

            JSONNode jsonNode = new JSONNode();
            jsonNode.setName(child);

            if (!node.getProperties().isEmpty() && !existingAttrs.contains(child)) {
                jsonNode.setProperties(node.getProperties());
            }

            List<JSONNode> childAttrs = getAttributes(policy, associations, delayedAssociations, existingAttrs, child, type);
            if (!childAttrs.isEmpty()) {
                jsonNode.setChildren(childAttrs);
            }

            jsonNodes.add(jsonNode);
        }

        return jsonNodes;
    }
}
