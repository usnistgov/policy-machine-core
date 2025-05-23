package gov.nist.csd.pm.core.pap.function.op.operation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.SET_RESOURCE_OPERATIONS;
import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.ARSET_PARAM;

public class SetResourceOperationsOp extends Operation<Void, SetResourceOperationsOp.SetResourceOperationsOpArgs> {

    public SetResourceOperationsOp() {
        super(
                "set_resource_operations",
                List.of(ARSET_PARAM)
        );
    }

    public static class SetResourceOperationsOpArgs extends Args {
        private final AccessRightSet accessRightSet;

        public SetResourceOperationsOpArgs(AccessRightSet accessRightSet) {
            super(Map.of(
                ARSET_PARAM, accessRightSet
            ));

            this.accessRightSet = accessRightSet;
        }

        public AccessRightSet getAccessRightSet() {
            return accessRightSet;
        }
    }

    @Override
    public SetResourceOperationsOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        AccessRightSet arset = new AccessRightSet(prepareArg(ARSET_PARAM, argsMap));
        return new SetResourceOperationsOpArgs(arset);
    }

    @Override
    public void canExecute(PAP pap,
                           UserContext userCtx, SetResourceOperationsOpArgs args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), SET_RESOURCE_OPERATIONS);
    }

    @Override
    public Void execute(PAP pap, SetResourceOperationsOpArgs args) throws PMException {
        pap.modify().operations().setResourceOperations(args.getAccessRightSet());
        return null;
    }
}
