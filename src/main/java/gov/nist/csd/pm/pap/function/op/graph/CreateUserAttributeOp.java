package gov.nist.csd.pm.pap.function.op.graph;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminAccessRights.CREATE_USER_ATTRIBUTE;

public class CreateUserAttributeOp extends CreateNodeOp {
    public CreateUserAttributeOp() {
        super(
            "create_user_attribute",
            List.of(NAME_PARAM, DESCENDANTS_PARAM),
            CREATE_USER_ATTRIBUTE
        );
    }

    @Override
    protected CreateNodeOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        String name = prepareArg(NAME_PARAM, argsMap);
        List<Long> descIds = prepareArg(DESCENDANTS_PARAM, argsMap);
        return new CreateNodeOpArgs(name, descIds);
    }

    @Override
    public Long execute(PAP pap, CreateNodeOpArgs args) throws PMException {
        return pap.modify().graph().createUserAttribute(
		        args.getName(),
		        args.getDescendantIds()
        );
    }
}