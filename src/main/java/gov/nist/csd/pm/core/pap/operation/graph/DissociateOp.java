package gov.nist.csd.pm.core.pap.operation.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import java.util.List;
import java.util.Map;

public class DissociateOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter DISSOCIATE_UA_PARAM = new NodeIdFormalParameter("ua");
    public static final NodeIdFormalParameter DISSOCIATE_TARGET_PARAM = new NodeIdFormalParameter("target");

    public DissociateOp() {
        super(
            "dissociate",
            BasicTypes.VOID_TYPE,
            List.of(DISSOCIATE_UA_PARAM, DISSOCIATE_TARGET_PARAM),
            new RequiredCapability(
                DISSOCIATE_UA_PARAM, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_UA_DELETE),
                DISSOCIATE_TARGET_PARAM, new AccessRightSet(AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_TARGET_DELETE)
            )
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
