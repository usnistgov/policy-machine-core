package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_OBJECT;

public class CreateObjectOp extends CreateNodeOp{
    public CreateObjectOp() {
        super("create_object", CREATE_OBJECT);

    }

    @Override
    public Long execute(PAP pap, ActualArgs actualArgs) throws PMException {
        return pap.modify().graph().createObject(
                actualArgs.get(NAME_ARG),
                actualArgs.get(DESCENDANTS_ARG)
        );
    }
}
