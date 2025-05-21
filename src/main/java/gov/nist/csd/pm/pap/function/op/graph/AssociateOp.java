package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.graph.AssociateOp.AssociateOpArgs;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminAccessRights.ASSOCIATE;
import static gov.nist.csd.pm.pap.admin.AdminAccessRights.ASSOCIATE_TO;

public class AssociateOp extends GraphOp<Void, AssociateOpArgs> {

    public AssociateOp() {
        super(
            "associate",
            List.of(UA_PARAM, TARGET_PARAM, ARSET_PARAM)
        );
    }

    @Override
    protected AssociateOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        return new AssociateOpArgs(
            prepareArg(UA_PARAM, argsMap),
            prepareArg(TARGET_PARAM, argsMap),
            new AccessRightSet(prepareArg(ARSET_PARAM, argsMap))
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, AssociateOpArgs args) throws PMException {
        pap.privilegeChecker().check(userCtx, args.getUa(), ASSOCIATE);
        pap.privilegeChecker().check(userCtx, args.getTarget(), ASSOCIATE_TO);
    }

    @Override
    public Void execute(PAP pap, AssociateOpArgs args) throws PMException {
        pap.modify().graph().associate(
            args.getUa(),
            args.getTarget(),
            args.getArset()
        );
        return null;
    }

    public static class AssociateOpArgs extends Args {
        private long ua;
        private long target;
        private AccessRightSet arset;
        public AssociateOpArgs(long ua, long target, AccessRightSet arset) {
            super(Map.of(
                UA_PARAM, ua,
                TARGET_PARAM, target,
                ARSET_PARAM, arset
            ));

            this.ua = ua;
            this.target = target;
            this.arset = arset;
        }

        public long getUa() {
            return ua;
        }

        public long getTarget() {
            return target;
        }

        public AccessRightSet getArset() {
            return arset;
        }
    }
}
