package gov.nist.csd.pm.pap.serialization.pml;

import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.admin.AdminPolicy;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.pap.pml.expression.literal.MapLiteral;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.pap.pml.statement.operation.*;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.serialization.PolicySerializer;
import gov.nist.csd.pm.pap.query.PolicyQuery;
import gov.nist.csd.pm.pap.serialization.json.*;

import java.util.*;

import static gov.nist.csd.pm.common.graph.node.NodeType.*;

public class PMLSerializer implements PolicySerializer {

    @Override
    public String serialize(PolicyQuery policyQuery) throws PMException {
        JSONSerializer json = new JSONSerializer();
        JSONPolicy jsonPolicy = json.buildJSONPolicy(policyQuery);

        return serialize(jsonPolicy);
    }

    private String serialize(JSONPolicy jsonPolicy) {
        StringBuilder sb = new StringBuilder();

        sb.append("// resource operations\n");
        sb.append(jsonResourceOperations(jsonPolicy.getResourceOperations()));

        sb.append("\n// GRAPH\n");
        sb.append(jsonGraphToPML(jsonPolicy.getGraph()));

        sb.append("\n// PROHIBITIONS\n");
        sb.append(concatStrings(jsonPolicy.getProhibitions()));

        sb.append("\n// OBLIGATIONS\n");
        sb.append(concatStrings(jsonPolicy.getObligations()));

        sb.append("\n// OPERATIONS\n");
        sb.append(concatStrings(jsonPolicy.getOperations()));

        sb.append("\n// ROUTINES\n");
        sb.append(concatStrings(jsonPolicy.getRoutines()));

        return sb.toString();
    }

    private String jsonResourceOperations(AccessRightSet accessRightSet) {
        List<Expression> expressions = new ArrayList<>();
        for (String ar : accessRightSet) {
            expressions.add(new StringLiteral(ar));
        }

        ArrayLiteral arrayLiteral = new ArrayLiteral(expressions, Type.string());

        return new SetResourceOperationsStatement(arrayLiteral).toFormattedString(0) + "\n";
    }

    private String jsonGraphToPML(JSONGraph jsonGraph) {
        StringBuilder pml = new StringBuilder();

        pml.append(buildPolicyClassesPML(jsonGraph));
        pml.append(buildAttributesPML(jsonGraph.getPcs().keySet(), jsonGraph.getUas(), UA));
        pml.append(buildAttributesPML(jsonGraph.getPcs().keySet(), jsonGraph.getOas(), OA));
        pml.append(buildAssociations(jsonGraph.getUas()));
        pml.append(buildUsersAndObjectsPML(jsonGraph));

        return pml.toString();
    }

