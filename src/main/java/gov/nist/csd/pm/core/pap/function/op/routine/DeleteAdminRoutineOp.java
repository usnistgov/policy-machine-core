package gov.nist.csd.pm.core.pap.function.op.routine;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_ADMIN_ROUTINE;

public class DeleteAdminRoutineOp extends Operation<Void, DeleteAdminRoutineOp.DeleteAdminRoutineOpArgs> {
    
    public DeleteAdminRoutineOp() {
        super(
                "delete_admin_routine",
                List.of(NAME_PARAM)
        );
    }

    public static class DeleteAdminRoutineOpArgs extends Args {
        private final String name;

        public DeleteAdminRoutineOpArgs(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Override
    protected DeleteAdminRoutineOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        String name = prepareArg(NAME_PARAM, argsMap);
        return new DeleteAdminRoutineOpArgs(name);
    }

    @Override
    public Void execute(PAP pap, DeleteAdminRoutineOpArgs args) throws PMException {
        pap.modify().routines().deleteAdminRoutine(args.getName());
        return null;
    }

    @Override
    public void canExecute(PAP pap,
                           UserContext userCtx, DeleteAdminRoutineOpArgs args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_ROUTINES.nodeId(), DELETE_ADMIN_ROUTINE);
    }
}
