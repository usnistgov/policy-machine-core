package gov.nist.csd.pm.core.pap.pml.statement.result;

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
