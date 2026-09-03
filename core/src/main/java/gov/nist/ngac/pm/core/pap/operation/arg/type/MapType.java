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

package gov.nist.ngac.pm.core.pap.operation.arg.type;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Supported type for a Map.
 *
 * @param <K> the Java type of the map's keys
 * @param <V> the Java type of the map's values
 */
public final class MapType<K, V> extends Type<Map<K, V>> {

    /**
     * Builds a map type with the given key and value types.
     *
     * @param keyType the key type
     * @param valueType the value type
     * @return the map type
     */
    public static <K, V> MapType<K, V> of(Type<K> keyType, Type<V> valueType) {
        return new MapType<>(keyType.asType(keyType), valueType.asType(valueType));
    }

    private final Type<K> keyType;
    private final Type<V> valueType;

    public MapType(Type<K> keyType, Type<V> valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public Type<K> getKeyType() {
        return keyType;
    }

    public Type<V> getValueType() {
        return valueType;
    }

    @Override
    public Map<K, V> cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof Map<?, ?> map)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to Map");
        }
        Map<K, V> resultMap = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            K key = keyType.cast(entry.getKey());
            V value = valueType.cast(entry.getValue());
            resultMap.put(key, value);
        }
        return resultMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MapType<?, ?> mapType)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(keyType, mapType.keyType) && Objects.equals(valueType, mapType.valueType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), keyType, valueType);
    }
}
