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

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *Supported types in the policy machine.
 *
 * @param <T> the Java type this PML type maps to
 */
public sealed abstract class Type<T> implements Serializable
    permits AdminOperationType, AnyType, FunctionType, BooleanType, EventPatternType,
    ListType, LongType, MapType, ObligationResponseType, ResourceOperationType, RoutineType,
    StringType, VoidType, QueryOperationType {

    /**
     * Infers the {@link Type} of a runtime Java value. A list or map resolves to ANY_TYPE unless every
     * element/key/value shares a single inferred type.
     *
     * @param o the value to infer a type for
     * @return the inferred type
     */
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
            if (!elementType.equals(firsType)) {
                return ANY_TYPE;
            }
        }

        return ListType.of(firsType);
    }

    private static MapType<?, ?> resolveMapType(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return new MapType<>(ANY_TYPE, ANY_TYPE);
        }

        Type<?> keyType = getMapElementType(map.keySet());
        Type<?> valueType = getMapElementType(map.values());

        return MapType.of(keyType, valueType);
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

    /**
     * Checks whether a value of this type can be used where the target type is expected — always true if
     * either type is ANY_TYPE, or, for list/map types, if their element/key/value types are recursively
     * castable.
     *
     * @param targetType the type to check castability to
     * @return whether this type is castable to the target type
     */
    public boolean isCastableTo(Type<?> targetType) {
        if (this.equals(ANY_TYPE)) {
            return true;
        } else if (targetType.equals(ANY_TYPE) || this.equals(targetType)) {
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

    
    /**
     * Returns this type viewed as the target type, without converting any value.
     *
     * @param <S> the target Java type
     * @param targetType the type to view this type as
     * @return this instance, unsafely cast to the target type parameter
     * @throws IllegalArgumentException if this type is not castable to the target type
     */
    public <S> Type<S> asType(Type<S> targetType) {
        if (!this.isCastableTo(targetType)) {
            throw new IllegalArgumentException("Cannot cast from " + this + " to " + targetType);
        }
        
        return (Type<S>) this;
    }
}





