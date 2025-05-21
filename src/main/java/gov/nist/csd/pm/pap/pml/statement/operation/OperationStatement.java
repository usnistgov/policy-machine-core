package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.result.VoidResult;

public abstract class OperationStatement<A extends Args> extends PMLStatement<VoidResult> {

    protected Operation<?, A> op;

    public OperationStatement(Operation<?, A> op) {
        this.op = op;
    }

    public Operation<?, A> getOp() {
        return op;
    }

    public abstract A prepareArgs(ExecutionContext ctx, PAP pap) throws PMException;

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        op.execute(pap, prepareArgs(ctx, pap));

        return new VoidResult();
    }
}
