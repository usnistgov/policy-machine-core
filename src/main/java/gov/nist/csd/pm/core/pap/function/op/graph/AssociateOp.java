package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.NodeArg;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;

import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSOCIATE;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSOCIATE_TO;

public class AssociateOp extends AdminOperation<Void> {

    public static final NodeFormalParameter ASSOCIATE_UA_PARAM = new NodeFormalParameter("ua", ASSOCIATE);
    public static final NodeFormalParameter ASSOCIATE_TARGET_PARAM = new NodeFormalParameter("target", ASSOCIATE_TO);

    public AssociateOp() {
        super(
            "associate",
            List.of(ASSOCIATE_UA_PARAM, ASSOCIATE_TARGET_PARAM, ARSET_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        NodeArg<?> ua = args.get(ASSOCIATE_UA_PARAM);
        NodeArg<?> target = args.get(ASSOCIATE_TARGET_PARAM);
        List<String> arset = args.get(ARSET_PARAM);

        pap.modify().graph().associate(ua.getId(pap), target.getId(pap), new AccessRightSet(arset));
        return null;
    }
}
