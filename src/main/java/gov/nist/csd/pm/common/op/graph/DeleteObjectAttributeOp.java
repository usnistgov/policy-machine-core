package gov.nist.csd.pm.common.op.graph;

import static gov.nist.csd.pm.pap.AdminAccessRights.*;

public class DeleteObjectAttributeOp extends DeleteNodeOp{
    public DeleteObjectAttributeOp() {
        super("delete_object_attribute", DELETE_OBJECT_ATTRIBUTE, DELETE_OBJECT_ATTRIBUTE_FROM);
    }
}
