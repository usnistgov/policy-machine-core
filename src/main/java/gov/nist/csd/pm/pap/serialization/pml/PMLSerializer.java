package gov.nist.csd.pm.pap.serialization.pml;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.pap.serialization.json.*;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.pml.PMLFormatter;
import gov.nist.csd.pm.policy.pml.model.expression.*;
import gov.nist.csd.pm.policy.pml.statement.*;

import java.util.*;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.O;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.U;

public class PMLSerializer implements PolicySerializer {
    @Override
    public String serialize(Policy policy) throws PMException {
        JSONSerializer json = new JSONSerializer();
        JSONPolicy jsonPolicy = json.buildJSONPolicy(policy);

        return serialize(jsonPolicy);
    }

    private String serialize(JSONPolicy jsonPolicy) {
        StringBuilder sb = new StringBuilder();

        sb.append("# user defined pml functions and constants\n");
        sb.append(jsonUserDefinedPMLToPML(jsonPolicy.getUserDefinedPML()));

        sb.append("\n# GRAPH\n");
        sb.append(jsonGraphToPML(jsonPolicy.getGraph()));

        sb.append("\n\n# PROHIBITIONS\n");
        sb.append(jsonProhibitionsToPML(jsonPolicy.getProhibitions()));

        sb.append("\n\n# OBLIGATIONS\n");
        sb.append(jsonObligations(jsonPolicy.getObligations()));

        return sb.toString();
    }

    private String jsonUserDefinedPMLToPML(JSONUserDefinedPML jsonUserDefinedPML) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> constants = jsonUserDefinedPML.getConstants();
        for (Map.Entry<String, String> e : constants.entrySet()) {
            // wrap e.getValue in value constructor to include the quote marks
            sb.append(String.format("const %s = %s", e.getKey(), new Value(e.getValue()))).append("\n");
        }

        sb.append("\n");

        Map<String, String> functions = jsonUserDefinedPML.getFunctions();
        for (Map.Entry<String, String> e : functions.entrySet()) {
            sb.append(PMLFormatter.format(e.getValue())).append("\n\n");
        }

