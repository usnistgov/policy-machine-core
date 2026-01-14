package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.NodeArg;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeListFormalParameter;

import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSIGN;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSIGN_TO;

public class AssignOp extends AdminOperation<Void> {

    public static final NodeFormalParameter ASSIGN_ASCENDANT_PARAM =
        new NodeFormalParameter("ascendant", ASSIGN);

    public static final NodeListFormalParameter ASSIGN_DESCENDANTS_PARAM =
        new NodeListFormalParameter("descendants", ASSIGN_TO);

    public AssignOp() {
        super(
            "assign",
            List.of(ASSIGN_ASCENDANT_PARAM, ASSIGN_DESCENDANTS_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        NodeArg<?> asc = args.get(ASSIGN_ASCENDANT_PARAM);
        List<Long> descs = args.getIdList(ASSIGN_DESCENDANTS_PARAM, pap);

        pap.modify().graph().assign(asc.getId(pap), descs);
        return null;
    }
}

