package gov.nist.csd.pm.policy.pml.expression.literal;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.scope.Scope;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.MapValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapLiteral extends Literal {

    private final Map<Expression, Expression> map;
    private final Type type;

    public MapLiteral(Map<Expression, Expression> map, Type keyType, Type valueType) {
        this.map = map;
        this.type = Type.map(keyType, valueType);
    }

    public MapLiteral(Type keyType, Type valueType) {
        this.map = new HashMap<>();
        this.type = Type.map(keyType, valueType);
    }

    @Override
    public Type getType(Scope scope) throws PMLScopeException {
        return type;
    }

    public void put(Expression key, Expression value){
        this.map.put(key, value);
    }

    public Map<Expression, Expression> getMap() {
        return map;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapLiteral that = (MapLiteral) o;
        return Objects.equals(this.map, that.map)
                && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        Map<Value, Value> values = new HashMap<>();
        Type keyType = null;
        Type valueType = null;

        for (Expression keyExpr : map.keySet()) {
            Expression valueExpr = map.get(keyExpr);

            Value key = keyExpr.execute(ctx, policy);
            Value value = valueExpr.execute(ctx, policy);

            if (keyType == null) {
                keyType = key.getType();
            } else if (!keyType.equals(key.getType())) {
                keyType = Type.any();
            }

            if (valueType == null) {
                valueType = value.getType();
            } else if (!valueType.equals(value.getType())) {
                keyType = Type.any();
            }

            values.put(key, value);
        }

        return new MapValue(values, Type.any(), Type.any());
    }

    @Override
    public String toFormattedString(int indentLevel) {
        StringBuilder s = new StringBuilder();
        for (Expression k : map.keySet()) {
            if (s.length() > 0) {
                s.append(", ");
            }

            s.append(k.toString()).append(": ").append(map.get(k));
        }

        return String.format("{%s}", s);
    }
}
