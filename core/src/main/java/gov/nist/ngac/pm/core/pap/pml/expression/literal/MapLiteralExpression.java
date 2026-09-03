/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.expression.literal;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * PML map literal expression, evaluating each key/value expression pair into a map.
 *
 * @param <K> the Java type of the map's keys
 * @param <V> the Java type of the map's values
 */
public class MapLiteralExpression<K, V> extends Expression<Map<K, V>> {

    /**
     * Builds a map literal from same-typed key and value expressions.
     */
    public static <K, V> MapLiteralExpression<K, V> of(Map<Expression<K>, Expression<V>> map, Type<K> keyType, Type<V> valueType) {
        return new MapLiteralExpression<>(new HashMap<>(map), keyType, valueType);
    }

    /**
     * Builds a map literal with ANY_TYPE keys and values.
     */
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