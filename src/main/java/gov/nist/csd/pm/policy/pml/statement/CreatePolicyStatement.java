package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

import java.util.*;

public class CreatePolicyStatement extends PMLStatement {

    private final Expression name;
    private final Expression properties;
    private List<CreateOrAssignAttributeStatement> uas;
    private List<CreateOrAssignAttributeStatement> oas;
    private List<AssociateStatement> assocs;

    public CreatePolicyStatement(Expression name, Expression properties) {
        this.name = name;
        this.properties = properties;
    }

    public CreatePolicyStatement(Expression name) {
        this.name = name;
        this.properties = null;
    }

    public CreatePolicyStatement(Expression name, Expression properties, List<CreateOrAssignAttributeStatement> uas,
                                 List<CreateOrAssignAttributeStatement> oas, List<AssociateStatement> assocs) {
        this.name = name;
        this.properties = properties;
        this.uas = uas;
        this.oas = oas;
        this.assocs = assocs;
    }

    public Expression getName() {
        return name;
    }

    public Expression getProperties() {
        return properties;
    }

    public List<CreateOrAssignAttributeStatement> getUas() {
        return uas;
    }

    public List<CreateOrAssignAttributeStatement> getOas() {
        return oas;
    }

    public List<AssociateStatement> getAssocs() {
        return assocs;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        Map<String, String> props = new HashMap<>();

        if (this.properties != null) {
            Value propertiesValue = properties.execute(ctx, policy);
            for (Map.Entry<Value, Value> e : propertiesValue.getMapValue().entrySet()) {
                props.put(e.getKey().getStringValue(), e.getValue().getStringValue());
            }
        }

        policy.graph().createPolicyClass(name.execute(ctx, policy).getStringValue(), props);

        // create hierarchy
        createHierarchy(ctx, policy);

        return new VoidValue();
    }

    private void createHierarchy(ExecutionContext ctx, Policy policy) throws PMException {
        // create uas
        if (uas != null) {
            createUas(ctx, policy);
        }

        // create oas
        if (oas != null) {
            createOas(ctx, policy);
        }

        // assocs
        if (assocs != null) {
            createAssocs(ctx, policy);
        }
    }

    private void createUas(ExecutionContext ctx, Policy policy) throws PMException {
        for (PMLStatement stmt : uas) {
            stmt.execute(ctx, policy);
        }
    }

    private void createOas(ExecutionContext ctx, Policy policy) throws PMException {
        for (PMLStatement stmt : oas) {
            stmt.execute(ctx, policy);
        }
    }

    private void createAssocs(ExecutionContext ctx, Policy policy) throws PMException {
        for (AssociateStatement associateStatement : assocs) {
            associateStatement.execute(ctx, policy);
        }
    }

    @Override
    public String toFormattedString(int indentLevel) {
        String propertiesStr = (properties == null ? "" : " with properties " + properties);
        String hierarchyStr = getHierarchyStr(indentLevel);
        return indent(indentLevel) + String.format("create PC %s%s%s", name, propertiesStr, hierarchyStr);
    }

    private String getHierarchyStr(int indentLevel) {
        String hierarchyStr = "";

        if (uas != null && !uas.isEmpty()) {
            hierarchyStr += getUaStr(indentLevel) + "\n";
        }

        if (oas != null && !oas.isEmpty()) {
            hierarchyStr += getOaStr(indentLevel) + "\n";
        }

        if (assocs != null && !assocs.isEmpty()) {
            hierarchyStr += getAssocStr(indentLevel);
        }

        if (hierarchyStr.isEmpty()) {
            return hierarchyStr;
        }

        return String.format(" {\n%s\n%s}", hierarchyStr, indent(indentLevel));
    }

    private String getUaStr(int indentLevel) {
        return getAttrStr(indentLevel, uas, "user attributes");
    }

    private String getOaStr(int indentLevel) {
        return getAttrStr(indentLevel, oas, "object attributes");
    }

    private String getAssocStr(int indentLevel) {
        indentLevel++;

        StringBuilder assocsStr = new StringBuilder();
        for (AssociateStatement associateStatement : assocs) {
            assocsStr.append("\n")
                     .append(indent(indentLevel+1))
                     .append(associateStatement.getUa())
                     .append(" and ")
                     .append(associateStatement.getTarget())
                     .append(" with ")
                     .append(associateStatement.getAccessRights());
        }

        String rootIndent = indent(indentLevel);
        return String.format("%sassociations {%s\n%s}", rootIndent, assocsStr, rootIndent);
    }

    private String getAttrStr(int indentLevel, List<CreateOrAssignAttributeStatement> attrs, String label) {
        indentLevel++;

        Map<Expression, Integer> parentIndents = new HashMap<>();
        parentIndents.put(name, indentLevel);

        StringBuilder uaStr = new StringBuilder();
        for (CreateOrAssignAttributeStatement stmt : attrs) {
            String propertiesStr = (stmt.getWithProperties() == null ? "" : " " + stmt.getWithProperties());

            int parentIndent = parentIndents.get(stmt.parent);
            int indent = parentIndent+1;

            uaStr.append("\n")
                 .append(indent(indent))
                 .append(stmt.getName())
                 .append(propertiesStr);

            parentIndents.put(stmt.getName(), indent);
        }

        String rootIndent = indent(indentLevel);
        return String.format("%s%s {%s\n%s}", rootIndent, label, uaStr, rootIndent);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreatePolicyStatement that = (CreatePolicyStatement) o;
        return Objects.equals(name, that.name) && Objects.equals(
                properties, that.properties) && Objects.equals(uas, that.uas) && Objects.equals(
                oas, that.oas) && Objects.equals(assocs, that.assocs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static class CreateOrAssignAttributeStatement extends CreateNonPCStatement {

        private final Expression parent;

        public CreateOrAssignAttributeStatement(Expression name, NodeType type, Expression assignTo) {
            super(name, type, new ArrayLiteral(Type.string(), assignTo));

            this.parent = assignTo;
        }

        public CreateOrAssignAttributeStatement(Expression name, NodeType type, Expression assignTo, Expression withProperties) {
            super(name, type, new ArrayLiteral(Type.string(), assignTo), withProperties);

            this.parent = assignTo;
        }

        @Override
        public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
            Value nameValue = getName().execute(ctx, policy);

            if (!policy.graph().nodeExists(nameValue.getStringValue())) {
                return super.execute(ctx, policy);
            }

            AssignStatement assignStatement = new AssignStatement(getName(), getAssignTo());
            return assignStatement.execute(ctx, policy);
        }

        @Override
        public String toFormattedString(int indentLevel) {
            Expression withProperties = getWithProperties();
            return indent(indentLevel) + getName() + (withProperties == null ? "" : " " + withProperties);
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
            CreateOrAssignAttributeStatement that = (CreateOrAssignAttributeStatement) o;
            return Objects.equals(parent, that.parent);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), parent);
        }
    }
}
