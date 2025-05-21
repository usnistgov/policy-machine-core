package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminAccessRights.DISSOCIATE;
import static gov.nist.csd.pm.pap.admin.AdminAccessRights.DISSOCIATE_FROM;

public class DissociateOp extends GraphOp<Void, DissociateOp.DissociateOpArgs> {

    public DissociateOp() {
        super(
                "dissociate",
                List.of(UA_PARAM, TARGET_PARAM)
        );
    }

    public static class DissociateOpArgs extends Args {
        private final long uaId;
        private final long targetId;

        public DissociateOpArgs(long uaId, long targetId) {
            super(Map.of(
                UA_PARAM, uaId,
                TARGET_PARAM, targetId
            ));

            this.uaId = uaId;
            this.targetId = targetId;
        }

        public long getUaId() {
            return uaId;
        }

        public long getTargetId() {
            return targetId;
        }
    }

    @Override
    protected DissociateOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        Long uaId = prepareArg(UA_PARAM, argsMap);
        Long targetId = prepareArg(TARGET_PARAM, argsMap);
        return new DissociateOpArgs(uaId, targetId);
    }

    @Override
    public Void execute(PAP pap, DissociateOpArgs args) throws PMException {
        pap.modify().graph().dissociate(
                args.getUaId(),
                args.getTargetId()
        );
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, DissociateOpArgs args) throws PMException {
        pap.privilegeChecker().check(userCtx, args.getUaId(), DISSOCIATE);
        pap.privilegeChecker().check(userCtx, args.getTargetId(), DISSOCIATE_FROM);
    }
}
