package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_OBJECT;

public class CreateObjectOp extends CreateNodeOp {
    public CreateObjectOp() {
        super(
            "create_object",
            List.of(NAME_ARG, DESCENDANTS_ARG),
            CREATE_OBJECT
        );
    }

    @Override
    protected CreateNodeOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        String name = prepareArg(NAME_ARG, argsMap);
        List<Long> descIds = prepareArg(DESCENDANTS_ARG, argsMap);
        return new CreateNodeOpArgs(name, descIds);
    }

    @Override
    public Long execute(PAP pap, CreateNodeOpArgs args) throws PMException {
        return pap.modify().graph().createObject(
                args.getName(),
                args.getDescendantIds()
        );
    }
}
