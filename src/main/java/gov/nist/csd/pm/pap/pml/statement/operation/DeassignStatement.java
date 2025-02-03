package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.op.graph.DeassignOp;
import gov.nist.csd.pm.common.op.graph.GraphOp;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class DeassignStatement extends OperationStatement {

    private final Expression ascendant;
    private final Expression deassignFrom;

    public DeassignStatement(Expression ascendant, Expression deassignFrom) {
        super(new DeassignOp());
        this.ascendant = ascendant;
        this.deassignFrom = deassignFrom;
    }

    @Override
    public Map<String, Object> prepareOperands(ExecutionContext ctx, PAP pap)
            throws PMException {
        String asc = ascendant.execute(ctx, pap).getStringValue();
        List<Value> deassignFromValue = deassignFrom.execute(ctx, pap).getArrayValue();
        List<String> descs = new ArrayList<>();
        for (Value value : deassignFromValue) {
            descs.add(value.getStringValue());
        }

        long ascId = pap.query().graph().getNodeId(asc);
        List<Long> descIds = new ArrayList<>();
        for (String desc : descs) {
            descIds.add(pap.query().graph().getNodeId(desc));
        }

        return Map.of(GraphOp.ASCENDANT_OPERAND, ascId, GraphOp.DESCENDANTS_OPERAND, descIds);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("deassign %s from %s", ascendant, deassignFrom);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeassignStatement that)) return false;
        return Objects.equals(ascendant, that.ascendant) && Objects.equals(deassignFrom, that.deassignFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ascendant, deassignFrom);
    }
}
