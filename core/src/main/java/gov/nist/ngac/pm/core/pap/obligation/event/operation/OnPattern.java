/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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
