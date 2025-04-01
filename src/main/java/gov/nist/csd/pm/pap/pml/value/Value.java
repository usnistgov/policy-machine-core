package gov.nist.csd.pm.pap.pml.value;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gov.nist.csd.pm.common.exception.PMRuntimeException;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Value implements Serializable {
    private static final Gson GSON = new Gson();
    private static final TypeToken<Map<Object, Object>> MAP_TYPE_TOKEN = new TypeToken<>() {};

    protected final Type type;

    protected Value(Type type) {
        this.type = Objects.requireNonNull(type, "Type cannot be null");
    }

    public Type getType() {
        return type;
    }

    public String getStringValue() {
        return unwrap().to(StringValue.class).getValue();
    }

    protected Value unwrap() {
        return this instanceof ReturnValue rv ? rv.unwrap() : this;
    }

    public Boolean getBooleanValue() {
        return unwrap().to(BoolValue.class).getValue();
    }

    public List<Value> getArrayValue() {
        return unwrap().to(ArrayValue.class).getValue();
    }

    public Map<Value, Value> getMapValue() {
        return unwrap().to(MapValue.class).getValue();
    }

    public Value getProhibitionValue() {
        return unwrap().to(ProhibitionValue.class).getValue();
    }

    public Rule getRuleValue() {
        return unwrap().to(RuleValue.class).getValue();
    }

    public Pattern getPatternValue() {
        return unwrap().to(PatternValue.class).getValue();
    }

    public <T extends Value> T to(Class<T> c) {
        return c.cast(this);
    }

    public Object toObject() {
        if (type.isString()) {
            return getStringValue();
        } else if (type.isBoolean()) {
            return getBooleanValue();
        } else if (type.isArray()) {
            return convertArrayToObject();
        } else if (type.isMap()) {
            return convertMapToObject();
        } else if (type.isVoid()) {
            return null;
        }

        throw new PMRuntimeException("Cannot convert value of type " + type + " to an object");
    }

    private List<Object> convertArrayToObject() {
        List<Object> list = new ArrayList<>();
        for (Value value : getArrayValue()) {
            list.add(value.toObject());
        }
        return list;
    }

    private Map<Object, Object> convertMapToObject() {
        Map<Object, Object> map = new HashMap<>();
        for (Map.Entry<Value, Value> entry : getMapValue().entrySet()) {
            map.put(entry.getKey().toObject(), entry.getValue().toObject());
        }
        return map;
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

    public static Value fromObject(Object o) {
        return switch (o) {
            case null -> null;
            case Value value -> value;
            case String s -> new StringValue(s);
            case List<?> list -> toListValue(list);
            case Boolean b -> new BoolValue(b);
            case Map<?, ?> m -> toMapValue(m);
            case NodeType t -> new StringValue(t.toString());
            default -> objToValue(o);
        };
    }

    private static ArrayValue toListValue(List<?> list) {
        List<Value> valueList = new ArrayList<>(list.size());
        for (Object arrObj : list) {
            valueList.add(fromObject(arrObj));
        }
        return new ArrayValue(valueList, Type.array(Type.any()));
    }

    private static MapValue toMapValue(Map<?, ?> m) {
        Map<Value, Value> map = new HashMap<>(m.size());
        for (Map.Entry<?, ?> entry : m.entrySet()) {
            map.put(fromObject(entry.getKey()), fromObject(entry.getValue()));
        }
        return new MapValue(map, Type.string(), Type.any());
    }

    private static MapValue objToValue(Object o) {
        String json = GSON.toJson(o);
        Map<Object, Object> map = GSON.fromJson(json, MAP_TYPE_TOKEN.getType());

        Map<Value, Value> valueMap = new HashMap<>(map.size());
        for (Map.Entry<Object, Object> e : map.entrySet()) {
            valueMap.put(fromObject(e.getKey()), fromObject(e.getValue()));
        }

        return new MapValue(valueMap, Type.string(), Type.any());
    }
}
