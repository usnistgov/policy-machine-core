package gov.nist.csd.pm.core.pap.function.op.routine;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_ADMIN_ROUTINE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.function.arg.type.RoutineType;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.Routine;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class CreateAdminRoutineOp extends AdminOperation<Void> {

    public static final FormalParameter<Routine<?>> ROUTINE_PARAM = new FormalParameter<>("routine", new RoutineType());

    public CreateAdminRoutineOp() {
        super(
            "create_admin_routine",
            BasicTypes.VOID_TYPE,
            List.of(ROUTINE_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().routines().createAdminRoutine(args.get(ROUTINE_PARAM));
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_ROUTINES.nodeId(), CREATE_ADMIN_ROUTINE);
    }
}
