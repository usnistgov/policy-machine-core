package gov.nist.csd.pm.pap.serialization.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssignmentException;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.model.expression.Literal;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.VarStatement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.OA;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.UA;

public class JSONDeserializer {

    public static void fromJSON(PolicyStore policyStore, UserContext author, String json, FunctionDefinitionStatement... customPMLFunctions)
            throws PMException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JSONPolicy jsonPolicy = objectMapper.readValue(json, JSONPolicy.class);

            createGraph(policyStore, jsonPolicy.getGraph());
            createProhibitions(policyStore, jsonPolicy.getProhibitions());
            createObligations(policyStore, author, customPMLFunctions, jsonPolicy.getObligations());
            createUserDefinedPML(policyStore, author, customPMLFunctions, jsonPolicy.getUserDefinedPML());

            // TODO
        } catch (JsonProcessingException e) {
            throw new PMException(e);
        }
    }

    private static void createUserDefinedPML(PolicyStore policyStore, UserContext author,
                                             FunctionDefinitionStatement[] customPMLFunctions, JSONUserDefinedPML userDefinedPML)
            throws PMException {
        Map<String, String> constants = userDefinedPML.getConstants();
        for (Map.Entry<String, String> e : constants.entrySet()) {
            VarStatement varStatement = new VarStatement(e.getKey(), new Expression(new Literal(e.getValue())), true);
            PMLDeserializer.fromPML(policyStore, author, varStatement.toString(), customPMLFunctions);
        }

        Map<String, String> functions = userDefinedPML.getFunctions();
        for (Map.Entry<String, String> e : functions.entrySet()) {
            PMLDeserializer.fromPML(policyStore, author, e.getValue(), customPMLFunctions);
        }
    }

    private static void createObligations(PolicyStore policyStore, UserContext author,
                                          FunctionDefinitionStatement[] customPMLFunctions, List<String> obligations)
            throws PMException {
        for (String obligationStr : obligations) {
            PMLDeserializer.fromPML(policyStore, author, obligationStr, customPMLFunctions);
        }
    }

    private static void createProhibitions(PolicyStore policyStore, List<Prohibition> prohibitions)
            throws UnknownAccessRightException, ProhibitionExistsException, ProhibitionSubjectDoesNotExistException,
                   PMBackendException, ProhibitionContainerDoesNotExistException {
        for (Prohibition prohibition : prohibitions) {
            policyStore.prohibitions().create(
                    prohibition.getName(),
                    prohibition.getSubject(),
                    prohibition.getAccessRightSet(),
                    prohibition.isIntersection(),
                    prohibition.getContainers().toArray(new ContainerCondition[0])
            );
        }
    }

    private static void createGraph(PolicyStore policyStore, JSONGraph graph)
            throws AdminAccessRightExistsException, PMBackendException, NodeNameExistsException,
                   NodeDoesNotExistException, InvalidAssignmentException, AssignmentCausesLoopException {
        if (graph.resourceAccessRights != null) {
            policyStore.graph().setResourceAccessRights(graph.resourceAccessRights);
        }

        if (graph.policyClasses != null) {
            for (JSONPolicyClass policyClass : graph.policyClasses) {
                createPolicyClass(policyStore, policyClass);
            }
        }
    }

    private static void createPolicyClass(PolicyStore policyStore, JSONPolicyClass policyClass)
            throws PMBackendException, NodeNameExistsException, NodeDoesNotExistException, InvalidAssignmentException,
                   AssignmentCausesLoopException {
        String name = policyClass.getName();
        Map<String, String> properties = policyClass.getProperties();
        List<JSONNode> userAttributes = policyClass.getUserAttributes();
        List<JSONNode> objectAttributes = policyClass.getObjectAttributes();

        // create policy class node
        policyStore.graph().createPolicyClass(name, properties == null ? new HashMap<>() : properties);
        createAttributes(policyStore, UA, name, userAttributes);
        createAttributes(policyStore, OA, name, objectAttributes);
    }

    private static void createAttributes(PolicyStore policyStore, NodeType type, String parent, List<JSONNode> attrs)
            throws NodeDoesNotExistException, InvalidAssignmentException, PMBackendException,
                   AssignmentCausesLoopException, NodeNameExistsException {
        for (JSONNode attr : attrs) {
            String name = attr.getName();
            if (policyStore.graph().nodeExists(name)) {
                policyStore.graph().assign(attr.getName(), parent);
            } else {

                Map<String, String> properties = attr.getProperties() == null ? new HashMap<>() : attr.getProperties();
                if (type == UA) {
                    policyStore.graph().createUserAttribute(name, properties, parent);
                } else {
                    policyStore.graph().createObjectAttribute(name, properties, parent);
                }
            }

            createAttributes(policyStore, type, name, attr.getChildren());
        }
    }
}
