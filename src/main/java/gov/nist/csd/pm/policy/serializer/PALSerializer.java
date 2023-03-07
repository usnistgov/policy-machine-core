package gov.nist.csd.pm.policy.serializer;

import gov.nist.csd.pm.pap.memory.dag.BreadthFirstGraphWalker;
import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.pal.PALFormatter;
import gov.nist.csd.pm.policy.author.pal.model.expression.*;
import gov.nist.csd.pm.policy.author.pal.statement.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.sql.Statement;
import java.util.*;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.isAdminAccessRight;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.OA;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.UA;

public class PALSerializer implements PolicySerializer {

    private boolean format;

    public PALSerializer(boolean format) {
        this.format = format;
    }

    private final String SEMI_COLON = ";";

    @Override
    public String serialize(PolicyAuthor policyAuthor) throws PMException {
        String pal = toPAL(policyAuthor);
        if (format) {
            pal = PALFormatter.format(pal);
        }

        return pal;
    }
    
    private String toPAL(PolicyAuthor policy) throws PMException {
        String pal = "";
        String constants = serializeConstants(policy);
        if (!constants.isEmpty()) {
            pal += constants + "\n";
        }

        String functions = serializeFunctions(policy);
        if (!functions.isEmpty()) {
            pal += functions + "\n";
        }

        String graph = serializeGraph(policy);
        if (!graph.isEmpty()) {
            pal += graph + "\n";
        }

        String prohibitions = serializeProhibitions(policy);
        if (!prohibitions.isEmpty()) {
            pal += prohibitions + "\n";
        }

        String obligations = serializeObligations(policy);
        if (!obligations.isEmpty()) {
            pal += obligations;
        }

        return pal.trim();
    }

    private String serializeObligations(PolicyAuthor policy) throws PMException {
        StringBuilder pal = new StringBuilder();

        List<Obligation> obligations = policy.getObligations();
        for (Obligation o : obligations) {
            if (!pal.isEmpty()) {
                pal.append("\n");
            }
            pal.append(CreateObligationStatement.fromObligation(o));
        }

        return pal.toString();
    }

    private String serializeProhibitions(PolicyAuthor policy) throws PMException {
        StringBuilder pal = new StringBuilder();

        Map<String, List<Prohibition>> prohibitions = policy.getProhibitions();
        for (List<Prohibition> subjectPros : prohibitions.values()) {
            for (Prohibition p : subjectPros) {
                if (!pal.isEmpty()) {
                    pal.append("\n");
                }

                pal.append(CreateProhibitionStatement.fromProhibition(p));
            }
        }

        return pal.toString();
    }

