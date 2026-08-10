package gov.nist.ngac.pm.core.pap.pml.statement.result;

/**
 * The result of a statement that neither breaks, continues, nor returns.
 */
public class VoidResult extends StatementResult {

    @Override
    public boolean equals(Object obj) {
        return obj instanceof VoidResult;
    }

    @Override
    public int hashCode() {
        return "void".hashCode();
    }
}
