package gov.nist.csd.pm.core.pap.pml.statement.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightValidator;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.result.VoidResult;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import java.util.List;
import java.util.Objects;

public class RequireStatement extends PMLStatement<VoidResult> {
    private final Expression<List<String>> arsExpr;
    private final Expression<List<String>> targetExpr;

    public RequireStatement(Expression<List<String>> arsExpr, Expression<List<String>> targetExpr) {
        this.arsExpr = arsExpr;
        this.targetExpr = targetExpr;
    }

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        List<String> ars = arsExpr.execute(ctx, pap);

        // check access rights are valid
        AccessRightValidator.validateAccessRights(pap.query().operations().getResourceAccessRights(), ars);

        List<String> targets = targetExpr.execute(ctx, pap);

        for (String target : targets) {
            long id = pap.query().graph().getNodeByName(target).getId();
            TargetContext targetCtx = new TargetContext(id);
            AccessRightSet privs = pap.query().access().computePrivileges(ctx.author(), targetCtx);
            if (!privs.containsAll(ars) || (privs.isEmpty() && ars.isEmpty())) {
                throw UnauthorizedException.of(pap.query().graph(), ctx.author(), targetCtx, privs, ars);
            }
        }

        return new VoidResult();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) +
            "require " + arsExpr.toFormattedString(0) +
            " on " + targetExpr.toFormattedString(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequireStatement that)) return false;
        return Objects.equals(arsExpr, that.arsExpr) && Objects.equals(targetExpr, that.targetExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arsExpr, targetExpr);
    }
}
