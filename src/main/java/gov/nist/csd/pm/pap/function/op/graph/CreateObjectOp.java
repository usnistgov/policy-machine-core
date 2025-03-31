package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_OBJECT;

public class CreateObjectOp extends CreateNodeOp{
    public CreateObjectOp() {
        super("create_object", CREATE_OBJECT);

    }

    @Override
    public Long execute(PAP pap, Args args) throws PMException {
        return pap.modify().graph().createObject(
                args.get(NAME_ARG),
                args.get(DESCENDANTS_ARG)
        );
    }
}
