package gov.nist.csd.pm.core.pap.operation.obligation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class DeleteObligationOp extends ObligationOp {

    public DeleteObligationOp() {
        super(
            "delete_obligation",
            List.of(NAME_PARAM)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.privilegeChecker().check(userCtx, new TargetContext(AdminPolicyNode.PM_ADMIN_OBLIGATIONS.nodeId()),
            AdminAccessRight.ADMIN_OBLIGATION_DELETE.toString());
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().obligations().deleteObligation(args.get(NAME_PARAM));
        return null;
    }
}
