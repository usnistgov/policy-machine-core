package gov.nist.csd.pm.pap.function.arg.type;

import java.io.Serializable;

public sealed abstract class ArgType<T> implements Serializable
    permits StringType, LongType, BooleanType, ListType, MapType, ObjectType, AccessRightSetType, OperationType,
    RoutineType, RuleType, ProhibitionSubjectType, ContainerConditionType, NodeTypeType {

    /**
     * Safely cast a given object into the type defined in T. If obj is not convertable to T an
     * IllegalArgumentException will be thrown.
     *
     * @param obj the object to convert to T.
     * @return an instance of T from obj.
     */
    public abstract T cast(Object obj);

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
}





