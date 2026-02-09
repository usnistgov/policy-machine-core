package gov.nist.csd.pm.core.pap.operation.reqcap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.io.Serializable;
import java.util.Objects;

public abstract sealed class RequiredPrivilege implements Serializable permits RequiredPrivilegeOnNode, RequiredPrivilegeOnParameter {

    private final AccessRightSet required;

    public RequiredPrivilege(AccessRightSet required) {
        this.required = required;
    }

    public abstract boolean isSatisfied(PAP pap, UserContext userCtx, Args args) throws PMException;

    public AccessRightSet getRequired() {
        return required;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequiredPrivilege that)) {
            return false;
        }
        return Objects.equals(required, that.required);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(required);
    }
}
