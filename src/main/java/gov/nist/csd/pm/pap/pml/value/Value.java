package gov.nist.csd.pm.pap.pml.value;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gov.nist.csd.pm.pap.exception.PMRuntimeException;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Value implements Serializable {

    protected Type type;

    public Value(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getStringValue() {
        return this.unwrap().to(StringValue.class).getValue();
    }

    protected Value unwrap() {
        if (this instanceof ReturnValue rv) {
            return rv.unwrap();
        }

        return this;
    }

    public Boolean getBooleanValue() {
        return this.unwrap().to(BoolValue.class).getValue();
    }

    public List<Value> getArrayValue() {
        return this.unwrap().to(ArrayValue.class).getValue();
    }

    public Map<Value, Value> getMapValue() {
        return this.unwrap().to(MapValue.class).getValue();
    }

    public Value getProhibitionValue() {
        return this.unwrap().to(ProhibitionValue.class).getValue();
    }

    public Rule getRuleValue() {
        return this.unwrap().to(RuleValue.class).getValue();
    }

    public Pattern getPatternValue() {
        return this.unwrap().to(PatternValue.class).getValue();
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
            List<Object> list = new ArrayList<>();
            List<Value> arrayValue = getArrayValue();
            for (Value value : arrayValue) {
                list.add(value.toObject());
            }

            return list;
        } else if (type.isMap()) {
            Map<Object, Object> map = new HashMap<>();
            Map<Value, Value> mapValue = getMapValue();
            for (Map.Entry<Value, Value> entry : mapValue.entrySet()) {
                map.put(entry.getKey().toObject(), entry.getValue().toObject());
            }

            return map;
        } else if (type.isVoid()){
            return null;
        } else if (type.isPattern()) {
            return getPatternValue();
        }

        throw new PMRuntimeException("cannot convert value of type " + type + " to an object");
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
            case List list -> toListValue(list);
            case Boolean b -> new BoolValue(b);
            case Map m -> toMapValue(m);
            default -> objToValue(o);
        };
    }

    private static ArrayValue toListValue(List list) {
        List<Value> valueList = new ArrayList<>();
        for (Object arrObj : list) {
            valueList.add(fromObject(arrObj));
        }

        return new ArrayValue(valueList, Type.array(Type.any()));
    }

    private static MapValue toMapValue(Map m) {
        Map<Value, Value> map = new HashMap<>();
        for (Object key : m.keySet()) {
            map.put(fromObject(key), fromObject(m.get(key)));
        }

        return new MapValue(map, Type.string(), Type.any());
    }

    private static MapValue objToValue(Object o) {
        Gson gson = new Gson();
        String json = gson.toJson(o);
        Map<Object, Object> map = gson.fromJson(json, new TypeToken<Map<Object, Object>>() {}.getType());

        Map<Value, Value> valueMap = new HashMap<>();
        for (Map.Entry<Object, Object> e : map.entrySet()) {
            valueMap.put(fromObject(e.getKey()), fromObject(e.getValue()));
        }

        return new MapValue(valueMap, Type.string(), Type.any());
    }
}
