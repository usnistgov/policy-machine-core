package gov.nist.csd.pm.pap.op.graph;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.*;

public class DeleteObjectAttributeOp extends DeleteNodeOp{
    public DeleteObjectAttributeOp() {
        super("delete_object_attribute", DELETE_OBJECT_ATTRIBUTE, DELETE_OBJECT_ATTRIBUTE_FROM);
    }
}
