package gov.nist.csd.pm.core.pap.function.op.routine;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_ADMIN_ROUTINE;

public class DeleteAdminRoutineOp extends AdminOperation<Void> {
    
    public DeleteAdminRoutineOp() {
        super(
                "delete_admin_routine",
                List.of(NAME_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().routines().deleteAdminRoutine(args.get(NAME_PARAM));
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_ROUTINES.nodeId(), DELETE_ADMIN_ROUTINE);
    }
}
