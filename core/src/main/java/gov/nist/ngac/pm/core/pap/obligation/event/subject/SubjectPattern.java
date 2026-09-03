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
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementSerializable;
import java.io.Serializable;
import java.util.Objects;

/**
 * The subject part of an event pattern, matching either any user or a specific subject pattern expression.
 */
public class SubjectPattern implements Serializable, PMLStatementSerializable {

    private final boolean isAny;
    private final SubjectPatternExpression subjectPatternExpression;

    public SubjectPattern() {
        this.isAny = true;
        this.subjectPatternExpression = null;
    }

    public SubjectPattern(SubjectPatternExpression subjectPatternExpression) {
        this.isAny = false;
        this.subjectPatternExpression = subjectPatternExpression;
    }

    public boolean isAny() {
        return isAny;
    }

    public SubjectPatternExpression getSubjectPatternExpression() {
        return subjectPatternExpression;
    }

    /**
     * Returns true if the given value matches this pattern. If the value is null, then return false.
     *
     * @param user the user.
     * @param ctx the event context information.
     * @param pap  The PolicyQuery object to get policy information relevant to the value and pattern.
     * @return True if the value matches this pattern.
     */
    public boolean matches(EventContextUser user, ExecutionContext ctx, PAP pap) throws PMException {
        if (user == null) {
            return false;
        }

        return matchesInternal(user, ctx, pap);
    }

    /**
     * The match logic behind {@link #matches}, run once the user is already confirmed non-null.
     */
    public boolean matchesInternal(EventContextUser user, ExecutionContext ctx, PAP pap) throws PMException {
        return isAny || subjectPatternExpression.matches(user, ctx, pap);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return isAny ? "any user" : subjectPatternExpression.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubjectPattern that)) return false;
        return isAny == that.isAny && Objects.equals(subjectPatternExpression, that.subjectPatternExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isAny, subjectPatternExpression);
    }
}
