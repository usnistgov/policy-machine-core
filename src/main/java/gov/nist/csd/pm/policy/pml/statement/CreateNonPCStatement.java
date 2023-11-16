package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

import java.util.*;


public class CreateNonPCStatement extends PMLStatement{
    private final Expression name;
    private final NodeType type;
    private final Expression assignTo;
    private final Expression withProperties;

    public CreateNonPCStatement(Expression name, NodeType type, Expression assignTo) {
        this.name = name;
        this.type = type;
        this.assignTo = assignTo;
        this.withProperties = null;
    }

    public CreateNonPCStatement(Expression name, NodeType type, Expression assignTo, Expression withProperties) {
        this.name = name;
        this.type = type;
        this.assignTo = assignTo;
        this.withProperties = withProperties;
    }

    public Expression getName() {
        return name;
    }

    public NodeType getType() {
        return type;
    }

    public Expression getAssignTo() {
        return assignTo;
    }

    public Expression getWithProperties() {
        return withProperties;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        Value nameValue = name.execute(ctx, policy);
        Value assignToValue = assignTo.execute(ctx, policy);

        String initialParent = "";
        List<String> parents = new ArrayList<>();

        List<Value> arrayValue = assignToValue.getArrayValue();
        for (Value parentValue : arrayValue) {
            if (initialParent.isEmpty()) {
                initialParent = parentValue.getStringValue();
            } else {
                parents.add(parentValue.getStringValue());
            }
        }

        String[] parentsArr = parents.toArray(new String[]{});

        switch (type) {
            case UA -> policy.graph().createUserAttribute(
                    nameValue.getStringValue(),
                    new HashMap<>(),
                    initialParent,
                    parentsArr
            );
            case OA -> policy.graph().createObjectAttribute(
                    nameValue.getStringValue(),
                    new HashMap<>(),
                    initialParent,
                    parentsArr
            );
            case U -> policy.graph().createUser(
                    nameValue.getStringValue(),
                    new HashMap<>(),
                    initialParent,
                    parentsArr
            );
            case O -> policy.graph().createObject(
                    nameValue.getStringValue(),
                    new HashMap<>(),
                    initialParent,
                    parentsArr
            );
        }

        if (withProperties != null) {
            Value propertiesValue = withProperties.execute(ctx, policy);

            Map<String, String> properties = new HashMap<>();
            for (Map.Entry<Value, Value> e : propertiesValue.getMapValue().entrySet()) {
                properties.put(e.getKey().getStringValue(), e.getValue().getStringValue());
            }

            policy.graph().setNodeProperties(nameValue.getStringValue(), properties);
        }

        return new VoidValue();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format(
                "create %s %s %sassign to %s",
                type.toString(),
                name,
                withProperties != null ? "with properties " + withProperties + " ": "",
                assignTo
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateNonPCStatement that = (CreateNonPCStatement) o;
        return Objects.equals(name, that.name) && type == that.type && Objects.equals(
                assignTo, that.assignTo) && Objects.equals(withProperties, that.withProperties);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, type, withProperties != null ? withProperties : "", assignTo);
    }
}
