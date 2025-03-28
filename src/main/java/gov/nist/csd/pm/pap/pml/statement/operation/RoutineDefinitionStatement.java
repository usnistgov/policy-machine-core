package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.function.op.routine.CreateAdminRoutineOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.pap.pml.value.Value;
import java.util.Objects;

public class RoutineDefinitionStatement extends OperationStatement<CreateAdminRoutineOp> implements
    FunctionDefinitionStatement {

    protected PMLStmtsRoutine pmlStmtsRoutine;

    public RoutineDefinitionStatement(PMLStmtsRoutine pmlStmtsRoutine) {
        super(new CreateAdminRoutineOp());

        this.pmlStmtsRoutine = pmlStmtsRoutine;
    }

    @Override
    public PMLFunctionSignature getSignature() {
        return pmlStmtsRoutine.getSignature();
    }

    @Override
    public ActualArgs prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        return op.actualArgs(pmlStmtsRoutine);
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        Value value = super.execute(ctx, pap);

        ctx.scope().addFunction(pmlStmtsRoutine.getName(), pmlStmtsRoutine);

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
