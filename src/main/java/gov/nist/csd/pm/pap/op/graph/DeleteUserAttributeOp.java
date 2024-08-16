package gov.nist.csd.pm.pap.op.graph;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.DELETE_USER_ATTRIBUTE;
import static gov.nist.csd.pm.pap.op.AdminAccessRights.DELETE_USER_ATTRIBUTE_FROM;

public class DeleteUserAttributeOp extends DeleteNodeOp{
    public DeleteUserAttributeOp() {
        super("delete_user_attribute", DELETE_USER_ATTRIBUTE, DELETE_USER_ATTRIBUTE_FROM);
    }
}
