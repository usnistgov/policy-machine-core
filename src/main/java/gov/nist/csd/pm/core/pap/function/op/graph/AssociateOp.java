package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSOCIATE;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSOCIATE_TO;

public class AssociateOp extends GraphOp<Void> {

    public AssociateOp() {
        super(
            "associate",
            List.of(UA_PARAM, TARGET_PARAM, ARSET_PARAM)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        long uaId = args.get(UA_PARAM);
        long targetId = args.get(TARGET_PARAM);

        pap.privilegeChecker().check(userCtx, uaId, ASSOCIATE);
        pap.privilegeChecker().check(userCtx, targetId, ASSOCIATE_TO);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        long uaId = args.get(UA_PARAM);
        long targetId = args.get(TARGET_PARAM);
        List<String> arset = args.get(ARSET_PARAM);

        pap.modify().graph().associate(uaId, targetId, new AccessRightSet(arset));
        return null;
    }
}
