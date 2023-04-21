package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.OA;

public class CreateAttrStatement extends PALStatement {

    private final Expression name;
    private final NodeType type;
    private final Expression assignTo;

    public CreateAttrStatement(Expression name, NodeType type, Expression assignTo) {
        this.name = name;
        this.type = type;
        this.assignTo = assignTo;
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

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        Value nameValue = name.execute(ctx, policy);
        Value assignToValue = assignTo.execute(ctx, policy);

        String initialParent = "";
        List<String> parents = new ArrayList<>();
        if (assignToValue.isString()) {
            initialParent = assignToValue.getStringValue();
        } else {
            List<Value> arrayValue = assignToValue.getArrayValue();
            for (Value parentValue : arrayValue) {
                if (initialParent.isEmpty()) {
                    initialParent = parentValue.getStringValue();
                } else {
                    parents.add(parentValue.getStringValue());
                }
            }
        }

        String[] parentsArr = parents.toArray(new String[]{});

        if (type == OA) {
            policy.createObjectAttribute(
                    nameValue.getStringValue(),
                    new HashMap<>(),
                    initialParent,
                    parentsArr
            );
        } else {
            policy.createUserAttribute(
                    nameValue.getStringValue(),
                    new HashMap<>(),
                    initialParent,
                    parentsArr
            );
        }

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("create %s %s in %s;",
                (type == OA ? "object attribute" : "user attribute"),
                name,
                assignTo
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateAttrStatement that = (CreateAttrStatement) o;
        return Objects.equals(name, that.name) && type == that.type && Objects.equals(assignTo, that.assignTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, assignTo);
    }
}