    private String buildAssociations(Map<String, JSONNode> uas) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, JSONNode> ua : uas.entrySet()) {
            String name = ua.getKey();
            JSONNode node = ua.getValue();

            Map<String, AccessRightSet> associations = node.getAssociations();
            if (associations == null) {
                continue;
            }

            for (Map.Entry<String, AccessRightSet> assoc : associations.entrySet()) {
                sb.append(
                        new AssociateStatement(
                                buildNameExpression(name),
                                buildNameExpression(assoc.getKey()),
                                setToExpression(assoc.getValue())
                        )
                ).append("\n");
            }
        }

        return sb.toString();
    }

    private String buildAttributesPML(Set<String> pcs, Map<String, JSONNode> attrs, NodeType type) {
        StringBuilder pml = new StringBuilder();

        Set<String> createNodes = new HashSet<>(pcs);

        Set<Map.Entry<String, JSONNode>> entries = attrs.entrySet();
        for (Map.Entry<String, JSONNode> entry : entries) {
            String name = entry.getKey();
            createNode(pml, createNodes, name, type, attrs);
        }

        return pml.toString();
    }

    private void createNode(StringBuilder sb, Set<String> createdNodes, String name, NodeType type, Map<String, JSONNode> nodes) {
        if (createdNodes.contains(name)) {
            return;
        }

        JSONNode jsonNode = nodes.get(name);

        Collection<String> assignments = jsonNode.getAssignments();
        boolean created = false;
        for (String assignment : assignments) {
            if (!createdNodes.contains(assignment)) {
                createNode(sb, createdNodes, assignment, type, nodes);
            }
            createOrAssign(sb, createdNodes, created, name, type, jsonNode, assignment);
            created = true;
        }
    }

    private void createOrAssign(StringBuilder sb, Set<String> createdNodes, boolean create, String name, NodeType type, JSONNode node, String assignment) {
        if (!create) {
            sb.append(jsonNodeToPML(createdNodes, name, type, node, List.of(assignment)));
            createdNodes.add(name);
        } else {
            sb.append(new AssignStatement(
                    buildNameExpression(name),
                    setToExpression(Set.of(assignment))
            )).append("\n");
        }
    }

    private String buildPolicyClassesPML(JSONGraph jsonGraph) {
        StringBuilder sb = new StringBuilder();

        sb.append("// policy classes\n");

        Map<String, JSONPolicyClass> pcs = jsonGraph.getPcs();
        for (Map.Entry<String, JSONPolicyClass> e : pcs.entrySet()) {
            // do not serialize admin policy node
            if (AdminPolicy.isAdminPolicyNodeName(e.getKey())) {
                continue;
            }

            sb.append(new CreatePolicyStatement(
                    buildNameExpression(e.getKey())
            )).append("\n");

            SetNodePropertiesStatement setNodePropertiesStatement =
                    buildSetNodePropertiesStatement(e.getKey(), e.getValue().getProperties());
            if (setNodePropertiesStatement != null) {
                sb.append(setNodePropertiesStatement).append("\n");
            }
        }

        return sb.toString();
    }

    private String buildUsersAndObjectsPML(JSONGraph jsonGraph) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n// users\n");
        for (Map.Entry<String, JSONNode> e : jsonGraph.getUsers().entrySet()) {
            sb.append(jsonNodeToPML(new HashSet<>(), e.getKey(), U, e.getValue(), e.getValue().getAssignments()));
        }

        sb.append("\n// objects\n");
        for (Map.Entry<String, JSONNode> e : jsonGraph.getObjects().entrySet()) {
            sb.append(jsonNodeToPML(new HashSet<>(), e.getKey(), O, e.getValue(), e.getValue().getAssignments()));
        }

        return sb.toString();
    }

    private String jsonNodeToPML(Set<String> seen, String name, NodeType type, JSONNode jsonNode, Collection<String> assignments) {
        StringBuilder sb = new StringBuilder();

        // if node is an admin node, assign to assignments not create
        if (seen.contains(name)) {
            sb.append(new AssignStatement(
                    buildNameExpression(name),
                    setToExpression(new HashSet<>(assignments))
            )).append("\n");
        } else {
            sb.append(new CreateNonPCStatement(
                    buildNameExpression(name),
                    type,
                    setToExpression(new HashSet<>(assignments))
            )).append("\n");
        }

        SetNodePropertiesStatement setNodePropertiesStatement =
                buildSetNodePropertiesStatement(name, jsonNode.getProperties());
        if (setNodePropertiesStatement != null) {
            sb.append(setNodePropertiesStatement).append("\n");
        }

        return sb.toString();

    }

    private String concatStrings(List<String> strings) {
        StringBuilder pml = new StringBuilder();

        for (String s : strings) {
            pml.append(s).append("\n");
        }

        return pml.toString();
    }

    private Expression buildNameExpression(String name) {
        if (AdminPolicy.isAdminPolicyNodeName(name)) {
            return new ReferenceByID(
                    AdminPolicyNode.fromNodeName(name).constantName()
            );
        }

        return new StringLiteral(name);
    }

    private ArrayLiteral setToExpression(Set<String> set) {
        Expression[] expressions = new Expression[set.size()];
        int i = 0;
        for (String s : set) {
            expressions[i] = buildNameExpression(s);
            i++;
        }

        return new ArrayLiteral(
                expressions,
                Type.string()
        );
    }

    private SetNodePropertiesStatement buildSetNodePropertiesStatement(String name, Map<String, String> properties) {
        Expression propertiesExpression = propertiesMapToExpression(properties);
        if (propertiesExpression == null) {
            return null;
        }

        return new SetNodePropertiesStatement(
                buildNameExpression(name),
                propertiesExpression
        );
    }

    private Expression propertiesMapToExpression(Map<String, String> properties) {
        if (properties == null || properties.isEmpty()) {
            return null;
        }

        Map<Expression, Expression> propertiesExpressions = new HashMap<>();
        for (Map.Entry<String, String> property : properties.entrySet()) {
            propertiesExpressions.put(
                    new StringLiteral(property.getKey()),
                    new StringLiteral(property.getValue())
            );
        }

        return new MapLiteral(propertiesExpressions, Type.string(), Type.string());
    }
}
