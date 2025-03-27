package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.graph.CreatePolicyClassOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import java.util.Objects;

public class CreatePolicyClassStatement extends OperationStatement<CreatePolicyClassOp> {

    private final Expression name;

    public CreatePolicyClassStatement(Expression name) {
        super(new CreatePolicyClassOp());

        this.name = name;
    }

    @Override
    public ActualArgs prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        String pcName = name.execute(ctx, pap).getStringValue();

        return op.actualArgs(pcName);
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        ActualArgs actualArgs = prepareOperands(ctx, pap);
        op.execute(pap, actualArgs);
        return new VoidValue();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("create PC %s", name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CreatePolicyClassStatement that))
            return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
