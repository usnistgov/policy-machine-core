package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.Args;
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
    
    public Args actualArgs(long ua, long target) {
        Args args = new Args();
        args.put(UA_ARG, ua);
        args.put(TARGET_ARG, target);
        return args;
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().graph().dissociate(
                args.get(UA_ARG),
                args.get(TARGET_ARG)
        );
        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args args) throws PMException {
        privilegeChecker.check(userCtx, args.get(UA_ARG), DISSOCIATE);
        privilegeChecker.check(userCtx, args.get(TARGET_ARG), DISSOCIATE_FROM);
    }
}
