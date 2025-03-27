package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.Operation;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;

public abstract class OperationStatement<T extends Operation<?>> implements PMLStatement {

    protected T op;

    public OperationStatement(T op) {
        this.op = op;
    }

    public T getOp() {
        return op;
    }

    public abstract ActualArgs prepareOperands(ExecutionContext ctx, PAP pap) throws PMException;

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        op.execute(pap, prepareOperands(ctx, pap));

        return new VoidValue();
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object o);
}
