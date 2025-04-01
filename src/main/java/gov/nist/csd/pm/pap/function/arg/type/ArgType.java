package gov.nist.csd.pm.pap.function.arg.type;

import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.function.routine.Routine;
import gov.nist.csd.pm.pap.obligation.Rule;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public sealed abstract class ArgType<T> implements Serializable
    permits StringType, LongType, BooleanType, ListType, MapType, ObjectType, AccessRightSetType, OperationType,
    RoutineType, RuleType, ProhibitionSubjectType, ContainerConditionType, NodeTypeType {

    private static StringType stringType;
    private static LongType longType;
    private static BooleanType booleanType;
    private static ObjectType objectType;
    private static AccessRightSetType accessRightSetType;
    private static OperationType operationType;
    private static RoutineType routineType;
    private static RuleType ruleType;
    private static ProhibitionSubjectType prohibitionSubjectType;
    private static ContainerConditionType containerConditionType;
    private static NodeTypeType nodeTypeType;

    public static <E> ArgType<List<E>> listType(ArgType<E> elementType) {
        return new ListType<>(elementType);
    }

    public static <K, V> ArgType<Map<K, V>> mapType(ArgType<K> keyType, ArgType<V> valueType) {
        return new MapType<>(keyType, valueType);
    }

    public static ArgType<String> stringType() {
        if (stringType == null) {
            stringType = new StringType();
        }

        return stringType;
    }

    public static ArgType<Long> longType() {
        if (longType == null) {
            longType = new LongType();
        }

        return longType;
    }

    public static ArgType<Boolean> booleanType() {
        if (booleanType == null) {
            booleanType = new BooleanType();
        }

        return booleanType;
    }

    public static ArgType<Object> objectType() {
        if (objectType == null) {
            objectType = new ObjectType();
        }

        return objectType;
    }

    public static ArgType<AccessRightSet> accessRightSetType() {
        if (accessRightSetType == null) {
            accessRightSetType = new AccessRightSetType();
        }

        return accessRightSetType;
    }

    public static ArgType<Operation<?>> operationType() {
        if (operationType == null) {
            operationType = new OperationType();
        }

        return operationType;
    }

    public static ArgType<Routine<?>> routineType() {
        if (routineType == null) {
            routineType = new RoutineType();
        }

        return routineType;
    }

    public static ArgType<Rule> ruleType() {
        if (ruleType == null) {
            ruleType = new RuleType();
        }

        return ruleType;
    }

    public static ArgType<ProhibitionSubject> prohibitionSubjectType() {
        if (prohibitionSubjectType == null) {
            prohibitionSubjectType = new ProhibitionSubjectType();
        }

        return prohibitionSubjectType;
    }

    public static ArgType<ContainerCondition> containerConditionType() {
        if (containerConditionType == null) {
            containerConditionType = new ContainerConditionType();
        }

        return containerConditionType;
    }

    public static ArgType<NodeType> nodeTypeType() {
        if (nodeTypeType == null) {
            nodeTypeType = new NodeTypeType();
        }

        return nodeTypeType;
    }

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





