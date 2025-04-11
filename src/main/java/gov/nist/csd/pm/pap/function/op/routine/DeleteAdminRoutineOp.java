package gov.nist.csd.pm.pap.function.op.routine;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_ADMIN_ROUTINE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;

public class DeleteAdminRoutineOp extends Operation<Void, DeleteAdminRoutineOp.DeleteAdminRoutineOpArgs> {

    public static final FormalParameter<String> NAME_ARG = new FormalParameter<>("name", STRING_TYPE);

    public DeleteAdminRoutineOp() {
        super(
                "delete_admin_routine",
                List.of(NAME_ARG)
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
    public DeleteAdminRoutineOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        String name = prepareArg(NAME_ARG, argsMap);
        return new DeleteAdminRoutineOpArgs(name);
    }

    @Override
    public Void execute(PAP pap, DeleteAdminRoutineOpArgs args) throws PMException {
        pap.modify().routines().deleteAdminRoutine(args.getName());
        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, DeleteAdminRoutineOpArgs args) throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), DELETE_ADMIN_ROUTINE);
    }
}
