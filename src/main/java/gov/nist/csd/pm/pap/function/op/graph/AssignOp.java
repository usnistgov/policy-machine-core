package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.graph.AssignOp.AssignOpArgs;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.ASSIGN;
import static gov.nist.csd.pm.pap.AdminAccessRights.ASSIGN_TO;

public class AssignOp extends GraphOp<Void, AssignOpArgs> {

    public AssignOp() {
        super(
                "assign",
                List.of(ASCENDANT_ARG, DESCENDANTS_ARG)
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, AssignOpArgs args) throws PMException {
        privilegeChecker.check(userCtx, args.getAscendantId(), ASSIGN);
        privilegeChecker.check(userCtx, args.getDescendantIds(), ASSIGN_TO);
    }

    @Override
    public Void execute(PAP pap, AssignOpArgs args) throws PMException {
        pap.modify().graph().assign(args.getAscendantId(), args.getDescendantIds());
        return null;
    }

    @Override
    protected AssignOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        Long ascId = prepareArg(ASCENDANT_ARG, argsMap);
        List<Long> descIds = prepareArg(DESCENDANTS_ARG, argsMap);

        return new AssignOpArgs(ascId, descIds);
    }

    public static class AssignOpArgs extends Args {
        private final long ascendantId;
        private final List<Long> descendantIds;

        public AssignOpArgs(long ascendantId, List<Long> descendantIds) {
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
}

