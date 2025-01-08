package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.op.graph.DissociateOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;

import java.util.Map;
import java.util.Objects;

import static gov.nist.csd.pm.common.op.graph.GraphOp.TARGET_OPERAND;
import static gov.nist.csd.pm.common.op.graph.GraphOp.UA_OPERAND;


public class DissociateStatement extends OperationStatement {

    private Expression uaExpr;
    private Expression targetExpr;

    public DissociateStatement(Expression uaExpr, Expression targetExpr) {
        super(new DissociateOp());
        this.uaExpr = uaExpr;
        this.targetExpr = targetExpr;
    }

    @Override
    public Map<String, Object> prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        String ua = uaExpr.execute(ctx, pap).getStringValue();
        String target = targetExpr.execute(ctx, pap).getStringValue();

        return Map.of(UA_OPERAND, ua, TARGET_OPERAND, target);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("dissociate %s and %s", uaExpr, targetExpr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DissociateStatement that)) return false;
        return Objects.equals(uaExpr, that.uaExpr) && Objects.equals(targetExpr, that.targetExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uaExpr, targetExpr);
    }
}
