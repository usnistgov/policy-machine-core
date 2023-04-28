package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.pap.memory.dag.BreadthFirstGraphWalker;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.pml.model.expression.*;
import gov.nist.csd.pm.policy.pml.statement.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.*;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.isAdminAccessRight;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.OA;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.UA;

public class PMLSerializer implements PMLSerializable {

    private final Policy policy;

    public PMLSerializer(Policy policy) {
        this.policy = policy;
    }

    @Override
    public void fromPML(UserContext author, String input, FunctionDefinitionStatement... customFunctions) throws PMException {
        PMLExecutor.compileAndExecutePML(policy, author, input, customFunctions);
    }

    @Override
    public String toPML(boolean format) throws PMException {
        String pml = toPML(policy);
        if (format) {
            pml = PMLFormatter.format(pml);
        }

        return pml;
    }

    private String toPML(Policy policy) throws PMException {
        String pml = "";
        String constants = serializeConstants(policy);
        if (!constants.isEmpty()) {
            pml += constants + "\n";
        }

        String functions = serializeFunctions(policy);
        if (!functions.isEmpty()) {
            pml += functions + "\n";
        }

        String graph = serializeGraph(policy);
        if (!graph.isEmpty()) {
            pml += graph + "\n";
        }

        String prohibitions = serializeProhibitions(policy);
        if (!prohibitions.isEmpty()) {
            pml += prohibitions + "\n";
        }

        String obligations = serializeObligations(policy);
        if (!obligations.isEmpty()) {
            pml += obligations;
        }

        return pml.trim();
    }

    private String serializeObligations(Policy policy) throws PMException {
        StringBuilder pml = new StringBuilder();

        List<Obligation> obligations = policy.obligations().getAll();
        for (Obligation o : obligations) {
            if (!pml.isEmpty()) {
                pml.append("\n");
            }
            pml.append(CreateObligationStatement.fromObligation(o));
        }

        return pml.toString();
    }

    private String serializeProhibitions(Policy policy) throws PMException {
        StringBuilder pml = new StringBuilder();

        Map<String, List<Prohibition>> prohibitions = policy.prohibitions().getAll();
        for (List<Prohibition> subjectPros : prohibitions.values()) {
            for (Prohibition p : subjectPros) {
                if (!pml.isEmpty()) {
                    pml.append("\n");
                }

                pml.append(CreateProhibitionStatement.fromProhibition(p));
            }
        }

        return pml.toString();
    }

