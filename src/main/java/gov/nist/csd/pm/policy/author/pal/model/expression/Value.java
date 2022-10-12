package gov.nist.csd.pm.policy.author.pal.model.expression;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.obligation.Rule;

import java.io.Serializable;
import java.util.*;

public class Value implements Serializable {

    private String sValue;
    private boolean isString;

    private boolean isNumber;
    private int nValue;

    private boolean bValue;
    private boolean isBoolean;

    private Value[] aValue;
    private boolean isArray;

    private Map<Value, Value> mValue;
    private boolean isMap;

    private boolean isVoid;

    private boolean isReturn;
    private boolean isBreak;
    private boolean isContinue;
    private Value value;
    private boolean isValue;
    private boolean isNull;
    private Type type;
    private Rule rule;
    private boolean isRule;

    public static Value returnValue(Value value) {
        value.isReturn = true;
        return value;
    }

    public static Value breakValue() {
        Value value = new Value();
        value.isBreak = true;
        return value;
    }

    public static Value continueValue() {
        Value value = new Value();
        value.isContinue = true;
        return value;
    }

    public static Value nullValue() {
        Value value = new Value();
        value.isNull = true;
        return value;
    }

    public Value() {
        this.isVoid = true;
        this.type = Type.voidType();
    }

    public Value(Value value) {
        this.isValue = true;
        this.value = value;
        this.type = Type.any();
    }

    public Value(String value) {
        this.isString = true;
        this.sValue = value;
        this.type = Type.string();
    }

    public Value(int value) {
        this.isNumber = true;
        this.nValue = value;
        this.type = Type.number();
    }

    public Value(boolean value) {
        this.isBoolean = true;
        this.bValue = value;
        this.type = Type.bool();
    }

    public Value(Value[] value) {
        this.isArray = true;
        this.aValue = value;
        if (value.length == 0) {
            this.type = Type.any();
        } else {
            this.type = Type.array(value[0].getType());
        }
    }

    public Value(AccessRightSet accessRightSet) {
        this.isArray = true;
        this.type = Type.array(Type.string());

        String[] arr = accessRightSet.toArray(String[]::new);
        this.aValue = new Value[arr.length];
        for (int i = 0; i < arr.length; i++) {
            this.aValue[i] = new Value(arr[i]);
        }
    }

    public Value(Map<Value, Value> value) {
        this.isMap = true;
        this.mValue = value;
        if (value.isEmpty()) {
            this.type = Type.map(Type.any(), Type.any());
        } else {
            Value key = value.keySet().iterator().next();
            this.type = Type.map(key.getType(), value.get(key).getType());
        }
    }

    // these should only be used be the CreateObligationStmt
    public Value(Rule rule) {
        this.isRule = true;
        this.rule = rule;
    }

    public Rule getRule() {
        return rule;
    }

    public boolean isReturn() {
        return isReturn;
    }

    public boolean isBreak() {
        return isBreak;
    }

    public boolean isContinue() {
        return isContinue;
    }

    public boolean isString() {
        return isString;
    }

    public boolean isNumber() {
        return isNumber;
    }

    public boolean isBoolean() {
        return isBoolean;
    }

    public boolean isArray() {
        return this.isArray;
    }

