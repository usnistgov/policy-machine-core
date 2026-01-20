package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.operation.CreateOperationOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.core.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;
import java.util.Objects;

public class RoutineDefinitionStatement extends OperationStatement implements FunctionDefinitionStatement {

    protected PMLStmtsRoutine<?> pmlStmtsRoutine;

    public RoutineDefinitionStatement(PMLStmtsRoutine<?> pmlStmtsRoutine) {
        super(new CreateOperationOp());

        this.pmlStmtsRoutine = pmlStmtsRoutine;
    }

    @Override
    public PMLOperationSignature getSignature() {
        return pmlStmtsRoutine.getSignature();
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        return new Args()
            .put(CreateOperationOp.OPERATION_PARAM, pmlStmtsRoutine);
    }

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        VoidResult value = super.execute(ctx, pap);

        ctx.scope().addOperation(pmlStmtsRoutine.getName(), pmlStmtsRoutine);

        return value;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return pmlStmtsRoutine.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RoutineDefinitionStatement that))
            return false;
        return Objects.equals(pmlStmtsRoutine, that.pmlStmtsRoutine);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pmlStmtsRoutine);
    }
}
