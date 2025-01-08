package gov.nist.csd.pm.common.op.graph;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_POLICY_CLASS;
import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_POLICY_CLASS_FROM;

public class DeletePolicyClassOp extends DeleteNodeOp {

    public DeletePolicyClassOp() {
        super("delete_policy_class", DELETE_POLICY_CLASS, DELETE_POLICY_CLASS_FROM);
    }
}
