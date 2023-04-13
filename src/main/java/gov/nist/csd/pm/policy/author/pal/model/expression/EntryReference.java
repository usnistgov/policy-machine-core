package gov.nist.csd.pm.policy.author.pal.model.expression;

import gov.nist.csd.pm.policy.author.pal.statement.Expression;

import java.io.Serializable;
import java.util.Objects;

public class EntryReference implements Serializable {

    private final VariableReference varRef;
    private final Expression key;

    public EntryReference(VariableReference varRef, Expression key) {
        this.varRef = varRef;
        this.key = key;
    }

    public VariableReference getVarRef() {
        return varRef;
    }

    public Expression getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntryReference that = (EntryReference) o;
        return Objects.equals(varRef, that.varRef)
                && Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varRef, key);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", varRef, key);
    }
}
