package gov.nist.ngac.pm.core.pap.pml.statement.result;

/**
 * Signals that a {@link gov.nist.ngac.pm.core.pap.pml.statement.basic.ContinueStatement} was executed,
 * telling the enclosing loop to skip to its next iteration.
 */
public class ContinueResult extends StatementResult {

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ContinueResult;
    }

    @Override
    public int hashCode() {
        return "continue".hashCode();
    }
}
