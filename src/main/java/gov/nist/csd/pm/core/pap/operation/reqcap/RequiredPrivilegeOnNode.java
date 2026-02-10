package gov.nist.csd.pm.core.pap.operation.reqcap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.Objects;

public final class RequiredPrivilegeOnNode extends RequiredPrivilege{

    private final String name;

    public RequiredPrivilegeOnNode(String name, AccessRightSet required) {
        super(required);
        this.name = name;
    }

    public RequiredPrivilegeOnNode(String name, AdminAccessRight required) {
        super(new AccessRightSet(required.toString()));
        this.name = name;
    }

    @Override
    public boolean isSatisfied(PAP pap, UserContext userCtx, Args args) throws PMException {
        return pap.query()
            .access()
            .computePrivileges(userCtx, new TargetContext(pap.query().graph().getNodeId(name)))
            .containsAll(getRequired());
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequiredPrivilegeOnNode that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
