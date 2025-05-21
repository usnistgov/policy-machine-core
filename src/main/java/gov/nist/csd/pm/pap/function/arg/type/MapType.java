package gov.nist.csd.pm.pap.function.arg.type;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class MapType<K, V> extends Type<Map<K, V>> {

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
