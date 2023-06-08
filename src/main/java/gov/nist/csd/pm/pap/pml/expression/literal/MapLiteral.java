package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.MapValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapLiteral extends Literal {

    private final Map<Expression, Expression> map;
    private final Type type;

    public MapLiteral(Map<Expression, Expression> map, Type keyType, Type valueType) {
        this.map = map;
        this.type = Type.map(keyType, valueType);
    }

    @Override
    public Type getType(Scope<Variable, PMLExecutableSignature> scope) throws PMLScopeException {
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

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        Map<Value, Value> values = new HashMap<>();

        for (Expression keyExpr : map.keySet()) {
            Expression valueExpr = map.get(keyExpr);

            Value key = keyExpr.execute(ctx, pap);
            Value value = valueExpr.execute(ctx, pap);

            values.put(key, value);
        }

        return new MapValue(values, type.getMapKeyType(), type.getMapValueType());
    }
}
