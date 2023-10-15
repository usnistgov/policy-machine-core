package gov.nist.csd.pm.pap.serialization.json;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.*;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

public class JSONSerializer implements PolicySerializer {

    private Map<Association, List<String>> delayedAssociations;
    private Set<String> createdPCs;
    private Set<String> createdAttrs;
    private Policy policy;

    public JSONSerializer() {}

    @Override
    public String serialize(Policy policy) throws PMException {
        return buildJSONPolicy(policy)
                .toString();
    }

    public JSONPolicy buildJSONPolicy(Policy policy) throws PMException {
        resetBuild(policy);

        return new JSONPolicy(
                buildGraphJSON(policy),
                buildProhibitionsJSON(policy),
                buildObligationsJSON(policy),
                buildUserDefinedPML(policy)
        );
    }

    private void resetBuild(Policy policy) throws PMException {
        this.delayedAssociations = new HashMap<>();
        this.createdPCs = new HashSet<>();
        this.createdAttrs = new HashSet<>();
        this.policy = policy;
    }


    private JSONUserDefinedPML buildUserDefinedPML(Policy policy) throws PMException {
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

    private List<String> buildObligationsJSON(Policy policy) throws PMException {
        List<String> jsonObligations = new ArrayList<>();
        List<Obligation> all = policy.obligations().getAll();
        for (Obligation obligation : all) {
            jsonObligations.add(obligation.toString());
        }

        return jsonObligations;
    }

    private List<Prohibition> buildProhibitionsJSON(Policy policy) throws PMException {
        List<Prohibition> prohibitions = new ArrayList<>();
        Map<String, List<Prohibition>> all = policy.prohibitions().getAll();
        for (List<Prohibition> value : all.values()) {
            prohibitions.addAll(value);
        }

        return prohibitions;
    }

    private JSONGraph buildGraphJSON(Policy policy) throws PMException {
        return new JSONGraph(
                policy.graph().getResourceAccessRights(),
                buildPolicyClasses(policy),
                buildUsersOrObjects(policy, U),
                buildUsersOrObjects(policy, O)
        );
    }

    private List<JSONUserOrObject> buildUsersOrObjects(Policy policy, NodeType type)
            throws PMException {
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

    private List<JSONPolicyClass> buildPolicyClasses(Policy policy) throws PMException {
        List<JSONPolicyClass> policyClassesList = new ArrayList<>();

        List<String> policyClasses = policy.graph().getPolicyClasses();
        for (String pc : policyClasses) {
            JSONPolicyClass jsonPolicyClass = buildJSONPolicyCLass(pc);

            createdPCs.add(pc);

            // ignore the creation of the admin policy class - it is done automatically
            if (jsonPolicyClass.getName().equals(AdminPolicyNode.ADMIN_POLICY.nodeName())) {
                continue;
            }

            policyClassesList.add(jsonPolicyClass);
        }

        return policyClassesList;
    }

    private JSONPolicyClass buildJSONPolicyCLass(String pc) throws PMException {
        List<Association> associations = new ArrayList<>();

        // uas
        List<JSONNode> userAttributes = getAttributes(pc, UA, associations);

        // oas
        List<JSONNode> objectAttributes = getAttributes(pc, OA, associations);

        // associations
        Map<String, List<JSONAssociation>> jsonAssociations = new HashMap<>();
        for (Association association : associations) {
            List<String> waitingFor = delayedAssociations.getOrDefault(association, new ArrayList<>());
            waitingFor.removeAll(createdAttrs);

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

        // check delayed associations
        Iterator<Map.Entry<Association, List<String>>> iterator = delayedAssociations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Association, List<String>> next = iterator.next();
            Association association = next.getKey();
            List<String> waitingFor = next.getValue();
            waitingFor.removeAll(createdAttrs);

            if (waitingFor.isEmpty()) {
                List<JSONAssociation> nodeAssociations = jsonAssociations.getOrDefault(association.getSource(), new ArrayList<>());
                nodeAssociations.add(new JSONAssociation(association.getTarget(), association.getAccessRightSet()));
                jsonAssociations.put(association.getSource(), nodeAssociations);

                iterator.remove();
            }
        }

        Node node = policy.graph().getNode(pc);
        boolean isAdminNode = AdminPolicy.isAdminPolicyNodeName(pc);

        JSONPolicyClass jsonPolicyClass = new JSONPolicyClass();
        jsonPolicyClass.setName(pc);
        if (!isAdminNode && !node.getProperties().isEmpty()) {
            jsonPolicyClass.setProperties(node.getProperties());
        }
        if (!isAdminNode && !userAttributes.isEmpty()) {
            jsonPolicyClass.setUserAttributes(userAttributes);
        }
        if (!isAdminNode && !objectAttributes.isEmpty()) {
            jsonPolicyClass.setObjectAttributes(objectAttributes);
        }
        if (!jsonAssociations.isEmpty()) {
            jsonPolicyClass.setAssociations(jsonAssociations);
        }

        return jsonPolicyClass;
    }

    private List<JSONNode> getAttributes(String start, NodeType type, List<Association> associations) throws PMException {
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

            JSONNode jsonNode = new JSONNode();
            jsonNode.setName(child);

            if (!node.getProperties().isEmpty() && !createdAttrs.contains(child)) {
                jsonNode.setProperties(node.getProperties());
            }

            createdAttrs.add(child);

            List<JSONNode> childAttrs = getAttributes(child, type, associations);
            if (!childAttrs.isEmpty()) {
                jsonNode.setChildren(childAttrs);
            }

            jsonNodes.add(jsonNode);
        }

        return jsonNodes;
    }
}
