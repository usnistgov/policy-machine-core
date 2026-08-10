package gov.nist.ngac.pm.core.pap.obligation.event.operation;

import gov.nist.ngac.pm.core.pap.pml.operation.routine.PMLStmtsRoutine;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementSerializable;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link MatchesOperationPattern}'s "on (args) { ... }" clause: the event context argument names bound
 * for the body, and the routine body that returns boolean.
 *
 * @param patternArgs the event context argument names bound for the body
 * @param func the pattern body
 */
public record OnPattern(Set<String> patternArgs, PMLStmtsRoutine<Boolean> func) implements PMLStatementSerializable {

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format(" on (%s) %s", String.join(", ", patternArgs), func.getStatements().toFormattedString(indentLevel));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OnPattern onPattern)) {
            return false;
        }
        return Objects.equals(patternArgs, onPattern.patternArgs) && Objects.equals(func,
            onPattern.func);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patternArgs, func);
    }
}
