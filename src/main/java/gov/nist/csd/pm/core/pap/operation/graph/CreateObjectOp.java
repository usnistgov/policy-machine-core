package gov.nist.csd.pm.core.pap.operation.graph;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_OBJECT;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import java.util.List;

public class CreateObjectOp extends AdminOperation<Long> {
    public static final NodeIdListFormalParameter CREATE_O_DESCENDANTS_PARAM =
        new NodeIdListFormalParameter("descendants", CREATE_OBJECT);

    public CreateObjectOp() {
        super(
            "create_object",
            BasicTypes.LONG_TYPE,
            List.of(NAME_PARAM, CREATE_O_DESCENDANTS_PARAM)
        );
    }

    @Override
    public Long execute(PAP pap, Args args) throws PMException {
        String name = args.get(NAME_PARAM);
        List<Long> descIds = args.get(CREATE_O_DESCENDANTS_PARAM);

        return pap.modify().graph().createObject(name, descIds);
    }
}
