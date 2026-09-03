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

package gov.nist.ngac.pm.core.pap.obligation.event.subject;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.obligation.event.EventContextUser;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import java.util.Objects;

/**
 * Logical pattern expression evaluating with AND or OR of the left and right expressions.
 */
public class LogicalSubjectPatternExpression extends SubjectPatternExpression {

    private final SubjectPatternExpression left;
    private final SubjectPatternExpression right;
    private final boolean isAnd;

    public LogicalSubjectPatternExpression(SubjectPatternExpression left, SubjectPatternExpression right, boolean isAnd) {
        this.left = left;
        this.right = right;
        this.isAnd = isAnd;
    }

    public SubjectPattern getLeft() {
        return left;
    }

    public SubjectPattern getRight() {
        return right;
    }

    public boolean isAnd() {
        return isAnd;
    }

    @Override
    public boolean matches(EventContextUser user, ExecutionContext ctx, PAP pap) throws PMException {
        return isAnd ? left.matches(user, ctx, pap) && right.matches(user, ctx, pap)
                : left.matches(user, ctx,  pap) || right.matches(user, ctx, pap);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return left.toFormattedString(indentLevel) +
                (isAnd ? " && " : " || ") +
                right.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogicalSubjectPatternExpression that)) return false;
        return isAnd == that.isAnd && Objects.equals(left, that.left) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, isAnd);
    }
}
