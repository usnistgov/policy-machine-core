package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeFormalParameter;

import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSOCIATE_TO;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DISSOCIATE;

public class DissociateOp extends AdminOperation<Void> {

    public static final NodeFormalParameter DISSOCIATE_UA_PARAM = new NodeFormalParameter("ua", DISSOCIATE);
    public static final NodeFormalParameter DISSOCIATE_TARGET_PARAM = new NodeFormalParameter("target", ASSOCIATE_TO);

    public DissociateOp() {
        super(
                "dissociate",
                List.of(DISSOCIATE_UA_PARAM, DISSOCIATE_TARGET_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        long uaId = args.get(DISSOCIATE_UA_PARAM).getId(pap);
        long targetId = args.get(DISSOCIATE_TARGET_PARAM).getId(pap);

        pap.modify().graph().dissociate(uaId, targetId);
        return null;
    }
}
