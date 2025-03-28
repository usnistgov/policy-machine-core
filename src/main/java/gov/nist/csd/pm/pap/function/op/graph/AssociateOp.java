package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
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

    public ActualArgs actualArgs(long ua, long target, AccessRightSet arset) {
        ActualArgs actualArgs = new ActualArgs();
        actualArgs.put(UA_ARG, ua);
        actualArgs.put(TARGET_ARG, target);
        actualArgs.put(ARSET_ARG, arset);
        return actualArgs;
    }

    @Override
    public Void execute(PAP pap, ActualArgs actualArgs) throws PMException {
        pap.modify().graph().associate(
                actualArgs.get(UA_ARG),
                actualArgs.get(TARGET_ARG),
                actualArgs.get(ARSET_ARG)
        );
        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs actualArgs) throws PMException {
        privilegeChecker.check(userCtx, actualArgs.get(UA_ARG), ASSOCIATE);
        privilegeChecker.check(userCtx, actualArgs.get(TARGET_ARG), ASSOCIATE_TO);
    }
}
