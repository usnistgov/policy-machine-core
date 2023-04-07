package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.O;

public class CreateUserOrObjectStatement extends PALStatement {

    private final Expression name;
    private final NodeType type;
    private final Expression assignTo;

    public CreateUserOrObjectStatement(Expression name, NodeType type, Expression assignTo) {
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
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        Value nameValue = name.execute(ctx, policyAuthor);
        Value assignToValue = assignTo.execute(ctx, policyAuthor);

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

        if (type == O) {
            policyAuthor.createObject(
                    nameValue.getStringValue(),
                    new HashMap<>(),
                    initialParent,
                    parentsArr
            );
        } else {
            policyAuthor.createUser(
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
        return String.format(
                "create %s %s in %s;",
                (type == O ? "object" : "user"),
                name,
                assignTo
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateUserOrObjectStatement that = (CreateUserOrObjectStatement) o;
        return Objects.equals(name, that.name) && type == that.type && Objects.equals(assignTo, that.assignTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, assignTo);
    }
}
