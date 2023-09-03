package gov.nist.csd.pm.pap.serialization.pml;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.memory.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.dag.propagator.Propagator;
import gov.nist.csd.pm.policy.model.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.pml.PMLFormatter;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.*;
import gov.nist.csd.pm.policy.pml.statement.*;

import java.util.*;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;

public class PMLSerializer implements PolicySerializer {

    private boolean format;

    public PMLSerializer(boolean format) {
        this.format = format;
    }

    public PMLSerializer() {
        this.format = false;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }

    @Override
    public String serialize(Policy policy) throws PMException {
        String pml = toPML(policy);
        if (format) {
            pml = PMLFormatter.format(pml);
        }

        return pml;
    }

    private String toPML(Policy policy) throws PMException {
        String pml = "";
        pml += "# constants\n";
        String constants = serializeConstants(policy);
        if (!constants.isEmpty()) {
            pml += constants + "\n";
        }

        pml += "\n# functions\n";
        String functions = serializeFunctions(policy);
        if (!functions.isEmpty()) {
            pml += functions + "\n";
        }

        pml += "\n# graph\n";
        String graph = serializeGraph(policy);
        if (!graph.isEmpty()) {
            pml += graph + "\n";
        }

        pml += "\n# prohibitions\n";
        String prohibitions = serializeProhibitions(policy);
        if (!prohibitions.isEmpty()) {
            pml += prohibitions + "\n";
        }

        pml += "\n# obligations\n";
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

        List<ComparableStatement> comparableStatements = new ArrayList<>();
        List<String> policyClasses = policy.graph().getPolicyClasses();
        Map<AssociateStatement, List<Expression>> delayedAssociations = new HashMap<>();
        for (String pc : policyClasses) {
            buildPCStatements(policy, comparableStatements, delayedAssociations, pc);
        }

        for (ComparableStatement comparableStatement : comparableStatements) {
            pml.append(comparableStatement.toString()).append("\n");
        }

        // add users and objects statements
        List<PMLStatement> usersAndObjects = buildUsersAndObjectsCreateStatements(policy);
        for (PMLStatement stmt : usersAndObjects) {
            pml.append(stmt).append("\n");
        }

        return pml.toString().trim();
    }

