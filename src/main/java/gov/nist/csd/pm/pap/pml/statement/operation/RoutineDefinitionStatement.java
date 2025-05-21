package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.pap.function.op.routine.CreateAdminRoutineOp.CreateAdminRoutineOpArgs;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.routine.CreateAdminRoutineOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;

import gov.nist.csd.pm.pap.pml.function.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.pap.pml.statement.result.VoidResult;
import java.util.Objects;

public class RoutineDefinitionStatement extends OperationStatement<CreateAdminRoutineOpArgs> implements FunctionDefinitionStatement {

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
    public CreateAdminRoutineOpArgs prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        return new CreateAdminRoutineOpArgs(pmlStmtsRoutine);
    }

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        VoidResult value = super.execute(ctx, pap);

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
