package gov.nist.csd.pm.policy.pml.value;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.pml.type.Type;

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

    public <T extends Value> T to(Class<T> c) {
        return c.cast(this);
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

    public static Value fromObject(Object o) {
        if (o instanceof String s) {
            return new StringValue(s);
        } if (o instanceof List list) {
            return toListValue(list);
        } else if (o instanceof Boolean b) {
            return new BoolValue(b);
        } else if (o instanceof Map m) {
            return toMapValue(m);
        } else {
            return objToValue(o);
        }
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
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.convertValue(o, Map.class);

        Map<Value, Value> valueMap = new HashMap<>();
        for (Object key : map.keySet()) {
            Value keyTV = fromObject(key);
            Object obj = map.get(key);
            valueMap.put(keyTV, fromObject(obj));
        }

        return new MapValue(valueMap, Type.string(), Type.any());
    }
}