    public boolean isMap() {
        return isMap;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public boolean isNull() {
        return isNull;
    }

    public boolean isValue() {
        return isValue;
    }

    public boolean isRule() {
        return isRule;
    }

    public String getStringValue() {
        if (!isString) {
            throw new IllegalStateException("expected value to be string but was " + type);
        }

        return sValue;
    }

    public int getNumberValue() {
        if (!isNumber) {
            throw new IllegalStateException("expected value to be number but was " + type);
        }

        return nValue;
    }

    public boolean getBooleanValue() {
        if (!isBoolean) {
            throw new IllegalStateException("expected value to be boolean but was " + type);
        }

        return bValue;
    }

    public Value[] getArrayValue() {
        if (!isArray) {
            throw new IllegalStateException("expected value to be array but was " + type);
        }

        return aValue;
    }

    public Map<Value, Value> getMapValue() {
        if (!isMap) {
            throw new IllegalStateException("expected value to be map but was " + type);
        }

        return mValue;
    }

    public Value getValue() {
        if (!isValue) {
            throw new IllegalStateException("expected value to be Value but was " + type);
        }

        return value;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value value1 = (Value) o;
        return isString == value1.isString
                && bValue == value1.bValue
                && isBoolean == value1.isBoolean
                && isArray == value1.isArray
                && isMap == value1.isMap
                && isVoid == value1.isVoid
                && isReturn == value1.isReturn
                && isBreak == value1.isBreak
                && isContinue == value1.isContinue
                && isNull == value1.isNull
                && isValue == value1.isValue
                && Objects.equals(sValue, value1.sValue)
                && Arrays.equals(aValue, value1.aValue)
                && Objects.equals(mValue, value1.mValue)
                && Objects.equals(value, value1.value)
                && Objects.equals(type, value1.type)
                && Objects.equals(rule, value1.rule);
    }

    @Override
    public int hashCode() {
        if (isString) {
            return sValue.hashCode();
        } else if (isBoolean) {
            return Objects.hashCode(bValue);
        } else if (isArray) {
            return Arrays.hashCode(aValue);
        } else if (isMap) {
            return mValue.hashCode();
        } else if (isValue) {
            return value.hashCode();
        } else if (isRule) {
            return rule.hashCode();
        } else if (isVoid) {
            return "void".hashCode();
        } else if (isReturn) {
            return "return".hashCode();
        } else if (isBreak) {
            return "break".hashCode();
        } else if (isContinue) {
            return "continue".hashCode();
        } else if (isNull) {
            return "null".hashCode();
        }

        return -1;
    }

    public static Value objectToValue(Object o) {
        if (o == null) {
            return Value.nullValue();
        }

        Value value;
        if (o instanceof String s) {
            value = new Value(s);
        } else if (o instanceof Integer i) {
            value = new Value(i);
        } else if (o.getClass().isArray()) {
            Object[] arr = (Object[])o;
            Value[] valueArr = new Value[arr.length];
            for (int i = 0; i < arr.length; i++) {
                Object arrObj = arr[i];
                valueArr[i] = objectToValue(arrObj);
            }

            value = new Value(valueArr);

        } else if (o instanceof List list) {
            List<Value> valueList = new ArrayList<>();
            for (Object arrObj : list) {
                valueList.add(objectToValue(arrObj));
            }

            value = new Value(valueList.toArray(new Value[]{}));

        } else if (o instanceof Boolean b) {
            value = new Value(b);

        } else if (o instanceof Map m) {
            Map<Value, Value> map = new HashMap<>();
            for (Object key : m.keySet()) {
                map.put(objectToValue(key), objectToValue(m.get(key)));
            }

            value = new Value(map);
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.convertValue(o, Map.class);

            Map<Value, Value> valueMap = new HashMap<>();
            for (Object key : map.keySet()) {
                String field = key.toString();
                Object obj = map.get(field);
                valueMap.put(new Value(field), objectToValue(obj));
            }

            value = new Value(valueMap);
        }

        return value;
    }

    @Override
    public String toString() {
        if (isString()) {
            return String.format("'%s'", getStringValue());
        } else if (isNumber()) {
            return String.valueOf(getNumberValue());
        } else if (isBoolean()) {
            return String.valueOf(getBooleanValue());
        } else if (isMap()) {
            return mapToString(getMapValue());
        } else if (isArray()) {
            return Arrays.toString(getArrayValue());
        } else if (isVoid()) {
            return "void";
        } else if (isReturn()) {
            return "return";
        } else if (isBreak()) {
            return "break";
        } else if (isContinue()) {
            return "continue";
        } else if (isValue()) {
            return value.toString();
        } else if (isNull()) {
            return "null";
        } else {
            return getRule().toString();
        }
    }

    private String mapToString(Map<Value, Value> mapValue) {
        StringBuilder entries = new StringBuilder();
        for (Value k : mapValue.keySet()) {
            if (entries.length() > 0) {
                entries.append(", ");
            }

            entries.append(k.toString()).append(": ").append(mapValue.get(k).toString());
        }

        return String.format("{%s}", entries);
    }
}
