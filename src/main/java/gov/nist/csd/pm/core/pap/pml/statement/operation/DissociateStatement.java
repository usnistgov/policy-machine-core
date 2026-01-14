package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.graph.DissociateOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.query.GraphQuery;

import java.util.Objects;

public class DissociateStatement extends OperationStatement {

    private final Expression<String> uaExpr;
    private final Expression<String> targetExpr;

    public DissociateStatement(Expression<String> uaExpr, Expression<String> targetExpr) {
        super(new DissociateOp());
        this.uaExpr = uaExpr;
        this.targetExpr = targetExpr;
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String ua = uaExpr.execute(ctx, pap);
        String target = targetExpr.execute(ctx, pap);

        GraphQuery graph = pap.query().graph();
        long uaId = graph.getNodeByName(ua).getId();
        long targetId = graph.getNodeByName(target).getId();

        return new Args()
            .put(DissociateOp.DISSOCIATE_UA_PARAM, uaId)
            .put(DissociateOp.DISSOCIATE_TARGET_PARAM, targetId);
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