package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.Objects;

public class DeleteStatement extends PALStatement {

    private final Type type;
    private final Expression expression;

    public DeleteStatement(Type type, Expression expression) {
        this.type = type;
        this.expression = expression;
    }

    public Type getType() {
        return type;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        String name = expression.execute(ctx, policyAuthor).getStringValue();
        if (type == Type.PROHIBITION) {
            policyAuthor.prohibitions().delete(name);
        } else if (type == Type.OBLIGATION) {
            policyAuthor.obligations().delete(name);
        } else {
            policyAuthor.graph().deleteNode(name);
        }

        return new Value();
    }

    @Override
    public String toString() {
        String typeStr = "";
        switch (type) {
            case PROHIBITION -> typeStr = "prohibition";
            case OBLIGATION -> typeStr = "obligation";
            case POLICY_CLASS -> typeStr = "policy class";
            case OBJECT_ATTRIBUTE -> typeStr = "object attribute";
            case USER_ATTRIBUTE -> typeStr = "user attribute";
            case OBJECT -> typeStr = "object";
            case USER -> typeStr = "user";
        }

        return String.format("delete %s %s;", typeStr, expression);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteStatement that = (DeleteStatement) o;
        return type == that.type && Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, expression);
    }

    public enum Type {
        POLICY_CLASS,
        OBJECT_ATTRIBUTE,
        USER_ATTRIBUTE,
        OBJECT,
        USER,
        PROHIBITION,
        OBLIGATION
    }
}
