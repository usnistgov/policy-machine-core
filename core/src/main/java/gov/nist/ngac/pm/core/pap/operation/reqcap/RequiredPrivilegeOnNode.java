package gov.nist.ngac.pm.core.pap.operation.reqcap;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.Objects;

/**
 * A {@link RequiredPrivilege} satisfied when the acting user holds the required access rights on a
 * node.
 */
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
        AccessRightSet arset = pap.query()
            .access()
            .computePrivileges(userCtx, NodeTargetContext.of(pap.query().graph().getNodeId(name)));
        return !arset.isEmpty() && arset.containsAll(getRequired());
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
