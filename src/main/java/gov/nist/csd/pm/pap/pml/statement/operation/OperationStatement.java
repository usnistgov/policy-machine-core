package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.PreparedOperation;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.value.VoidValue;

import java.util.*;

public abstract class OperationStatement extends PreparedOperation<Void> implements PMLStatement {

    public OperationStatement(Operation<Void> op) {
        super(op, new HashMap<>());
    }

    public abstract Map<String, Object> prepareOperands(ExecutionContext ctx, PAP pap) throws PMException;

    @Override
    public final VoidValue execute(ExecutionContext ctx, PAP pap) throws PMException {
        Map<String, Object> prepareOperands = prepareOperands(ctx, pap);
        setOperands(prepareOperands);

        execute(pap);

        return new VoidValue();
    }

    @Override
    public final Void execute(PAP pap) throws PMException  {
        return super.execute(pap);
    }

    @Override
    public String toString() {
        return toFormattedString(0);
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
