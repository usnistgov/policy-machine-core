package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SetNodePropertiesStatement extends PALStatement {

    private final Expression nameExpr;
    private final Expression propertiesExpr;

    public SetNodePropertiesStatement(Expression nameExpr, Expression propertiesExpr) {
        this.nameExpr = nameExpr;
        this.propertiesExpr = propertiesExpr;
    }

    public Expression getNameExpr() {
        return nameExpr;
    }

    public Expression getPropertiesExpr() {
        return propertiesExpr;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        String name = nameExpr.execute(ctx, policyAuthor).getStringValue();
        Map<Value, Value> map = propertiesExpr.execute(ctx, policyAuthor).getMapValue();
        Map<String, String> properties = new HashMap<>();
        for (Value key : map.keySet()) {
            properties.put(key.getStringValue(), map.get(key).getStringValue());
        }

        policyAuthor.graph().setNodeProperties(name, properties);

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("set properties of %s to %s;", nameExpr, propertiesExpr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetNodePropertiesStatement that = (SetNodePropertiesStatement) o;
        return Objects.equals(nameExpr, that.nameExpr)
                && Objects.equals(propertiesExpr, that.propertiesExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameExpr, propertiesExpr);
    }
}
