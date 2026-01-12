package gov.nist.csd.pm.core.pap.function.arg.type;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public sealed abstract class Type<T> implements Serializable
    permits AnyType, BooleanType, ListType, LongType, MapType, StringType, VoidType, RuleType, RoutineType,
    OperationType, ProhibitionSubjectArgType, ContainerConditionType, NodeType {

    public static StringType STRING_TYPE = new StringType();
    public static LongType LONG_TYPE = new LongType();
    public static BooleanType BOOLEAN_TYPE = new BooleanType();
    public static AnyType ANY_TYPE = new AnyType();
    public static VoidType VOID_TYPE = new VoidType();

    public static Type<?> resolveTypeOfObject(Object o) {
        return switch (o) {
            case String s -> STRING_TYPE;
            case Boolean b -> BOOLEAN_TYPE;
            case Long l -> LONG_TYPE;
            case List<?> list -> resolveListType(list);
            case Map<?, ?> map -> resolveMapType(map);
            case null, default -> ANY_TYPE;
        };
    }

    private static Type<?> resolveListType(List<?> list) {
        if (list == null || list.isEmpty()) {
            return ANY_TYPE;
        }

        Object firstElement = list.getFirst();
        if (firstElement == null) {
            return ANY_TYPE;
        }

        Type<?> firsType = resolveTypeOfObject(firstElement);
        for (int i = 1; i < list.size(); i++) {
            Object element = list.get(i);

            if (element == null) {
                return ANY_TYPE;
            }

            Type<?> elementType = resolveTypeOfObject(element);
            if (!elementType.getClass().equals(firsType.getClass())) {
                return ANY_TYPE;
            }
        }

        return firsType;
    }

    private static MapType<?, ?> resolveMapType(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return new MapType<>(ANY_TYPE, ANY_TYPE);
        }

        Type<?> keyType = getMapElementType(map.keySet());
        Type<?> valueType = getMapElementType(map.values());

        return new MapType<>(keyType, valueType);
    }

    private static Type<?> getMapElementType(Collection<?> values) {
        if (values.isEmpty()) {
            return ANY_TYPE;
        }

        Object firstValue = values.iterator().next();
        if (firstValue == null) {
            return ANY_TYPE;
        }

        Type<?> firstType = resolveTypeOfObject(firstValue);
        for (Object value : values) {
            if (value == null) {
                return ANY_TYPE;
            }

            Type<?> valueType = resolveTypeOfObject(value);
            if (!valueType.equals(firstType)) {
                return ANY_TYPE;
            }
        }

        return firstType;
    }

    /**
     * Safely cast a given object into the type defined in T. If obj is not convertable to T an
     * IllegalArgumentException will be thrown.
     *
     * @param obj the object to convert to T.
     * @return an instance of T from obj.
     */
    public abstract T cast(Object obj);

    public boolean isCastableTo(Type<?> targetType) {
        if (this.equals(ANY_TYPE)) {
            return true;
        } else if (targetType.equals(Type.ANY_TYPE) || this.equals(targetType)) {
            return true;
        } else if ((this instanceof ListType<?> sourceList) && (targetType instanceof ListType<?> targetList)) {
            return sourceList.getElementType().isCastableTo(targetList.getElementType());
        } else if ((this instanceof MapType<?, ?> sourceMap) && targetType instanceof MapType<?, ?> targetMap) {
            return sourceMap.getKeyType().isCastableTo(targetMap.getKeyType()) &&
                sourceMap.getValueType().isCastableTo(targetMap.getValueType());
        }        

        return false;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass();
    }

    
    public <S> Type<S> asType(Type<S> targetType) {
        if (!this.isCastableTo(targetType)) {
            throw new IllegalArgumentException("Cannot cast from " + this + " to " + targetType);
        }
        
        return (Type<S>) this;
    }
}





