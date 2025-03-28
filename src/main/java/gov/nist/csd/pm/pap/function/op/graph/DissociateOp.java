package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.DISSOCIATE;
import static gov.nist.csd.pm.pap.AdminAccessRights.DISSOCIATE_FROM;

public class DissociateOp extends GraphOp<Void> {

    public DissociateOp() {
        super(
                "dissociate",
                List.of(UA_ARG, TARGET_ARG)
        );
    }
    
    public ActualArgs actualArgs(long ua, long target) {
        ActualArgs actualArgs = new ActualArgs();
        actualArgs.put(UA_ARG, ua);
        actualArgs.put(TARGET_ARG, target);
        return actualArgs;
    }

    @Override
    public Void execute(PAP pap, ActualArgs actualArgs) throws PMException {
        pap.modify().graph().dissociate(
                actualArgs.get(UA_ARG),
                actualArgs.get(TARGET_ARG)
        );
        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs actualArgs) throws PMException {
        privilegeChecker.check(userCtx, actualArgs.get(UA_ARG), DISSOCIATE);
        privilegeChecker.check(userCtx, actualArgs.get(TARGET_ARG), DISSOCIATE_FROM);
    }
}
