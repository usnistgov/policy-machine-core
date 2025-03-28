package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.function.op.graph.DissociateOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.query.GraphQuery;

import java.util.Objects;

public class DissociateStatement extends OperationStatement<DissociateOp> {

    private final Expression uaExpr;
    private final Expression targetExpr;

    public DissociateStatement(Expression uaExpr, Expression targetExpr) {
        super(new DissociateOp());
        this.uaExpr = uaExpr;
        this.targetExpr = targetExpr;
    }

    @Override
    public ActualArgs prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        String ua = uaExpr.execute(ctx, pap).getStringValue();
        String target = targetExpr.execute(ctx, pap).getStringValue();

        GraphQuery graph = pap.query().graph();
        long uaId = graph.getNodeByName(ua).getId();
        long targetId = graph.getNodeByName(target).getId();

        return op.actualArgs(uaId, targetId);
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