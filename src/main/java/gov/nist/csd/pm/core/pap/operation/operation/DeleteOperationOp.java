package gov.nist.csd.pm.core.pap.operation.operation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredPrivilegeOnNode;
import java.util.List;

public class DeleteOperationOp extends AdminOperation<Void> {

    public DeleteOperationOp() {
        super(
            "delete_operation",
            VOID_TYPE,
            List.of(NAME_PARAM),
            new RequiredCapability(
                new RequiredPrivilegeOnNode(AdminPolicyNode.PM_ADMIN_OPERATIONS.nodeName(), AdminAccessRight.ADMIN_OPERATION_DELETE)
            )
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().operations().deleteOperation(args.get(NAME_PARAM));
        return null;
    }
}