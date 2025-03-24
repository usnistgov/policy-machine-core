package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.op.graph.CreatePolicyClassOp;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;

import java.util.Map;
import java.util.Objects;

import static gov.nist.csd.pm.pap.op.Operation.NAME_OPERAND;

public class CreatePolicyStatement extends OperationStatement<Long> {

    private final Expression name;

    public CreatePolicyStatement(Expression name) {
        super(new CreatePolicyClassOp());
        
        this.name = name;
    }

    @Override
    public Map<String, Object> prepareOperands(ExecutionContext ctx, PAP pap)
            throws PMException {
        String pcName = name.execute(ctx, pap).getStringValue();

        return Map.of(NAME_OPERAND, pcName);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("create PC %s", name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreatePolicyStatement that)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
