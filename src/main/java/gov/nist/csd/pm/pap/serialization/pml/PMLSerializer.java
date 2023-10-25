package gov.nist.csd.pm.pap.serialization.pml;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.pap.serialization.json.*;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.MapLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.policy.pml.model.exception.PMLCompilationException;
import gov.nist.csd.pm.policy.pml.statement.*;
import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.*;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;

public class PMLSerializer implements PolicySerializer {

    @Override
    public String serialize(Policy policy) throws PMException {
        JSONSerializer json = new JSONSerializer();
        JSONPolicy jsonPolicy = json.buildJSONPolicy(policy);

        return serialize(jsonPolicy);
    }

    private String serialize(JSONPolicy jsonPolicy) throws PMLCompilationException {
        StringBuilder sb = new StringBuilder();

        sb.append("// user defined pml functions and constants\n");
        sb.append(jsonUserDefinedPMLToPML(jsonPolicy.getUserDefinedPML()));

        sb.append("\n// GRAPH\n");
        sb.append(jsonGraphToPML(jsonPolicy.getGraph()));

        sb.append("\n\n// PROHIBITIONS\n");
        sb.append(jsonProhibitionsToPML(jsonPolicy.getProhibitions()));

        sb.append("\n\n// OBLIGATIONS\n");
        sb.append(jsonObligations(jsonPolicy.getObligations()));

        return sb.toString();
    }

    private String jsonUserDefinedPMLToPML(JSONUserDefinedPML jsonUserDefinedPML) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> constants = jsonUserDefinedPML.getConstants();
        for (Map.Entry<String, String> e : constants.entrySet()) {
            // wrap e.getValue in string literal constructor to include the quote marks
            sb.append(String.format("const %s = %s", e.getKey(), e.getValue())).append("\n");
        }

        sb.append("\n");

        Map<String, String> functions = jsonUserDefinedPML.getFunctions();
        for (Map.Entry<String, String> e : functions.entrySet()) {
            sb.append(e.getValue()).append("\n\n");
        }

        return sb.toString();
    }

    private String jsonGraphToPML(JSONGraph jsonGraph) {
        StringBuilder pml = new StringBuilder();

        // resource access rights
        ArrayLiteral arrayLiteral = new ArrayLiteral(Type.string());
        for (String ar : jsonGraph.getResourceAccessRights()) {
            arrayLiteral.add(new StringLiteral(ar));
        }
        pml.append(new SetResourceAccessRightsStatement(arrayLiteral)).append("\n\n");

        List<JSONPolicyClass> policyClasses = jsonGraph.getPolicyClasses();

        // add create target statements
        for (JSONPolicyClass policyClass : policyClasses) {
            pml.append(new CreateNonPCStatement(
                    buildNameExpression(AdminPolicy.policyClassTargetName(policyClass.getName())),
                    OA,
                    new ArrayLiteral(Type.string(), buildNameExpression(AdminPolicyNode.POLICY_CLASS_TARGETS.nodeName()))
            )).append("\n");
        }

        pml.append("\n");

        for (JSONPolicyClass policyClass : policyClasses) {
            pml.append(buildCreatePCStatement(policyClass)).append("\n");
        }

        pml.append(buildUsersAndObjectsPML(jsonGraph));

        return pml.append("\n").toString();
    }

    private String jsonProhibitionsToPML(List<Prohibition> prohibitions) throws PMLCompilationException {
        StringBuilder pml = new StringBuilder();

        for (Prohibition p : prohibitions) {
            String s = CreateProhibitionStatement.fromProhibition(p).toString();
            pml.append(s).append("\n");
        }

        return pml.toString();
    }

    private String jsonObligations(List<String> obligations) throws PMLCompilationException {
        StringBuilder pml = new StringBuilder();

        for (String o : obligations) {
            pml.append(o).append("\n");
        }

        return pml.toString();
    }

    private String buildUsersAndObjectsPML(JSONGraph jsonGraph) {
        StringBuilder sb = new StringBuilder();

        sb.append("// users\n");
        for (JSONUserOrObject jsonUserOrObject : jsonGraph.getUsers()) {
            sb.append(jsonUserOrObjectToString(jsonUserOrObject, U));
        }

        sb.append("\n// objects\n");
        for (JSONUserOrObject jsonUserOrObject : jsonGraph.getObjects()) {
            sb.append(jsonUserOrObjectToString(jsonUserOrObject, O));
        }

        return sb.toString();
    }

    private String jsonUserOrObjectToString(JSONUserOrObject jsonUserOrObject, NodeType type) {
        StringBuilder sb = new StringBuilder();

        sb.append(new CreateNonPCStatement(
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

    private String buildCreatePCStatement(JSONPolicyClass policyClass) {
        String pc = policyClass.getName();

        if (AdminPolicy.isAdminPolicyNodeName(pc)) {
            return "";
        }

        CreatePolicyStatement stmt = new CreatePolicyStatement(
                buildNameExpression(pc),
                propertiesMapToExpression(policyClass.getProperties()),
                buildHierarchy(policyClass.getName(), policyClass.getUserAttributes(), UA),
                buildHierarchy(policyClass.getName(), policyClass.getObjectAttributes(), OA),
                buildAssociations(policyClass.getAssociations())
        );

        return "// " + pc + "\n" + stmt.toFormattedString(0) + "\n";
    }

    private List<CreatePolicyStatement.CreateOrAssignAttributeStatement> buildHierarchy(String parent, List<JSONNode> jsonAttrs, NodeType type) {
        if (jsonAttrs == null || jsonAttrs.isEmpty()) {
            return new ArrayList<>();
        }

        List<CreatePolicyStatement.CreateOrAssignAttributeStatement> stmts = new ArrayList<>();

        for (JSONNode attrJSONNode : jsonAttrs) {
            String name = attrJSONNode.getName();

            CreatePolicyStatement.CreateOrAssignAttributeStatement stmt =
                    new CreatePolicyStatement.CreateOrAssignAttributeStatement(
                            buildNameExpression(name),
                            type,
                            buildNameExpression(parent),
                            propertiesMapToExpression(attrJSONNode.getProperties())
                    );

            stmts.add(stmt);
            stmts.addAll(buildHierarchy(name, attrJSONNode.getChildren(), type));
        }

        return stmts;
    }

    private List<AssociateStatement> buildAssociations(Map<String, List<JSONAssociation>> jsonAssociations) {
        if (jsonAssociations == null || jsonAssociations.isEmpty()) {
            return new ArrayList<>();
        }

        List<AssociateStatement> associateStatements = new ArrayList<>();

        for (Map.Entry<String, List<JSONAssociation>> e : jsonAssociations.entrySet()) {
            String ua = e.getKey();
            List<JSONAssociation> associations = e.getValue();

            for (JSONAssociation association : associations) {
                AssociateStatement associateStatement = new AssociateStatement(
                        buildNameExpression(ua),
                        buildNameExpression(association.getTarget()),
                        setToExpression(association.getArset())
                );

                associateStatements.add(associateStatement);
            }
        }

        return associateStatements;
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
                        buildNameExpression(ua),
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
