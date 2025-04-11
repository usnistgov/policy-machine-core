package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.graph.AssociateOp.AssociateOpArgs;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.ASSOCIATE;
import static gov.nist.csd.pm.pap.AdminAccessRights.ASSOCIATE_TO;

public class AssociateOp extends GraphOp<Void, AssociateOpArgs> {

    public AssociateOp() {
        super(
            "associate",
            List.of(UA_ARG, TARGET_ARG, ARSET_ARG)
        );
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

    @Override
    protected AssociateOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        return new AssociateOpArgs(
            prepareArg(UA_ARG, argsMap),
            prepareArg(TARGET_ARG, argsMap),
            prepareArg(ARSET_ARG, argsMap)
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, AssociateOpArgs args) throws PMException {
        privilegeChecker.check(userCtx, args.get(UA_ARG), ASSOCIATE);
        privilegeChecker.check(userCtx, args.get(TARGET_ARG), ASSOCIATE_TO);
    }

    public static class AssociateOpArgs extends Args {
        private long ua;
        private long target;
        private AccessRightSet arset;
        public AssociateOpArgs(long ua, long target, AccessRightSet arset) {
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
