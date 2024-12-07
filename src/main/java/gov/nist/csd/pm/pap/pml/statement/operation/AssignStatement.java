package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.op.graph.AssignOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static gov.nist.csd.pm.common.op.graph.AssignOp.ASCENDANT_OPERAND;
import static gov.nist.csd.pm.common.op.graph.GraphOp.DESCENDANTS_OPERAND;


public class AssignStatement extends OperationStatement {

    private Expression ascendant;
    private Expression descendants;

    public AssignStatement(Expression ascendant, Expression descendants) {
        super(new AssignOp());

        this.ascendant = ascendant;
        this.descendants = descendants;
    }

    @Override
    public Map<String, Object> prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        String asc = ascendant.execute(ctx, pap).getStringValue();
        List<Value> assignToValue = descendants.execute(ctx, pap).getArrayValue();
        List<String> descs = new ArrayList<>();
        for (Value value : assignToValue) {
            descs.add(value.getStringValue());
        }

        return Map.of(ASCENDANT_OPERAND, asc, DESCENDANTS_OPERAND, descs);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("assign %s to %s", ascendant, descendants);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssignStatement that)) return false;
        return Objects.equals(ascendant, that.ascendant) && Objects.equals(descendants, that.descendants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ascendant, descendants);
    }
}
