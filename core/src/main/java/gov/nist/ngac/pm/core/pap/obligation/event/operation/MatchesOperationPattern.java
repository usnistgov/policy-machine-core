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
