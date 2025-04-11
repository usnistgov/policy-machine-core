package gov.nist.csd.pm.pap.pml.statement.result;

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
