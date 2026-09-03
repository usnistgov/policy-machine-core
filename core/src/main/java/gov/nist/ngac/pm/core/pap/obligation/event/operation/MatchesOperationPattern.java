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

import gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.operation.routine.PMLStmtsRoutine;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.ReturnStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * PML matches pattern, with name and optional on (...) { ... } clause. Matches events for the
 * named operation, and only if the on clause (when present) evaluates true.
 */
public final class MatchesOperationPattern extends OperationPattern {

    private final String opName;
    private final OnPattern onPattern;

    // whether an `on (...) { ... }` clause was actually authored, vs. synthesized by the no-on-clause
    // constructor purely so match-time code always has an OnPattern to invoke. Only an authored clause is
    // re-emitted by toFormattedString() -- otherwise recompiling persisted PML text for a bare
    // `matches "opName"` (no on-clause) would spuriously require opName to resolve to a real operation at
    // compile time (needed only to type-check an on-clause's event params), when the original text never
    // demanded that.
    private final boolean explicitOnPattern;

    public MatchesOperationPattern(String opName, OnPattern onPattern) {
        this.opName = opName;
        this.onPattern = onPattern;
        this.explicitOnPattern = true;
    }

    public MatchesOperationPattern(String opName, Set<String> argNames, PMLStmtsRoutine<Boolean> func) {
        this.opName = opName;
        this.onPattern = new OnPattern(argNames, func);
        this.explicitOnPattern = true;
    }

    public MatchesOperationPattern(String opName) {
        this.opName = opName;
        this.onPattern = new OnPattern(
            new HashSet<>(),
            new PMLStmtsRoutine<>("", BasicTypes.BOOLEAN_TYPE, List.of(), new PMLStatementBlock(List.of(
                new ReturnStatement(new BoolLiteralExpression(true))
            ))));
        this.explicitOnPattern = false;
    }

    public String getOpName() {
        return opName;
    }

    public OnPattern getOnPattern() {
        return onPattern;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        if (!explicitOnPattern) {
            return String.format("\"%s\"", opName);
        }

        return String.format("\"%s\"%s", opName, onPattern.toFormattedString(indentLevel));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatchesOperationPattern that)) {
            return false;
        }
        return Objects.equals(opName, that.opName) && Objects.equals(onPattern, that.onPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(opName, onPattern);
    }
}
