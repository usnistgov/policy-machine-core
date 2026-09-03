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

package gov.nist.ngac.pm.core.pap.obligation.event;

import gov.nist.ngac.pm.core.pap.obligation.event.operation.OperationPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.SubjectPattern;
import java.io.Serializable;
import java.util.Objects;

/**
 * The event pattern of an obligation that holds a subject pattern and operation pattern.
 */
public class EventPattern implements Serializable {

    protected SubjectPattern subjectPattern;
    protected OperationPattern operationPattern;

    public EventPattern(SubjectPattern subjectPattern,
                        OperationPattern operationPattern) {
        this.subjectPattern = subjectPattern;
        this.operationPattern = operationPattern;
    }

    public SubjectPattern getSubjectPattern() {
        return subjectPattern;
    }

    public void setSubjectPattern(SubjectPattern subjectPattern) {
        this.subjectPattern = subjectPattern;
    }

    public OperationPattern getOperationPattern() {
        return operationPattern;
    }

    public void setOperationPattern(OperationPattern operationPattern) {
        this.operationPattern = operationPattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventPattern that)) return false;

        return Objects.equals(subjectPattern, that.subjectPattern) && Objects.equals(operationPattern, that.operationPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectPattern, operationPattern);
    }
}
