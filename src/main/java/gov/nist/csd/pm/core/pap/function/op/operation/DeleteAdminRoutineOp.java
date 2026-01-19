package gov.nist.csd.pm.core.pap.function.op.operation;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_ADMIN_ROUTINE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class DeleteAdminRoutineOp extends AdminOperation<Void> {

    public DeleteAdminRoutineOp() {
        super(
            "delete_admin_routine",
            BasicTypes.VOID_TYPE,
            List.of(NAME_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().operations().deleteAdminRoutine(args.get(NAME_PARAM));
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_ROUTINES.nodeId(), DELETE_ADMIN_ROUTINE);
    }
}
