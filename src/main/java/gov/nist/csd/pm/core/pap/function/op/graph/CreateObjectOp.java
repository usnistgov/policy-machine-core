package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;

import gov.nist.csd.pm.core.pap.function.arg.Args;
import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_OBJECT;

public class CreateObjectOp extends CreateNodeOp {
    public CreateObjectOp() {
        super(
            "create_object",
            true,
            CREATE_OBJECT
        );
    }

    @Override
    public Long execute(PAP pap, Args args) throws PMException {
        String name = args.get(NAME_PARAM);
        List<Long> descIds = args.get(DESCENDANTS_PARAM);

        return pap.modify().graph().createObject(name, descIds);
    }
}
