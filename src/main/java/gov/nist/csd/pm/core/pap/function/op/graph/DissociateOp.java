package gov.nist.csd.pm.core.pap.function.op.graph;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.ASSOCIATE_TO;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DISSOCIATE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeIdFormalParameter;
import java.util.List;

public class DissociateOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter DISSOCIATE_UA_PARAM = new NodeIdFormalParameter("ua", DISSOCIATE);
    public static final NodeIdFormalParameter DISSOCIATE_TARGET_PARAM = new NodeIdFormalParameter("target", ASSOCIATE_TO);

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
