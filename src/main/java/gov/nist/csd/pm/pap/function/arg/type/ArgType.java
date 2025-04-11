package gov.nist.csd.pm.pap.function.arg.type;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public sealed abstract class ArgType<T> implements Serializable
    permits StringType, LongType, BooleanType, ListType, MapType, ObjectType, AccessRightSetType, OperationType,
    RoutineType, RuleType, ProhibitionSubjectType, ContainerConditionType, NodeTypeType, VoidType {

    public static StringType STRING_TYPE = new StringType();
    public static LongType LONG_TYPE = new LongType();
    public static BooleanType BOOLEAN_TYPE = new BooleanType();
    public static ObjectType OBJECT_TYPE = new ObjectType();
    public static AccessRightSetType ACCESS_RIGHT_SET_TYPE = new AccessRightSetType();
    public static OperationType OPERATION_TYPE = new OperationType();
    public static RoutineType ROUTINE_TYPE = new RoutineType();
    public static RuleType RULE_TYPE = new RuleType();
    public static ProhibitionSubjectType PROHIBITION_SUBJECT_TYPE = new ProhibitionSubjectType();
    public static ContainerConditionType CONTAINER_CONDITION_TYPE = new ContainerConditionType();
    public static NodeTypeType NODE_TYPE_TYPE = new NodeTypeType();
    public static VoidType VOID_TYPE = new VoidType();

    public static <E> ListType<E> listType(ArgType<E> elementType) {
        return new ListType<>(elementType);
    }

    public static <K, V> MapType<K, V> mapType(ArgType<K> keyType, ArgType<V> valueType) {
        return new MapType<>(keyType, valueType);
    }

    public static ArgType<?> resolveTypeOfObject(Object o) {
        return switch (o) {
            case String s -> STRING_TYPE;
            case Boolean b -> BOOLEAN_TYPE;
            case Long l -> LONG_TYPE;
            case List<?> list -> resolveListType(list);
            case Map<?, ?> map -> resolveMapType(map);
            case null, default -> OBJECT_TYPE;
        };
    }

    private static ArgType<?> resolveListType(List<?> list) {
        if (list == null || list.isEmpty()) {
            return OBJECT_TYPE;
        }

        Object firstElement = list.getFirst();
        if (firstElement == null) {
            return OBJECT_TYPE;
        }

        ArgType<?> firsType = resolveTypeOfObject(firstElement);
        for (int i = 1; i < list.size(); i++) {
            Object element = list.get(i);

            if (element == null) {
                return OBJECT_TYPE;
            }

            ArgType<?> elementType = resolveTypeOfObject(element);
            if (!elementType.getClass().equals(firsType.getClass())) {
                return OBJECT_TYPE;
            }
        }

        return firsType;
    }

    private static MapType<?, ?> resolveMapType(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return new MapType<>(OBJECT_TYPE, OBJECT_TYPE);
        }

        ArgType<?> keyType = getMapElementType(map.keySet());
        ArgType<?> valueType = getMapElementType(map.values());

        return new MapType<>(keyType, valueType);
    }

    private static ArgType<?> getMapElementType(Collection<?> values) {
        if (values.isEmpty()) {
            return OBJECT_TYPE;
        }

        Object firstValue = values.iterator().next();
        if (firstValue == null) {
            return OBJECT_TYPE;
        }

        ArgType<?> firstType = resolveTypeOfObject(firstValue);
        for (Object value : values) {
            if (value == null) {
                return OBJECT_TYPE;
            }

            ArgType<?> valueType = resolveTypeOfObject(value);
            if (!valueType.equals(firstType)) {
                return OBJECT_TYPE;
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

    public abstract Class<T> getExpectedClass();

    public boolean isCastableTo(ArgType<?> targetType) {
        if (this.equals(OBJECT_TYPE)) {
            return true;
        } else if (targetType.equals(ArgType.OBJECT_TYPE) || this.equals(targetType)) {
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

    
    public <S> ArgType<S> asType(ArgType<S> targetType) {
        if (!this.isCastableTo(targetType)) {
            throw new IllegalArgumentException("Cannot cast from " + this + " to " + targetType);
        }
        
        return (ArgType<S>) this;
    }
}





