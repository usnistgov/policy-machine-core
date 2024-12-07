package gov.nist.csd.pm.common.op.graph;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_USER_ATTRIBUTE;
import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_USER_ATTRIBUTE_FROM;

public class DeleteUserAttributeOp extends DeleteNodeOp{
    public DeleteUserAttributeOp() {
        super("delete_user_attribute", DELETE_USER_ATTRIBUTE, DELETE_USER_ATTRIBUTE_FROM);
    }
}
