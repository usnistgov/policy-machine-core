package gov.nist.ngac.pm.core.pap.operation.graph;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredPrivilegeOnNode;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;

/**
 * Admin operation "create_policy_class": creates a new policy class. Unlike other {@link CreateNodeOp}s,
 * it takes only a name parameter and overrides {@link #execute} to skip reading a descendants argument,
 * since a policy class has none.
 */
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
    public Long execute(PAP pap, UserContext userCtx, Args args) throws PMException {
        String name = args.get(NAME_PARAM);
        return createNode(pap, name, List.of());
    }
}
