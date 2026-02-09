package gov.nist.csd.pm.core.pap.operation.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnNode;
import java.util.List;

public class CreatePolicyClassOp extends CreateNodeOp {

    public CreatePolicyClassOp() {
        super(
            "create_policy_class",
            List.of(NAME_PARAM),
            new RequiredCapability(
                new RequiredPrivilegeOnNode(
                    AdminPolicyNode.PM_ADMIN_POLICY_CLASSES.nodeName(), AdminAccessRight.ADMIN_GRAPH_NODE_CREATE
                )
            )
        );
    }

    @Override
    protected long createNode(PAP pap, String name, List<Long> descs) throws PMException {
        return pap.modify().graph().createPolicyClass(name);
    }

    @Override
    public Long execute(PAP pap, Args args) throws PMException {
        String name = args.get(NAME_PARAM);
        return createNode(pap, name, List.of());
    }
}
