package gov.nist.csd.pm.common.op.graph;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_USER;
import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_USER_FROM;

public class DeleteUserOp extends DeleteNodeOp{
    public DeleteUserOp() {
        super("delete_user", DELETE_USER, DELETE_USER_FROM);
    }
}
