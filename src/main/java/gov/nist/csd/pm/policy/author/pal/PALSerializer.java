package gov.nist.csd.pm.policy.author.pal;

import gov.nist.csd.pm.pap.memory.dag.BreadthFirstGraphWalker;
import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.author.pal.model.expression.*;
import gov.nist.csd.pm.policy.author.pal.statement.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.*;

import static gov.nist.csd.pm.pap.SuperPolicy.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.OA;
import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.UA;

class PALSerializer {

    private static final String TAB_SPACES = "    ";
    private static final String SEMI_COLON = ";";

    private final PolicyAuthor policy;

    PALSerializer(PolicyAuthor policy) {
        this.policy = policy;
    }

    String toPAL() throws PMException {
        String pal = "%s\n%s\n%s\n%s\n%s";

        String constants = serializeConstants();
        String functions = serializeFunctions();
        String graph = serializeGraph();
        String prohibitions = serializeProhibitions();
        String obligations = serializeObligations();

        return String.format(pal, constants, functions, graph, prohibitions, obligations);
    }

    private String serializeObligations() throws PMException {
        StringBuilder pal = new StringBuilder();

        List<Obligation> obligations = policy.obligations().getAll();
        for (Obligation o : obligations) {
            pal.append(CreateObligationStatement.fromObligation(o)).append("\n");
        }

        return pal.toString();
    }

    private String serializeProhibitions() throws PMException {
        StringBuilder pal = new StringBuilder();

        Map<String, List<Prohibition>> prohibitions = policy.prohibitions().getAll();
        for (String s : prohibitions.keySet()) {
            List<Prohibition> subjectPros = prohibitions.get(s);
            for (Prohibition p : subjectPros) {
                pal.append(CreateProhibitionStatement.fromProhibition(p)).append("\n");
            }
        }

        return pal.toString();
    }

    private String serializeGraph() throws PMException {
        StringBuilder pal = new StringBuilder();

        // resource access rights
        pal.append(new SetResourceAccessRightsStatement(policy.graph().getResourceAccessRights())).append("\n");

        List<String> policyClasses = policy.graph().getPolicyClasses();

        Set<String> attributes = new HashSet<>();
        Set<String> usersAndObjects = new HashSet<>();

        for (String policyClass : policyClasses) {
            if (policyClass.equals(SUPER_PC)) {
                continue;
            }

            pal.append(new CreatePolicyStatement(new NameExpression(new VariableReference(policyClass, Type.string())))).append("\n");

            new BreadthFirstGraphWalker(policy.graph())
                    .withPropagator((parent, child) -> {
                        if (child.startsWith(SUPER_PREFIX)) {
                            return;
                        }

                        Node childNode = policy.graph().getNode(child);

                        if (childNode.getType() == OA || childNode.getType() == UA) {
                            if (!attributes.contains(child)) {
                                attributes.add(child);
                                PALStatement stmt = buildCreateNodeStatement(child, childNode.getType(), parent);
                                pal.append(stmt).append("\n");
                            } else {
                                pal.append(new AssignStatement(
                                        new NameExpression(new VariableReference(child, Type.string())),
                                        new NameExpression(new VariableReference(parent, Type.string()))
                                )).append("\n");
                            }

                            if (childNode.getType() == UA) {
                                List<Association> sourceAssociations = policy.graph().getAssociationsWithSource(child);
                                for (Association association : sourceAssociations) {
                                    List<NameExpression> exprs = new ArrayList<>();
                                    for (String ar : association.getAccessRightSet()) {
                                        exprs.add(new NameExpression(new VariableReference(ar, Type.string())));
                                    }

                                    pal.append(new AssociateStatement(
                                            new NameExpression(new VariableReference(child, Type.string())),
                                            new NameExpression(new VariableReference(association.getTarget(), Type.string())),
                                            new NameExpression(exprs)
                                    )).append("\n");
                                }
                            }
                        } else {
                            if (!usersAndObjects.contains(child)) {
                                usersAndObjects.add(child);
                                PALStatement stmt = buildCreateNodeStatement(child, childNode.getType(), parent);
                                pal.append(stmt).append("\n");
                            } else {
                                pal.append(new AssignStatement(
                                        new NameExpression(new VariableReference(child, Type.string())),
                                        new NameExpression(new VariableReference(parent, Type.string()))
                                )).append("\n");
                            }
                        }
                    })
                    .withDirection(Direction.CHILDREN)
                    .walk(policyClass);
        }

        return pal.toString();
    }

    private PALStatement buildCreateNodeStatement(String name, NodeType type, String parent) {
        if (type == UA || type == OA) {
            return new CreateAttrStatement(
                    new NameExpression(new VariableReference(name, Type.string())),
                    type,
                    new NameExpression(
                            new NameExpression(new VariableReference(parent, Type.string()))
                    )
            );
        } else {
            return new CreateUserOrObjectStatement(
                    new NameExpression(new VariableReference(name, Type.string())),
                    type,
                    new NameExpression(
                            new NameExpression(new VariableReference(parent, Type.string()))
                    )
            );
        }
    }

    private String serializeFunctions() throws PMException {
        StringBuilder pal = new StringBuilder();
        Map<String, FunctionDefinitionStatement> functions = policy.pal().getFunctions();
        for (String funcName : functions.keySet()) {
            pal.append("\n").append(functions.get(funcName).toString()).append("\n");
        }

        return pal.toString();
    }

    private String serializeConstants() throws PMException {
        StringBuilder pal = new StringBuilder();
        Map<String, Value> constants = policy.pal().getConstants();
        for (String c : constants.keySet()) {
            Value v = constants.get(c);
            pal.append(serializeConstant(c, v)).append(SEMI_COLON);
        }
        return pal.toString();
    }

    private String serializeConstant(String name, Value value) {
        return String.format("const %s = %s", name, value.toString());
    }

    private String indentLine(String line, int indentNum) {
        return TAB_SPACES.repeat(Math.max(0, indentNum)) + line;
    }

}
