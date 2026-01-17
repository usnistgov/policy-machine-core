package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.Operation;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;

public abstract class OperationStatement extends PMLStatement<VoidResult> {

    protected Operation<?> op;

    public OperationStatement(Operation<?> op) {
        this.op = op;
    }

    public Operation<?> getOp() {
        return op;
    }

    public abstract Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException;

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
