package gov.nist.csd.pm.pap.function.op.routine;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_ADMIN_ROUTINE;

public class DeleteAdminRoutineOp extends Operation<Void> {

    public DeleteAdminRoutineOp() {
        super(
                "delete_admin_routine",
                List.of(NAME_ARG)
        );
    }

    public Args actualArgs(String name) {
        Args args = new Args();
        args.put(NAME_ARG, name);
        return args;
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        String name = args.get(NAME_ARG);

        pap.modify().routines().deleteAdminRoutine(name);

        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args args) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), DELETE_ADMIN_ROUTINE);
    }
}
