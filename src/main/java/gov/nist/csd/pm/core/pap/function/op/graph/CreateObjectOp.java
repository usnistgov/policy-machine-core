package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;

import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeListFormalParameter;
import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_OBJECT;

public class CreateObjectOp extends AdminOperation<Long> {
    public static final NodeListFormalParameter CREATE_O_DESCENDANTS_PARAM =
        new NodeListFormalParameter("descendants", CREATE_OBJECT);

    public CreateObjectOp() {
        super(
            "create_object",
            List.of(NAME_PARAM, CREATE_O_DESCENDANTS_PARAM)
        );
    }

    @Override
    public Long execute(PAP pap, Args args) throws PMException {
        String name = args.get(NAME_PARAM);
        List<Long> descIds = args.getIdList(CREATE_O_DESCENDANTS_PARAM, pap);

        return pap.modify().graph().createObject(name, descIds);
    }
}
