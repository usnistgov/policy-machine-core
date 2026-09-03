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

package gov.nist.ngac.pm.core.common.prohibition;

import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link Prohibition} on a process.
 */
public final class ProcessProhibition extends Prohibition {

    private long userId;
    private String process;

    public ProcessProhibition(String name,
                              long userId,
                              String process,
                              AccessRightSet accessRightSet,
                              Set<Long> inclusionSet,
                              Set<Long> exclusionSet,
                              boolean isConjunctive) {
        super(name, accessRightSet, inclusionSet, exclusionSet, isConjunctive);
        this.userId = userId;
        this.process = process;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ProcessProhibition that = (ProcessProhibition) o;
        return userId == that.userId && Objects.equals(process, that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, process);
    }
}
