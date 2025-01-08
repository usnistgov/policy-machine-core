package gov.nist.csd.pm.common.op.graph;

import static gov.nist.csd.pm.pap.AdminAccessRights.*;

public class DeleteObjectOp extends DeleteNodeOp {
    public DeleteObjectOp() {
        super("delete_object", DELETE_OBJECT, DELETE_OBJECT_FROM);
    }
}