        return sb.toString();
    }

    private String jsonGraphToPML(JSONGraph jsonGraph) {
        StringBuilder pml = new StringBuilder();

        // resource access rights
        ArrayLiteral arrayLiteral = new ArrayLiteral(Type.string());
        for (String ar : jsonGraph.getResourceAccessRights()) {
            arrayLiteral.add(new Expression(new Literal(ar)));
        }
        pml.append(new SetResourceAccessRightsStatement(new Expression(new Literal(arrayLiteral)))).append("\n\n");

        List<JSONPolicyClass> policyClasses = jsonGraph.getPolicyClasses();
        Set<String> createdNodes = new HashSet<>();
        for (JSONPolicyClass policyClass : policyClasses) {
            pml.append(buildPCStatements(policyClass, createdNodes));
        }

        pml.append(buildUsersAndObjectsPML(jsonGraph));

        return pml.append("\n").toString();
    }

    private String jsonProhibitionsToPML(List<Prohibition> prohibitions) {
        StringBuilder pml = new StringBuilder();

        for (Prohibition p : prohibitions) {
            String s = CreateProhibitionStatement.fromProhibition(p).toString();
            pml.append(PMLFormatter.format(s)).append("\n");
        }

        return pml.toString();
    }

    private String jsonObligations(List<String> obligations) {
        StringBuilder pml = new StringBuilder();

        for (String o : obligations) {
            pml.append(PMLFormatter.format(o)).append("\n");
        }

        return pml.toString();
    }

    private String buildUsersAndObjectsPML(JSONGraph jsonGraph) {
        StringBuilder sb = new StringBuilder();

        sb.append("# users\n");
        for (JSONUserOrObject jsonUserOrObject : jsonGraph.getUsers()) {
            sb.append(jsonUserOrObjectToString(jsonUserOrObject, U));
        }

        sb.append("\n# objects\n");
        for (JSONUserOrObject jsonUserOrObject : jsonGraph.getObjects()) {
            sb.append(jsonUserOrObjectToString(jsonUserOrObject, O));
        }

        return sb.toString();
    }

    private String jsonUserOrObjectToString(JSONUserOrObject jsonUserOrObject, NodeType type) {
        StringBuilder sb = new StringBuilder();

        sb.append(new CreateUserOrObjectStatement(
                buildNameExpression(jsonUserOrObject.getName()),
                type,
                setToExpression(new HashSet<>(jsonUserOrObject.getParents()))
        )).append("\n");

        SetNodePropertiesStatement setNodePropertiesStatement =
                buildSetNodePropertiesStatement(jsonUserOrObject.getName(), jsonUserOrObject.getProperties());
        if (setNodePropertiesStatement != null) {
            sb.append(setNodePropertiesStatement).append("\n");
        }

        return sb.toString();

    }

    private String buildPCStatements(JSONPolicyClass policyClass, Set<String> createdNodes) {
        StringBuilder pml = new StringBuilder();

        pml.append(buildCreatePCStatement(policyClass)).append("\n");
        pml.append("# ").append(policyClass.getName()).append(" user attributes").append("\n");
        pml.append(buildAttributesStatements(policyClass.getName(), NodeType.UA, policyClass.getUserAttributes(), createdNodes)).append("\n");
        pml.append("# ").append(policyClass.getName()).append(" object attributes").append("\n");
        pml.append(buildAttributesStatements(policyClass.getName(), NodeType.OA, policyClass.getObjectAttributes(), createdNodes)).append("\n");
        pml.append("# ").append(policyClass.getName()).append(" associations").append("\n");
        pml.append(buildAssociationStatements(policyClass.getAssociations())).append("\n\n");

        return pml.toString();
    }

    private String buildCreatePCStatement(JSONPolicyClass policyClass) {
        StringBuilder pml = new StringBuilder();

        String pc = policyClass.getName();

        pml.append("# ").append(pc).append("\n");

        if (!AdminPolicy.isAdminPolicyNodeName(pc)) {
            pml.append(new CreatePolicyStatement(buildNameExpression(pc))).append("\n");
            SetNodePropertiesStatement setNodePropertiesStatement = buildSetNodePropertiesStatement(pc, policyClass.getProperties());
            if (setNodePropertiesStatement != null) {
                pml.append(setNodePropertiesStatement).append("\n");
            }
        }

        return pml.toString();
    }

    private String buildAttributesStatements(String parent, NodeType type, List<JSONNode> jsonAttrs,
                                             Set<String> createdNodes) {
        if (jsonAttrs == null || jsonAttrs.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (JSONNode attrJSONNode : jsonAttrs) {
            String name = attrJSONNode.getName();

            Expression assignTo = new Expression(
                    new Literal(new ArrayLiteral(new Expression[]{buildNameExpression(parent)}, Type.string()))
            );

            // create attr or assign to parent
            if (createdNodes.contains(name)) {
                sb.append(new AssignStatement(buildNameExpression(name), assignTo)).append("\n");
            } else if (!AdminPolicy.isAdminPolicyNodeName(name)){
                // add create node statement
                sb.append(new CreateAttrStatement(buildNameExpression(name), type, assignTo)).append("\n");

                // add set node properties stmt if exists
                SetNodePropertiesStatement setNodePropertiesStatement =
                        buildSetNodePropertiesStatement(name, attrJSONNode.getProperties());
                if (setNodePropertiesStatement != null) {
                    sb.append(setNodePropertiesStatement).append("\n");
                }

                // mark the node as created
                createdNodes.add(name);
            }

            // create children
            sb.append(buildAttributesStatements(name, type, attrJSONNode.getChildren(), createdNodes));
        }

        return sb.toString();
    }

    private String buildAssociationStatements(Map<String, List<JSONAssociation>> jsonAssociations) {
        if (jsonAssociations == null || jsonAssociations.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, List<JSONAssociation>> e : jsonAssociations.entrySet()) {
            String ua = e.getKey();
            List<JSONAssociation> associations = e.getValue();

            for (JSONAssociation association : associations) {
                AssociateStatement associateStatement = new AssociateStatement(
                        buildNameExpression(e.getKey()),
                        buildNameExpression(association.getTarget()),
                        setToExpression(association.getArset())
                );

                sb.append(associateStatement).append("\n");
            }
        }

        return sb.toString();
    }

    private Expression buildNameExpression(String name) {
        if (AdminPolicy.isAdminPolicyNodeName(name)) {
            return new Expression(new VariableReference(
                    AdminPolicyNode.fromNodeName(name).constantName(),
                    Type.string()
            ));
        }

        return new Expression(new Literal(name));
    }

    private Expression setToExpression(Set<String> set) {
        Expression[] expressions = new Expression[set.size()];
        int i = 0;
        for (String s : set) {
            expressions[i] = buildNameExpression(s);
            i++;
        }

        return new Expression(new Literal(
                new ArrayLiteral(
                        expressions,
                        Type.string()
                )));
    }

    private SetNodePropertiesStatement buildSetNodePropertiesStatement(String name, Map<String, String> properties) {
        if (properties == null || properties.isEmpty()) {
            return null;
        }

        Map<Expression, Expression> propertiesExpressions = new HashMap<>();
        for (Map.Entry<String, String> property : properties.entrySet()) {
            propertiesExpressions.put(
                    new Expression(new Literal(property.getKey())),
                    new Expression(new Literal(property.getValue()))
            );
        }

        return new SetNodePropertiesStatement(
                buildNameExpression(name),
                new Expression(new Literal(new MapLiteral(propertiesExpressions, Type.string(), Type.string())))
        );
    }
}
