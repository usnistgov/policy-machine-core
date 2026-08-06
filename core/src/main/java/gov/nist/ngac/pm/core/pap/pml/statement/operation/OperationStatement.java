package gov.nist.ngac.pm.core.pap.pml.statement.operation;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.result.VoidResult;

/**
 * Base class for PML statements that execute a single {@link Operation}.
 */
public abstract class OperationStatement extends PMLStatement<VoidResult> {

    protected Operation<?> op;

    public OperationStatement(Operation<?> op) {
        this.op = op;
    }

    public Operation<?> getOp() {
        return op;
    }

    /**
     * Builds the arguments to invoke this statement's operation with.
     *
     * @param ctx the execution context to resolve expressions against
     * @param pap the PAP to resolve names against
     * @return the arguments to invoke the operation with
     * @throws PMException if resolving an expression or name fails
     */
    public abstract Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException;

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        op.execute(pap, ctx.author(), prepareArgs(ctx, pap));

        return new VoidResult();
    }
}
