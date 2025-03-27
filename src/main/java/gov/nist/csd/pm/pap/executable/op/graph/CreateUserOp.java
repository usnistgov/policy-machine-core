package gov.nist.csd.pm.pap.executable.op.graph;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_USER;

public class CreateUserOp extends CreateNodeOp{
    public CreateUserOp() {
        super("create_user", CREATE_USER);
    }

    @Override
    public Long execute(PAP pap, ActualArgs actualArgs) throws PMException {
        return pap.modify().graph().createUser(
		        actualArgs.get(NAME_ARG),
		        actualArgs.get(DESCENDANTS_ARG)
        );
    }
}
