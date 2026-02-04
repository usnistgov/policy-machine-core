package gov.nist.csd.pm.core.pap.operation.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import java.util.List;

public class DissociateOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter DISSOCIATE_UA_PARAM = new NodeIdFormalParameter("ua",
        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_UA_DELETE);
    public static final NodeIdFormalParameter DISSOCIATE_TARGET_PARAM = new NodeIdFormalParameter("target",
        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_TARGET_DELETE);

    public DissociateOp() {
        super(
            "dissociate",
            BasicTypes.VOID_TYPE,
            List.of(DISSOCIATE_UA_PARAM, DISSOCIATE_TARGET_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        long uaId = args.get(DISSOCIATE_UA_PARAM);
        long targetId = args.get(DISSOCIATE_TARGET_PARAM);

        pap.modify().graph().dissociate(uaId, targetId);
        return null;
    }
}
