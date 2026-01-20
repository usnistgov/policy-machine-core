package gov.nist.csd.pm.core.pap.obligation.event.operation;

import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLStmtsQueryOperation;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ReturnStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class MatchesOperationPattern extends OperationPattern {

    private final String opName;
    private final OnPattern onPattern;

    public MatchesOperationPattern(String opName, OnPattern onPattern) {
        this.opName = opName;
        this.onPattern = onPattern;
    }

    public MatchesOperationPattern(String opName, Set<String> argNames, PMLStmtsQueryOperation<Boolean> func) {
        this.opName = opName;
        this.onPattern = new OnPattern(argNames, func);
    }

    public MatchesOperationPattern(String opName) {
        this.opName = opName;
        this.onPattern = new OnPattern(
            new HashSet<>(),
            new PMLStmtsQueryOperation<>("", BasicTypes.BOOLEAN_TYPE, List.of(), new PMLStatementBlock(List.of(
                new ReturnStatement(new BoolLiteralExpression(true))
            ))));
    }

    public String getOpName() {
        return opName;
    }

    public OnPattern getOnPattern() {
        return onPattern;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("%s%s", opName, onPattern.toFormattedString(indentLevel));
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
