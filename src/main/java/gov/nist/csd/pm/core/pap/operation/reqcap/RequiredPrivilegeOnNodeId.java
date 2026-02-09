package gov.nist.csd.pm.core.pap.operation.reqcap;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.Objects;

public class RequiredPrivilegeOnNodeId implements RequiredPrivilege {

    private final long nodeId;
    private final AccessRightSet required;

    public RequiredPrivilegeOnNodeId(long nodeId, AccessRightSet required) {
        this.nodeId = nodeId;
        this.required = required;
    }

    public RequiredPrivilegeOnNodeId(long nodeId, AdminAccessRight adminAccessRight) {
        this(nodeId, new AccessRightSet(adminAccessRight));
    }

    @Override
    public boolean isSatisfied(PAP pap, UserContext userCtx, Args args) throws PMException {
        return pap.query()
            .access()
            .computePrivileges(userCtx, new TargetContext(nodeId))
            .containsAll(required);
    }

    public long nodeId() {
        return nodeId;
    }

    public AccessRightSet required() {
        return required;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (RequiredPrivilegeOnNodeId) obj;
        return this.nodeId == that.nodeId &&
            Objects.equals(this.required, that.required);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, required);
    }

    @Override
    public String toString() {
        return "RequiredPrivilegeOnNode[" +
            "nodeId=" + nodeId + ", " +
            "required=" + required + ']';
    }

}