    private String serializeGraph(Policy policy) throws PMException {
        StringBuilder pml = new StringBuilder();

        // resource access rights
        ArrayLiteral arrayLiteral = new ArrayLiteral(Type.string());
        for (String ar : policy.graph().getResourceAccessRights()) {
            arrayLiteral.add(new Expression(new Literal(ar)));
        }
        pml.append(new SetResourceAccessRightsStatement(new Expression(new Literal(arrayLiteral)))).append("\n");

        List<String> policyClasses = policy.graph().getPolicyClasses();
        Set<String> attributes = new HashSet<>();
        Set<String> usersAndObjects = new HashSet<>();
        Map<String, List<AssociateStatement>> delayedAssociations = new HashMap<>();

        for (String policyClass : policyClasses) {
            pml.append(new CreatePolicyStatement(new Expression(new Literal(policyClass)))).append("\n");

            Node pcNode = policy.graph().getNode(policyClass);
            if (!pcNode.getProperties().isEmpty()) {
                PMLStatement stmt = buildSetNodePropertiesStatement(pcNode.getName(), pcNode.getProperties());
                pml.append(stmt).append("\n");
            }

            new BreadthFirstGraphWalker(policy.graph())
                    .withPropagator((child, parent) -> {
                        Node childNode = policy.graph().getNode(child);

                        if (childNode.getType() == OA || childNode.getType() == UA) {
                            if (!attributes.contains(child)) {
                                attributes.add(child);
                                PMLStatement stmt = buildCreateNodeStatement(child, childNode.getType(), parent);
                                pml.append(stmt).append("\n");
                                if (!childNode.getProperties().isEmpty()) {
                                    stmt = buildSetNodePropertiesStatement(child, childNode.getProperties());
                                    pml.append(stmt).append("\n");
                                }
                            } else {
                                pml.append(new AssignStatement(
                                        new Expression(new Literal(child)),
                                        new Expression(new Literal(new ArrayLiteral(new Expression[]{new Expression(new Literal(parent))}, Type.string())))
                                )).append("\n");
                            }

                            if (delayedAssociations.containsKey(child)) {
                                List<AssociateStatement> associateStatements = delayedAssociations.get(child);
                                for (AssociateStatement stmt : associateStatements) {
                                    pml.append(stmt).append("\n");
                                }

                                delayedAssociations.remove(child);
                            }

                            if (childNode.getType() == UA) {
                                List<Association> sourceAssociations = policy.graph().getAssociationsWithSource(child);
                                for (Association association : sourceAssociations) {

                                    ArrayLiteral arLiteral = new ArrayLiteral(Type.string());
                                    for (String ar : association.getAccessRightSet()) {
                                        if (isAdminAccessRight(ar)) {
                                            arLiteral.add(new Expression(new VariableReference(ar, Type.string())));
                                        } else {
                                            arLiteral.add(new Expression(new Literal(ar)));
                                        }
                                    }

                                    String target = association.getTarget();
                                    AssociateStatement stmt = new AssociateStatement(
                                            new Expression(new Literal(child)),
                                            new Expression(new Literal(association.getTarget())),
                                            new Expression(new Literal(arLiteral))
                                    );

                                    if (!attributes.contains(target)) {
                                        List<AssociateStatement> associateStmts = delayedAssociations.getOrDefault(target, new ArrayList<>());
                                        associateStmts.add(stmt);
                                        delayedAssociations.put(target, associateStmts);
                                    } else {
                                        pml.append(stmt).append("\n");
                                    }
                                }
                            }
                        } else {
                            if (!usersAndObjects.contains(child)) {
                                usersAndObjects.add(child);
                                PMLStatement stmt = buildCreateNodeStatement(child, childNode.getType(), parent);
                                pml.append(stmt).append("\n");
                            } else {
                                pml.append(new AssignStatement(
                                        new Expression(new Literal(child)),
                                        new Expression(new Literal(new ArrayLiteral(new Expression[]{new Expression(new Literal(parent))}, Type.string())))
                                )).append("\n");
                            }
                        }
                    })
                    .withDirection(Direction.CHILDREN)
                    .walk(policyClass);
        }

        return pml.toString().trim();
    }

    private PMLStatement buildSetNodePropertiesStatement(String name, Map<String, String> properties) {
        Map<Expression, Expression> propertiesExpressions = new HashMap<>();
        for (Map.Entry<String, String> property : properties.entrySet()) {
            propertiesExpressions.put(
                    new Expression(new Literal(property.getKey())),
                    new Expression(new Literal(property.getValue()))
            );
        }

        return new SetNodePropertiesStatement(
                new Expression(new Literal(name)),
                new Expression(new Literal(new MapLiteral(propertiesExpressions, Type.string(), Type.string())))
        );
    }

    private PMLStatement buildCreateNodeStatement(String name, NodeType type, String parent) {
        if (type == UA || type == OA) {
            return new CreateAttrStatement(
                    new Expression(new Literal(name)),
                    type,
                    new Expression(new Literal(new ArrayLiteral(new Expression[]{new Expression(new Literal(parent))}, Type.string())))
            );
        } else {
            return new CreateUserOrObjectStatement(
                    new Expression(new Literal(name)),
                    type,
                    new Expression(new Literal(new ArrayLiteral(new Expression[]{new Expression(new Literal(parent))}, Type.string())))
            );
        }
    }

    private String serializeFunctions(Policy policy) throws PMException {
        StringBuilder pml = new StringBuilder();
        Map<String, FunctionDefinitionStatement> functions = policy.userDefinedPML().getFunctions();
        for (FunctionDefinitionStatement func : functions.values()) {
            if (func.isFunctionExecutor()) {
                continue;
            }

            if (!pml.isEmpty()) {
                pml.append("\n");
            }

            pml.append(func);
        }

        return pml.toString();
    }

    private String serializeConstants(Policy policy) throws PMException {
        StringBuilder pml = new StringBuilder();
        Map<String, Value> constants = policy.userDefinedPML().getConstants();
        for (Map.Entry<String, Value> c : constants.entrySet()) {
            if (!pml.isEmpty()) {
                pml.append("\n");
            }

            pml.append(serializeConstant(c.getKey(), c.getValue()));
        }
        return pml.toString();
    }

    private String serializeConstant(String name, Value value) {
        return String.format("const %s = %s", name, value.toString());
    }
}
