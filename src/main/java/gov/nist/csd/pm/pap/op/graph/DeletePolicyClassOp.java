package gov.nist.csd.pm.pap.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.Collection;
import java.util.List;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.DELETE_POLICY_CLASS;
import static gov.nist.csd.pm.pap.op.AdminAccessRights.DELETE_POLICY_CLASS_FROM;

public class DeletePolicyClassOp extends DeleteNodeOp {

    public DeletePolicyClassOp() {
        super("delete_policy_class", DELETE_POLICY_CLASS, DELETE_POLICY_CLASS_FROM);
    }
}
