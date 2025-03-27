package gov.nist.csd.pm.pap.executable.op.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.arg.FormalArg;
import gov.nist.csd.pm.pap.executable.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.SET_RESOURCE_OPERATIONS;
import static gov.nist.csd.pm.pap.executable.op.graph.GraphOp.ARSET_ARG;

public class SetResourceOperationsOp extends Operation<Void> {

    public static final String OPERATIONS_OPERAND = "operations";

    public SetResourceOperationsOp() {
        super(
                "set_resource_operations",
                List.of(ARSET_ARG)
        );
    }

    public ActualArgs actualArgs(AccessRightSet accessRightSet) {
        ActualArgs actualArgs = new ActualArgs();
        actualArgs.put(ARSET_ARG, accessRightSet);
        return actualArgs;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs actualArgs) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), SET_RESOURCE_OPERATIONS);
    }

    @Override
    public Void execute(PAP pap, ActualArgs actualArgs) throws PMException {
        pap.modify().operations().setResourceOperations(actualArgs.get(ARSET_ARG));

        return null;
    }
}
