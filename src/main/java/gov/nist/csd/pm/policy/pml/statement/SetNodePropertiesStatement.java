package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SetNodePropertiesStatement extends PMLStatement {

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
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        String name = nameExpr.execute(ctx, policy).getStringValue();
        Map<Value, Value> map = propertiesExpr.execute(ctx, policy).getMapValue();
        Map<String, String> properties = new HashMap<>();
        for (Value key : map.keySet()) {
            properties.put(key.getStringValue(), map.get(key).getStringValue());
        }

        policy.graph().setNodeProperties(name, properties);

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("set properties of %s to %s", nameExpr, propertiesExpr);
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
