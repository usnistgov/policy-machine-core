package gov.nist.csd.pm.policy.author.pal.model.expression;

import gov.nist.csd.pm.policy.author.pal.statement.Expression;

import java.io.Serializable;
import java.util.Objects;

public class MapEntryReference implements Serializable {

    private final VariableReference map;
    private final Expression key;

    public MapEntryReference(VariableReference map, Expression key) {
        this.map = map;
        this.key = key;
    }

    public VariableReference getMap() {
        return map;
    }

    public Expression getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapEntryReference that = (MapEntryReference) o;
        return Objects.equals(map, that.map)
                && Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map, key);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", map, key);
    }
}
