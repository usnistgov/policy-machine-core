package gov.nist.csd.pm.pap.pml.statement.result;

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
