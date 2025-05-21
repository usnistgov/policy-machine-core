package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminAccessRights.DEASSIGN;
import static gov.nist.csd.pm.pap.admin.AdminAccessRights.DEASSIGN_FROM;

public class DeassignOp extends GraphOp<Void, DeassignOp.DeassignOpArgs> {

    public DeassignOp() {
        super(
                "deassign",
                List.of(ASCENDANT_PARAM, DESCENDANTS_PARAM)
        );
    }

    public static class DeassignOpArgs extends Args {
        private final long ascendantId;
        private final List<Long> descendantIds;

        public DeassignOpArgs(long ascendantId, List<Long> descendantIds) {
            super(Map.of(
                ASCENDANT_PARAM, ascendantId,
                DESCENDANTS_PARAM, descendantIds
            ));

            this.ascendantId = ascendantId;
            this.descendantIds = descendantIds;
        }

        public long getAscendantId() {
            return ascendantId;
        }

        public List<Long> getDescendantIds() {
            return descendantIds;
        }
    }

    @Override
    protected DeassignOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        Long ascId = prepareArg(ASCENDANT_PARAM, argsMap);
        List<Long> descIds = prepareArg(DESCENDANTS_PARAM, argsMap);
        return new DeassignOpArgs(ascId, descIds);
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, DeassignOpArgs args) throws PMException {
        pap.privilegeChecker().check(userCtx, args.getAscendantId(), DEASSIGN);
        pap.privilegeChecker().check(userCtx, args.getDescendantIds(), DEASSIGN_FROM);
    }

    @Override
    public Void execute(PAP pap, DeassignOpArgs args) throws PMException {
        pap.modify().graph().deassign(args.getAscendantId(), args.getDescendantIds());
        return null;
    }
}