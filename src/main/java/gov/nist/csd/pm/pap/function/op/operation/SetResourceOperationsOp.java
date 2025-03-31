package gov.nist.csd.pm.pap.function.op.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.SET_RESOURCE_OPERATIONS;
import static gov.nist.csd.pm.pap.function.op.graph.GraphOp.ARSET_ARG;

public class SetResourceOperationsOp extends Operation<Void> {

    public SetResourceOperationsOp() {
        super(
                "set_resource_operations",
                List.of(ARSET_ARG)
        );
    }

    public Args actualArgs(AccessRightSet accessRightSet) {
        Args args = new Args();
        args.put(ARSET_ARG, accessRightSet);
        return args;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args args) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), SET_RESOURCE_OPERATIONS);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().operations().setResourceOperations(new AccessRightSet(args.get(ARSET_ARG)));

        return null;
    }
}
