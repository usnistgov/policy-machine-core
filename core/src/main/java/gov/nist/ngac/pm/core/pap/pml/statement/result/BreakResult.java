package gov.nist.ngac.pm.core.pap.pml.statement.result;

/**
 * Signals that a {@link gov.nist.ngac.pm.core.pap.pml.statement.basic.BreakStatement} was executed,
 * telling the enclosing loop to stop iterating.
 */
public class BreakResult extends StatementResult{

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BreakResult;
    }

    @Override
    public int hashCode() {
        return "break".hashCode();
    }
}
