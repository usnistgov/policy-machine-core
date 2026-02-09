package gov.nist.csd.pm.core.common.prohibition;

import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Objects;
import java.util.Set;

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
