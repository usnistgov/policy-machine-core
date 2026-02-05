package gov.nist.csd.pm.core.pap.operation.obligation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapabilityFunc;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import java.util.List;

public class DeleteObligationOp extends AdminOperation<Void> {

    public DeleteObligationOp() {
        super(
            "delete_obligation",
            VOID_TYPE,
            List.of(NAME_PARAM),
            AdminPolicyNode.PM_ADMIN_OBLIGATIONS,
            AdminAccessRight.ADMIN_OBLIGATION_DELETE
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().obligations().deleteObligation(args.get(NAME_PARAM));
        return null;
    }
}
