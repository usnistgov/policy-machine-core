package gov.nist.csd.pm.core.pap.operation.graph;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import java.util.List;

public class AssociateOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter ASSOCIATE_UA_PARAM = new NodeIdFormalParameter("ua",
        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_CREATE_UA);
    public static final NodeIdFormalParameter ASSOCIATE_TARGET_PARAM = new NodeIdFormalParameter("target",
        AdminAccessRight.ADMIN_GRAPH_ASSOCIATION_CREATE_TARGET);

    public AssociateOp() {
        super(
            "associate",
            VOID_TYPE,
            List.of(ASSOCIATE_UA_PARAM, ASSOCIATE_TARGET_PARAM, ARSET_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        long ua = args.get(ASSOCIATE_UA_PARAM);
        long target = args.get(ASSOCIATE_TARGET_PARAM);
        List<String> arset = args.get(ARSET_PARAM);

        pap.modify().graph().associate(ua, target, new AccessRightSet(arset));
        return null;
    }
}
