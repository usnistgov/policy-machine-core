package gov.nist.csd.pm.pap.serialization.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicyDeserializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;

public class JSONDeserializer implements PolicyDeserializer {

    private FunctionDefinitionStatement[] customPMLFunctions;

    public JSONDeserializer(FunctionDefinitionStatement... customPMLFunctions) {
        this.customPMLFunctions = customPMLFunctions;
    }

    public void setCustomPMLFunctions(FunctionDefinitionStatement[] customPMLFunctions) {
        this.customPMLFunctions = customPMLFunctions;
    }

    @Override
    public void deserialize(Policy policy, UserContext author, String input) throws PMException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JSONPolicy jsonPolicy = objectMapper.readValue(input, JSONPolicy.class);

            createUserDefinedPML(policy, author, customPMLFunctions, jsonPolicy.getUserDefinedPML());
            createGraph(policy, jsonPolicy.getGraph());
            createProhibitions(policy, jsonPolicy.getProhibitions());
            createObligations(policy, author, customPMLFunctions, jsonPolicy.getObligations());
        } catch (JsonProcessingException e) {
            throw new PMException(e);
        }
    }

    private void createUserDefinedPML(Policy policy, UserContext author,
                                      FunctionDefinitionStatement[] customPMLFunctions, JSONUserDefinedPML userDefinedPML)
            throws PMException {
        Map<String, String> constants = userDefinedPML.getConstants();
        for (Map.Entry<String, String> e : constants.entrySet()) {
            String constPML = "const " + e.getKey() + " = " + e.getValue();
            PMLDeserializer pmlDeserializer = new PMLDeserializer(customPMLFunctions);
            pmlDeserializer.deserialize(policy, author, constPML);
        }

        Map<String, String> functions = userDefinedPML.getFunctions();
        for (Map.Entry<String, String> e : functions.entrySet()) {
            PMLDeserializer pmlDeserializer = new PMLDeserializer(customPMLFunctions);
            pmlDeserializer.deserialize(policy, author, e.getValue());
        }
    }

    private void createObligations(Policy policy, UserContext author,
                                   FunctionDefinitionStatement[] customPMLFunctions, List<String> obligations)
            throws PMException {
        for (String obligationStr : obligations) {
            PMLDeserializer pmlDeserializer = new PMLDeserializer(customPMLFunctions);
            pmlDeserializer.deserialize(policy, author, obligationStr);
        }
    }

    private void createProhibitions(Policy policy, List<Prohibition> prohibitions)
            throws PMException {
        for (Prohibition prohibition : prohibitions) {
            policy.prohibitions().create(
                    prohibition.getName(),
                    prohibition.getSubject(),
                    prohibition.getAccessRightSet(),
                    prohibition.isIntersection(),
                    prohibition.getContainers().toArray(new ContainerCondition[0])
            );
        }
    }

    private void createGraph(Policy policy, JSONGraph graph)
            throws PMException {
        if (graph.resourceAccessRights != null) {
            policy.graph().setResourceAccessRights(graph.resourceAccessRights);
        }

        if (graph.policyClasses == null) {
            return;
        }

        // create all policy class nodes first
        for (JSONPolicyClass policyClass : graph.policyClasses) {
            policy.graph().createPolicyClass(policyClass.getName(), policyClass.getProperties());
        }

        // create policy class attribute hierarchies
        for (JSONPolicyClass policyClass : graph.policyClasses) {
            createPolicyClass(policy, policyClass);
        }

        createUserOrObjects(policy, graph.users, U);
        createUserOrObjects(policy, graph.objects, O);
    }

    private void createUserOrObjects(Policy policy, List<JSONUserOrObject> usersOrObjects, NodeType type) throws PMException {
        for (JSONUserOrObject userOrObject : usersOrObjects) {
            List<String> parents = userOrObject.getParents();
            if (parents.isEmpty()) {
                throw new PMException("node " + userOrObject.getName() + " does not have any parents");
            }

            String parent = parents.get(0);
            String[] parentsArr = new String[parents.size()-1];
            for (int i = 0; i < parentsArr.length; i++) {
                parentsArr[i] = parents.get(i+1);
            }

            if (type == U) {
                policy.graph().createUser(userOrObject.getName(), userOrObject.getProperties(), parent, parentsArr);
            } else {
                policy.graph().createObject(userOrObject.getName(), userOrObject.getProperties(), parent, parentsArr);
            }
        }
    }

    private void createPolicyClass(Policy policy, JSONPolicyClass policyClass)
            throws PMException {
        String name = policyClass.getName();
        Map<String, String> properties = policyClass.getProperties();
        List<JSONNode> userAttributes = policyClass.getUserAttributes();
        List<JSONNode> objectAttributes = policyClass.getObjectAttributes();

        // create policy class node
        if (!policy.graph().nodeExists(name)) {
            policy.graph().createPolicyClass(name, properties == null ? new HashMap<>() : properties);
        }

        if (userAttributes != null) {
            createAttributes(policy, UA, name, userAttributes);
        }

        if (objectAttributes != null) {
            createAttributes(policy, OA, name, objectAttributes);
        }

        Map<String, List<JSONAssociation>> associations = policyClass.getAssociations();
        if (associations != null) {
            for (Map.Entry<String, List<JSONAssociation>> e : associations.entrySet()) {
                for (JSONAssociation jsonAssociation : e.getValue()) {
                    policy.graph().associate(e.getKey(), jsonAssociation.getTarget(), jsonAssociation.getArset());
                }
            }
        }
    }

    private void createAttributes(Policy policy, NodeType type, String parent, List<JSONNode> attrs)
            throws PMException {
        if (attrs == null) {
            return;
        }

        for (JSONNode attr : attrs) {
            String name = attr.getName();
            if (policy.graph().nodeExists(name)) {
                policy.graph().assign(attr.getName(), parent);
            } else {

                Map<String, String> properties = attr.getProperties() == null ? new HashMap<>() : attr.getProperties();
                if (type == UA) {
                    policy.graph().createUserAttribute(name, properties, parent);
                } else {
                    policy.graph().createObjectAttribute(name, properties, parent);
                }
            }

            createAttributes(policy, type, name, attr.getChildren());
        }
    }
}
