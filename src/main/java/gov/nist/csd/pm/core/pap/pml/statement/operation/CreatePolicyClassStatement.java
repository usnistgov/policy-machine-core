package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.graph.CreateNodeOp.CreateNodeOpArgs;
import gov.nist.csd.pm.core.pap.function.op.graph.CreatePolicyClassOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import java.util.List;
import java.util.Objects;

public class CreatePolicyClassStatement extends OperationStatement<CreateNodeOpArgs> {

    private final Expression<String> name;

    public CreatePolicyClassStatement(Expression<String> name) {
        super(new CreatePolicyClassOp());

        this.name = name;
    }

    @Override
    public CreateNodeOpArgs prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String pcName = name.execute(ctx, pap);

        return new CreateNodeOpArgs(pcName, List.of());
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
