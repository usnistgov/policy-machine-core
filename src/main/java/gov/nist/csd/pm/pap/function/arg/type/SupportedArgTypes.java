package gov.nist.csd.pm.pap.function.arg.type;

import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import java.util.List;
import java.util.Map;

public class SupportedArgTypes {

    public static <E> ArgType<List<E>> listType(ArgType<E> elementType) {
        return new ListType<>(elementType);
    }

    public static <K, V> ArgType<Map<K, V>> mapType(ArgType<K> keyType, ArgType<V> valueType) {
        return new MapType<>(keyType, valueType);
    }

    public static ArgType<String> stringType() {
        return new StringType();
    }

    public static ArgType<Long> longType() {
        return new LongType();
    }

    public static ArgType<Boolean> booleanType() {
        return new BooleanType();
    }

    public static ArgType<Object> objectType() {
        return new ObjectType();
    }
    
    public static ArgType<AccessRightSet> accessRightSetType() {
        return new AccessRightSetType();
    }
}
