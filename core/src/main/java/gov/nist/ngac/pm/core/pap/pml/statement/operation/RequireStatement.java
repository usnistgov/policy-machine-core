/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.statement.operation;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightValidator;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.result.VoidResult;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
import java.util.List;
import java.util.Objects;

/**
 * A PML statement that throws {@link UnauthorizedException} unless the executing user holds every
 * listed access right on every listed target.
 */
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
            TargetContext targetCtx = NodeTargetContext.of(id);
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
