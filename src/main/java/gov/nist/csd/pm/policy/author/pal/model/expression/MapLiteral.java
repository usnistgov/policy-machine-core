package gov.nist.csd.pm.policy.author.pal.model.expression;

import gov.nist.csd.pm.policy.author.pal.statement.Expression;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MapLiteral implements Serializable {

    private final Map<Expression, Expression> map;
    private final Type type;

    public MapLiteral(Map<Expression, Expression> map, Type keyType, Type valueType) {
        this.map = map;
        this.type = Type.map(keyType, valueType);
    }

    public MapLiteral(Type type) {
        this.map = new HashMap<>();
        this.type = type;
    }

    public void put(Expression key, Expression value){
        this.map.put(key, value);
    }

    public Map<Expression, Expression> getMap() {
        return map;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapLiteral that = (MapLiteral) o;
        return Objects.equals(this.map, that.map)
                && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Expression k : map.keySet()) {
            if (s.length() > 0) {
                s.append(", ");
            }

            s.append(k.toString()).append(": ").append(map.get(k));
        }

        return String.format("{%s}", s);
    }
}
