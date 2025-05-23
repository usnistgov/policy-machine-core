package gov.nist.csd.pm.core.pap.function.op.graph;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_USER;

public class CreateUserOp extends CreateNodeOp {
    public CreateUserOp() {
        super(
            "create_user",
            List.of(NAME_PARAM, DESCENDANTS_PARAM),
            CREATE_USER
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
        return pap.modify().graph().createUser(
		        args.getName(),
		        args.getDescendantIds()
        );
    }
}
