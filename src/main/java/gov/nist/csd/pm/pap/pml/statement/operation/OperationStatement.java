package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;

public abstract class OperationStatement<T extends Operation<?>> extends PMLStatement {

    protected T op;

    public OperationStatement(T op) {
        this.op = op;
    }

    public T getOp() {
        return op;
    }

    public abstract Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException;

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        op.execute(pap, prepareArgs(ctx, pap));

        return new VoidValue();
    }
}
