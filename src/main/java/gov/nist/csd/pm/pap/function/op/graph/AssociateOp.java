package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.ASSOCIATE;
import static gov.nist.csd.pm.pap.AdminAccessRights.ASSOCIATE_TO;

public class AssociateOp extends GraphOp<Void> {

    public AssociateOp() {
        super(
                "associate",
                List.of(UA_ARG, TARGET_ARG, ARSET_ARG)
        );
    }

    public Args actualArgs(long ua, long target, AccessRightSet arset) {
        Args args = new Args();
        args.put(UA_ARG, ua);
        args.put(TARGET_ARG, target);
        args.put(ARSET_ARG, arset);
        return args;
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().graph().associate(
                args.get(UA_ARG),
                args.get(TARGET_ARG),
                new AccessRightSet(args.get(ARSET_ARG))
        );
        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args args) throws PMException {
        privilegeChecker.check(userCtx, args.get(UA_ARG), ASSOCIATE);
        privilegeChecker.check(userCtx, args.get(TARGET_ARG), ASSOCIATE_TO);
    }
}
