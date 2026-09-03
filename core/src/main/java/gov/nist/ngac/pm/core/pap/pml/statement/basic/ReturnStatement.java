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

package gov.nist.ngac.pm.core.pap.pml.statement.basic;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.arg.type.VoidType;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.result.ReturnResult;
import gov.nist.ngac.pm.core.pap.pml.statement.result.StatementResult;
import java.util.Objects;


/**
 * PML return statement, with or without a return value.
 */
public class ReturnStatement extends BasicStatement<StatementResult> {

    private Expression<?> expr;

    public ReturnStatement() {
    }

    public ReturnStatement(Expression<?> expr) {
        this.expr = expr;
    }

    public Expression<?> getExpr() {
        return expr;
    }

    /**
     * Checks whether this statement's return type matches the given expected type.
     */
    public boolean matchesReturnType(Type<?> match) {
        if (expr == null) {
            return match.equals(new VoidType());
        }

        return expr.getType().isCastableTo(match);
    }

    @Override
    public StatementResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        if (expr == null) {
            return new ReturnResult(null);
        }

        return new ReturnResult(expr.execute(ctx, pap));
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("%sreturn%s", indent(indentLevel), (expr == null ? "" : String.format(" %s", expr)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReturnStatement that = (ReturnStatement) o;
        return Objects.equals(expr, that.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expr);
    }
}
