package gov.nist.csd.pm.core.pap.function.op.graph;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;

import gov.nist.csd.pm.core.pap.function.arg.Args;
import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_USER;

public class CreateUserOp extends CreateNodeOp {
    public CreateUserOp() {
        super(
            "create_user",
            true,
            CREATE_USER
        );
    }

    @Override
    public Long execute(PAP pap, Args args) throws PMException {
        String name = args.get(NAME_PARAM);
        List<Long> descIds = args.get(DESCENDANTS_PARAM);

        return pap.modify().graph().createUser(name, descIds);
    }
}
