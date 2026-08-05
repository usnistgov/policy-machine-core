package gov.nist.ngac.pm.core.pap.pml.statement;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;

public abstract class PMLStatement<T> implements PMLStatementSerializable {

    public abstract T execute(ExecutionContext ctx, PAP pap) throws PMException;

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public final String toString() {
        return toFormattedString(0);
    }

}
