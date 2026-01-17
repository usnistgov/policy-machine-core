package gov.nist.csd.pm.core.pap.function.op.graph;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSIGN;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSIGN_TO;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeIdListFormalParameter;
import java.util.List;

public class AssignOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter ASSIGN_ASCENDANT_PARAM =
        new NodeIdFormalParameter("ascendant", ASSIGN);

    public static final NodeIdListFormalParameter ASSIGN_DESCENDANTS_PARAM =
        new NodeIdListFormalParameter("descendants", ASSIGN_TO);

    public AssignOp() {
        super(
            "assign",
            VOID_TYPE,
            List.of(ASSIGN_ASCENDANT_PARAM, ASSIGN_DESCENDANTS_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        Long ascId = args.get(ASSIGN_ASCENDANT_PARAM);
        List<Long> descIds = args.get(ASSIGN_DESCENDANTS_PARAM);

        pap.modify().graph().assign(ascId, descIds);
        return null;
    }
}

