package gov.nist.ngac.pm.core.pap.pml.statement;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;

/**
 * Base class for every compiled PML statement.
 *
 * @param <T> the type this statement evaluates or executes to
 */
public abstract class PMLStatement<T> implements PMLStatementSerializable {

    /**
     * Executes this statement.
     *
     * @param ctx the execution context to run against
     * @param pap the PAP to run against
     * @return this statement's result
     * @throws PMException if execution fails
     */
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
