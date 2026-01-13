package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;
import java.util.List;
import java.util.Objects;

public class CheckStatement extends PMLStatement<VoidResult> {
    private final Expression<List<String>> arsExpr;
    private final Expression<List<String>> targetExpr;

    public CheckStatement(Expression<List<String>> arsExpr, Expression<List<String>> targetExpr) {
        this.arsExpr = arsExpr;
        this.targetExpr = targetExpr;
    }

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        List<String> ars = arsExpr.execute(ctx, pap);
        List<String> targets = targetExpr.execute(ctx, pap);

        for (String target : targets) {
            long id = pap.query().graph().getNodeByName(target).getId();
            pap.privilegeChecker().check(ctx.author(), id, ars);
        }

        return new VoidResult();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) +
            "check " + arsExpr.toFormattedString(0) +
            " on " + targetExpr.toFormattedString(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckStatement that)) return false;
        return Objects.equals(arsExpr, that.arsExpr) && Objects.equals(targetExpr, that.targetExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arsExpr, targetExpr);
    }
}
