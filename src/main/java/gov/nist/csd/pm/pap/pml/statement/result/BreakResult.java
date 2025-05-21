package gov.nist.csd.pm.pap.pml.statement.result;

import java.util.Objects;

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
