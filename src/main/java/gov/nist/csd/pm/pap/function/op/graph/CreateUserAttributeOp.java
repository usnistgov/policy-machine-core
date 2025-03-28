package gov.nist.csd.pm.pap.function.op.graph;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_USER_ATTRIBUTE;

public class CreateUserAttributeOp extends CreateNodeOp{
    public CreateUserAttributeOp() {
        super("create_user_attribute", CREATE_USER_ATTRIBUTE);

    }

    @Override
    public Long execute(PAP pap, ActualArgs actualArgs) throws PMException {
        return pap.modify().graph().createUserAttribute(
		        actualArgs.get(NAME_ARG),
		        actualArgs.get(DESCENDANTS_ARG)
        );
    }
}