    private String serializeGraph(PolicyAuthor policy) throws PMException {
        StringBuilder pal = new StringBuilder();

        // resource access rights
        List<Expression> arExprs = new ArrayList<>();
        for (String ar : policy.getResourceAccessRights()) {
            arExprs.add(new Expression(new Literal(ar)));
        }
        pal.append(new SetResourceAccessRightsStatement(arExprs)).append("\n");

        List<String> policyClasses = policy.getPolicyClasses();
        Set<String> attributes = new HashSet<>();
        Set<String> usersAndObjects = new HashSet<>();
        Map<String, List<AssociateStatement>> delayedAssociations = new HashMap<>();

        for (String policyClass : policyClasses) {
            pal.append(new CreatePolicyStatement(new Expression(new Literal(policyClass)))).append("\n");

            Node pcNode = policy.getNode(policyClass);
            if (!pcNode.getProperties().isEmpty()) {
                PALStatement stmt = buildSetNodePropertiesStatement(pcNode.getName(), pcNode.getProperties());
                pal.append(stmt).append("\n");
            }

            new BreadthFirstGraphWalker(policy)
                    .withPropagator((child, parent) -> {
                        Node childNode = policy.getNode(child);

                        if (childNode.getType() == OA || childNode.getType() == UA) {
                            if (!attributes.contains(child)) {
                                attributes.add(child);
                                PALStatement stmt = buildCreateNodeStatement(child, childNode.getType(), parent);
                                pal.append(stmt).append("\n");
                                if (!childNode.getProperties().isEmpty()) {
                                    stmt = buildSetNodePropertiesStatement(child, childNode.getProperties());
                                    pal.append(stmt).append("\n");
                                }
                            } else {
                                pal.append(new AssignStatement(
                                        new Expression(new Literal(child)),
                                        new Expression(new Literal(parent))
                                )).append("\n");
                            }

                            if (delayedAssociations.containsKey(child)) {
                                List<AssociateStatement> associateStatements = delayedAssociations.get(child);
                                for (AssociateStatement stmt : associateStatements) {
                                    pal.append(stmt).append("\n");
                                }

                                delayedAssociations.remove(child);
                            }

                            if (childNode.getType() == UA) {
                                List<Association> sourceAssociations = policy.getAssociationsWithSource(child);
                                for (Association association : sourceAssociations) {
                                    List<Expression> exprs = new ArrayList<>();
                                    for (String ar : association.getAccessRightSet()) {
                                        if (isAdminAccessRight(ar)) {
                                            exprs.add(new Expression(new VariableReference(ar, Type.string())));
                                        } else {
                                            exprs.add(new Expression(new Literal(ar)));
                                        }
                                    }

                                    String target = association.getTarget();
                                    AssociateStatement stmt = new AssociateStatement(
                                            new Expression(new Literal(child)),
                                            new Expression(new Literal(association.getTarget())),
                                            new Expression(exprs)
                                    );

                                    if (!attributes.contains(target)) {
                                        List<AssociateStatement> associateStmts = delayedAssociations.getOrDefault(target, new ArrayList<>());
                                        associateStmts.add(stmt);
                                        delayedAssociations.put(target, associateStmts);
                                    } else {
                                        pal.append(stmt).append("\n");
                                    }
                                }
                            }
                        } else {
                            if (!usersAndObjects.contains(child)) {
                                usersAndObjects.add(child);
                                PALStatement stmt = buildCreateNodeStatement(child, childNode.getType(), parent);
                                pal.append(stmt).append("\n");
                            } else {
                                pal.append(new AssignStatement(
                                        new Expression(new Literal(child)),
                                        new Expression(new Literal(parent))
                                )).append("\n");
                            }
                        }
                    })
                    .withDirection(Direction.CHILDREN)
                    .walk(policyClass);
        }

        return pal.toString().trim();
    }

    private PALStatement buildSetNodePropertiesStatement(String name, Map<String, String> properties) {
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

    private PALStatement buildCreateNodeStatement(String name, NodeType type, String parent) {
        if (type == UA || type == OA) {
            return new CreateAttrStatement(
                    new Expression(new Literal(name)),
                    type,
                    new Expression(
                            new Expression(new Literal(parent))
                    )
            );
        } else {
            return new CreateUserOrObjectStatement(
                    new Expression(new Literal(name)),
                    type,
                    new Expression(
                            new Expression(new Literal(parent))
                    )
            );
        }
    }

    private String serializeFunctions(PolicyAuthor policy) throws PMException {
        StringBuilder pal = new StringBuilder();
        Map<String, FunctionDefinitionStatement> functions = policy.getPALFunctions();
        for (FunctionDefinitionStatement func : functions.values()) {
            if (!pal.isEmpty()) {
                pal.append("\n");
            }

            pal.append(func.toString());
        }

        return pal.toString();
    }

    private String serializeConstants(PolicyAuthor policy) throws PMException {
        StringBuilder pal = new StringBuilder();
        Map<String, Value> constants = policy.getPALConstants();
        for (Map.Entry<String, Value> c : constants.entrySet()) {
            if (!pal.isEmpty()) {
                pal.append("\n");
            }

            pal.append(serializeConstant(c.getKey(), c.getValue())).append(SEMI_COLON);
        }
        return pal.toString();
    }

    private String serializeConstant(String name, Value value) {
        return String.format("const %s = %s", name, value.toString());
    }
}