    private void buildPCStatements(Policy policy, List<ComparableStatement> comparableStatements,
                                   Map<AssociateStatement, List<Expression>> delayedAssociations, String pc)
            throws PMException {
        comparableStatements.add(new ComparableCommentStatement("policy class: " + pc));

        if (!AdminPolicy.isAdminPolicyNodeName(pc)) {
            comparableStatements.add(new ComparableCreatePolicyClassStatement(pc));
            SetNodePropertiesStatement setNodePropertiesStatement = buildSetNodePropertiesStatement(policy, pc);
            if (setNodePropertiesStatement != null) {
                comparableStatements.add(new ComparableSetNodePropertiesStatement(setNodePropertiesStatement));
            }
        }

        List<Relations> relationsList = getAttributeRelations(policy, pc, UA);
        List<ComparableStatement> uaStmts = relationsToCreateAttrStatements(policy, relationsList, UA);
        List<ComparableAssociateStatement> assocStmts = new ArrayList<>(
                relationsToAssociationStatements(relationsList, delayedAssociations));

        relationsList = getAttributeRelations(policy, pc, OA);
        List<ComparableStatement> oaStmts = relationsToCreateAttrStatements(policy, relationsList, OA);
        assocStmts.addAll(relationsToAssociationStatements(relationsList, delayedAssociations));

        List<Expression> createdNodes = new ArrayList<>();
        for (ComparableStatement stmt : uaStmts) {
            if (comparableStatements.contains(stmt)) {
                comparableStatements.add(stmt.toAssignStatement());
            } else {
                comparableStatements.add(stmt);
                createdNodes.add(stmt.getName());
            }
        }

        for (ComparableStatement stmt : oaStmts) {
            if (comparableStatements.contains(stmt)) {
                comparableStatements.add(stmt.toAssignStatement());
            } else {
                comparableStatements.add(stmt);
                createdNodes.add(stmt.getName());
            }
        }

        for (ComparableAssociateStatement assocStmt : assocStmts) {
            List<Expression> waitingFor = delayedAssociations.getOrDefault(
                    assocStmt.associateStatement, new ArrayList<>());
            waitingFor.removeAll(createdNodes);

            if (waitingFor.isEmpty()) {
                comparableStatements.add(assocStmt);
                delayedAssociations.remove(assocStmt.associateStatement);
            } else {
                delayedAssociations.put(assocStmt.associateStatement, waitingFor);
            }
        }

        Iterator<Map.Entry<AssociateStatement, List<Expression>>> iterator = delayedAssociations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<AssociateStatement, List<Expression>> e = iterator.next();
            List<Expression> waitingFor = e.getValue();
            waitingFor.removeAll(createdNodes);

            if (waitingFor.isEmpty()) {
                comparableStatements.add(new ComparableAssociateStatement(e.getKey()));
                iterator.remove();
            }
        }
    }

    private List<ComparableAssociateStatement> relationsToAssociationStatements(List<Relations> relationsList,
                                                                                       Map<AssociateStatement,
                                                                                               List<Expression>> delayedAssociations) {
        List<ComparableAssociateStatement> stmts = new ArrayList<>();
        for (Relations relations : relationsList) {
            for (Association association : relations.associations) {
                Expression sourceExpr = nameToExpression(association.getSource());
                Expression targetExpr = nameToExpression(association.getTarget());
                ComparableAssociateStatement associateStatement = new ComparableAssociateStatement(
                        new AssociateStatement(
                                sourceExpr,
                                targetExpr,
                                new Expression(new Literal(
                                        new ArrayLiteral(
                                                setToExpressionArray(association.getAccessRightSet()),
                                                Type.string()
                                        )))
                        ));

                stmts.add(associateStatement);

                List<Expression> waitingFor = new ArrayList<>(List.of(sourceExpr));
                if (!AdminPolicy.isAdminPolicyNodeName(association.getTarget())) {
                    waitingFor.add(targetExpr);
                }

                delayedAssociations.put(associateStatement.associateStatement, waitingFor);
            }
        }

        return stmts;
    }

    private List<ComparableStatement> relationsToCreateAttrStatements(Policy policy, List<Relations> relationsList, NodeType type)
            throws PMException {
        List<ComparableStatement> stmts = new ArrayList<>();

        for (Relations relations : relationsList) {
            if (AdminPolicy.isAdminPolicyNodeName(relations.name)) {
                continue;
            }

            ComparableCreateAttrStatement stmt = new ComparableCreateAttrStatement(new CreateAttrStatement(
                    nameToExpression(relations.name),
                    type,
                    new Expression(
                            new Literal(new ArrayLiteral(setToExpressionArray(relations.assignments), Type.string()))
                    )
            ));

            stmts.add(stmt);

            SetNodePropertiesStatement setNodePropertiesStatement = buildSetNodePropertiesStatement(policy, relations.name);
            if (setNodePropertiesStatement == null) {
                continue;
            }

            stmts.add(new ComparableSetNodePropertiesStatement(setNodePropertiesStatement));
        }

        return stmts;
    }

    private static Expression nameToExpression(String name) {
        if (AdminPolicy.isAdminPolicyNodeName(name)) {
            return new Expression(new VariableReference(
                    AdminPolicy.Node.fromNodeName(name).constantName(),
                    Type.string()
            )
            );
        }

        return new Expression(new Literal(name));
    }

    private Expression[] setToExpressionArray(Set<String> set) {
        Expression[] expressions = new Expression[set.size()];
        int i = 0;
        for (String s : set) {
            expressions[i] = nameToExpression(s);
            i++;
        }

        return expressions;
    }

    private List<Relations> getAttributeRelations(Policy policy, String pc, NodeType type) throws PMException {
        Map<String, Relations> relationsMap = new HashMap<>();
        walk(
                policy,
                pc,
                node -> {
                    NodeType nodeType = policy.graph().getNode(node).getType();

                    if (nodeType != type) {
                        return;
                    }

                    List<Association> nodeAssocs = policy.graph().getAssociationsWithTarget(node);
                    if (nodeAssocs.isEmpty()) {
                        return;
                    }

                    Relations relations = relationsMap.getOrDefault(node, new Relations(node));
                    relations.associations.addAll(nodeAssocs);

                    relationsMap.put(node, relations);
                },
                (child, parent) -> {
                    NodeType childNodeType = policy.graph().getNode(child).getType();

                    if ((AdminPolicy.isAdminPolicyNodeName(child) && AdminPolicy.isAdminPolicyNodeName(parent)) ||
                            childNodeType != type) {
                        return;
                    }

                    Relations relations = relationsMap.getOrDefault(child, new Relations(child));
                    relations.assignments.add(parent);
                    relationsMap.put(child, relations);
                }
        );

        return new ArrayList<>(relationsMap.values());
    }

    record Relations(String name, Set<String> assignments, Set<Association> associations) {
        public Relations(String name) {
            this(name, new HashSet<>(), new HashSet<>());
        }
    }

    private List<PMLStatement> buildUsersAndObjectsCreateStatements(Policy policy) throws PMException {
        List<PMLStatement> statements = new ArrayList<>();
        statements.add(new EmptyStatement());
        statements.add(new SingleLineCommentStatement("users"));

        addStatements(policy, statements, U);

        statements.add(new EmptyStatement());
        statements.add(new SingleLineCommentStatement("objects"));
        addStatements(policy, statements, O);

        return statements;
    }

    static class EmptyStatement extends PMLStatement {

        @Override
        public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
            return new Value();
        }

        @Override
        public String toString() {
            return "";
        }
    }

    private void addStatements(Policy policy, List<PMLStatement> statements, NodeType type) throws PMException {
        List<String> search = policy.graph().search(type, NO_PROPERTIES);
        for (String object : search) {
            PMLStatement statement = buildCreateUserOrObjectStatement(policy, object, type);
            statements.add(statement);

            statement = buildSetNodePropertiesStatement(policy, object);
            if (statement == null) {
                continue;
            }

            statements.add(statement);
        }
    }

    private CreateUserOrObjectStatement buildCreateUserOrObjectStatement(Policy policy, String name, NodeType type) throws PMException {
        // the name expression
        Expression nameExpr = new Expression(new Literal(name));

        // build the parent expression for the create node statement
        List<String> parents = policy.graph().getParents(name);
        List<Expression> parentExprs = new ArrayList<>();
        for (String parent : parents) {
            parentExprs.add(nodeNameToExpression(parent));
        }

        Expression assignTo = new Expression(new Literal(new ArrayLiteral(
                parentExprs.toArray(Expression[]::new),
                Type.string()
        )));

        return new CreateUserOrObjectStatement(nameExpr, type, assignTo);
    }

    private SetNodePropertiesStatement buildSetNodePropertiesStatement(Policy policy, String name)
            throws PMException {
        Map<String, String> properties = policy.graph().getNode(name).getProperties();
        if (properties.isEmpty()) {
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
                nameToExpression(name),
                new Expression(new Literal(new MapLiteral(propertiesExpressions, Type.string(), Type.string())))
        );
    }

    private Expression nodeNameToExpression(String nodeName) {
        if (AdminPolicy.isAdminPolicyNodeName(nodeName)) {
            nodeName = AdminPolicy.Node.fromNodeName(nodeName).constantName();
        }

        return new Expression(new Literal(nodeName));
    }

    private void walk(Policy policy, String pc, Visitor visitor, Propagator propagator) throws PMException {
        new DepthFirstGraphWalker(policy.graph())
                .withVisitor(visitor)
                .withPropagator(propagator)
                .withDirection(Direction.CHILDREN)
                .walk(pc);
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
            if (AdminPolicy.AL_NODE_CONSTANT_NAMES.contains(c.getKey())) {
                continue;
            }

            if (!pml.isEmpty()) {
                pml.append("\n");
            }

            pml.append(serializeConstant(c.getKey(), c.getValue()));
        }
        return pml.toString();
    }

    private static String serializeConstant(String name, Value value) {
        return String.format("const %s = %s", name, value.toString());
    }

    abstract static class ComparableStatement {
        abstract Expression getName();

        abstract ComparableStatement toAssignStatement();

        @Override
        public int hashCode() {
            return Objects.hash(getName());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ComparableStatement that = (ComparableStatement) o;
            return Objects.equals(getName(), that.getName());
        }
    }

    static class ComparableCreatePolicyClassStatement extends ComparableStatement {

        private final String name;

        public ComparableCreatePolicyClassStatement(String name) {
            this.name = name;
        }

        @Override
        Expression getName() {
            return new Expression(new Literal(name));
        }

        @Override
        ComparableStatement toAssignStatement() {
            return this;
        }

        @Override
        public String toString() {
            return new CreatePolicyStatement(nameToExpression(name)).toString();
        }
    }

    static class ComparableCommentStatement extends ComparableStatement {

        private final String comment;

        public ComparableCommentStatement(String comment) {
            this.comment = comment;
        }

        @Override
        Expression getName() {
            return new Expression(new Literal(comment));
        }

        @Override
        ComparableStatement toAssignStatement() {
            return this;
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(System.nanoTime(), comment);
        }

        @Override
        public String toString() {
            return "\n# " + comment;
        }
    }

    static class ComparableCreateAttrStatement extends ComparableStatement {

        private final CreateAttrStatement createAttrStatement;

        public ComparableCreateAttrStatement(CreateAttrStatement createAttrStatement) {
            this.createAttrStatement = createAttrStatement;
        }

        @Override
        public ComparableStatement toAssignStatement() {
            return new ComparableAssignStatement(new AssignStatement(
                    createAttrStatement.getName(),
                    createAttrStatement.getAssignTo()
            ));
        }

        @Override
        public Expression getName() {
            return createAttrStatement.getName();
        }

        @Override
        public String toString() {
            return createAttrStatement.toString();
        }
    }

    static class ComparableSetNodePropertiesStatement extends ComparableStatement {

        private final SetNodePropertiesStatement setNodePropertiesStatement;

        public ComparableSetNodePropertiesStatement(SetNodePropertiesStatement setNodePropertiesStatement) {
            this.setNodePropertiesStatement = setNodePropertiesStatement;
        }

        @Override
        public ComparableStatement toAssignStatement() {
            return this;
        }

        @Override
        public Expression getName() {
            return setNodePropertiesStatement.getNameExpr();
        }

        @Override
        public String toString() {
            return setNodePropertiesStatement.toString();
        }
    }

    static class ComparableAssignStatement extends ComparableStatement {

        private final AssignStatement assignStatement;

        public ComparableAssignStatement(AssignStatement assignStatement) {
            this.assignStatement = assignStatement;
        }

        @Override
        Expression getName() {
            return assignStatement.getChild();
        }

        @Override
        ComparableStatement toAssignStatement() {
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }
            ComparableAssignStatement that = (ComparableAssignStatement) o;
            return Objects.equals(assignStatement, that.assignStatement);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), assignStatement);
        }

        @Override
        public String toString() {
            return assignStatement.toString();
        }
    }

    static class ComparableAssociateStatement extends ComparableStatement {

        private final AssociateStatement associateStatement;

        public ComparableAssociateStatement(AssociateStatement associateStatement) {
            this.associateStatement = associateStatement;
        }

        @Override
        Expression getName() {
            return new Expression(new Literal(new ArrayLiteral(
                    new Expression[]{associateStatement.getUa(), associateStatement.getTarget()},
                    Type.any()
            )));
        }

        @Override
        ComparableStatement toAssignStatement() {
            return this;
        }

        @Override
        public String toString() {
            return associateStatement.toString();
        }

        @Override
        public boolean equals(Object o) {
            return associateStatement.equals(o);
        }

        @Override
        public int hashCode() {
            return associateStatement.hashCode();
        }
    }
}
