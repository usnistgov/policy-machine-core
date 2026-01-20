package gov.nist.csd.pm.core.pap.pml.expression.literal;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapLiteralExpression<K, V> extends Expression<Map<K, V>> {

    public static <K, V> MapLiteralExpression<K, V> of(Map<Expression<K>, Expression<V>> map, Type<K> keyType, Type<V> valueType) {
        return new MapLiteralExpression<>(new HashMap<>(map), keyType, valueType);
    }
    
    public static MapLiteralExpression<?, ?> of(Map<Expression<?>, Expression<?>> map) {
        return new MapLiteralExpression<>(map, ANY_TYPE, ANY_TYPE);
    }

    private final Map<Expression<?>, Expression<?>> compiledExpressions;
    private final Type<K> keyType;
    private final Type<V> valueType;

    public MapLiteralExpression(Map<Expression<?>, Expression<?>> compiledExpressions, Type<K> keyType, Type<V> valueType) {
        this.compiledExpressions = Objects.requireNonNull(compiledExpressions);
        this.keyType = Objects.requireNonNull(keyType);
        this.valueType = Objects.requireNonNull(valueType);
    }

    @Override
    public MapType<K, V> getType() {
        return MapType.of(keyType, valueType);
    }

    @Override
    public Map<K, V> execute(ExecutionContext ctx, PAP pap) throws PMException {
        Map<K, V> resultMap = new HashMap<>();
        
        for (var entry : compiledExpressions.entrySet()) {
            Object rawKey = entry.getKey().execute(ctx, pap);
            Object rawValue = entry.getValue().execute(ctx, pap);
            
            K key = keyType.cast(rawKey);
            V value = valueType.cast(rawValue);
            
            resultMap.put(key, value);
        }

        return resultMap;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        StringBuilder s = new StringBuilder();
        for (Expression<?> k : compiledExpressions.keySet()) {
            if (!s.isEmpty()) {
                s.append(", ");
            }

            s.append(k.toString()).append(": ").append(compiledExpressions.get(k));
        }

        return String.format("{%s}", s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MapLiteralExpression<?, ?> that)) {
            return false;
        }

        return Objects.equals(compiledExpressions, that.compiledExpressions) && Objects.equals(keyType,
            that.keyType) && Objects.equals(valueType, that.valueType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compiledExpressions, keyType, valueType);
    }
}