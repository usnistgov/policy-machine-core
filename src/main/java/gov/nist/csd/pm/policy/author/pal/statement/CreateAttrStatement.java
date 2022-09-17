package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.OA;

public class CreateAttrStatement extends PALStatement {

    public NameExpression name;
    public NodeType type;
    public NameExpression assignTo;

    public CreateAttrStatement(NameExpression name, NodeType type, NameExpression assignTo) {
        this.name = name;
        this.type = type;
        this.assignTo = assignTo;
    }

    public NameExpression getName() {
        return name;
    }

    public NodeType getType() {
        return type;
    }

    public NameExpression getAssignTo() {
        return assignTo;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        Value nameValue = name.execute(ctx, policyAuthor);
        Value assignToValue = assignTo.execute(ctx, policyAuthor);

        String initialParent = "";
        List<String> parents = new ArrayList<>();
        if (assignToValue.isString()) {
            initialParent = assignToValue.getStringValue();
        } else {
            Value[] arrayValue = assignToValue.getArrayValue();
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
            policyAuthor.graph().createObjectAttribute(
                    nameValue.getStringValue(),
                    new HashMap<>(),
                    initialParent,
                    parentsArr
            );
        } else {
            policyAuthor.graph().createUserAttribute(
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
        return String.format("create %s %s assign to %s;",
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
