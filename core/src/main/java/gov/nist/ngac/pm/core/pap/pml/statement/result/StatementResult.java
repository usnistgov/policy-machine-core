package gov.nist.ngac.pm.core.pap.pml.statement.result;

/**
 * The result of executing a PML statement or block, used by enclosing blocks to decide whether to
 * keep executing, stop a loop iteration, or propagate a return.
 */
public abstract class StatementResult {

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
}
