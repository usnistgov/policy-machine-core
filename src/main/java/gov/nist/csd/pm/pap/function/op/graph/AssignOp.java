package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.ASSIGN;
import static gov.nist.csd.pm.pap.AdminAccessRights.ASSIGN_TO;

public class AssignOp extends GraphOp<Void> {

    public AssignOp() {
        super(
                "assign",
                List.of(ASCENDANT_ARG, DESCENDANTS_ARG)
        );
    }

    public ActualArgs actualArgs(long ascendant, LongArrayList descendants) {
        ActualArgs actualArgs = new ActualArgs();
        actualArgs.put(ASCENDANT_ARG, ascendant);
        actualArgs.put(DESCENDANTS_ARG, descendants);
        return actualArgs;
    }

    @Override
    public Void execute(PAP pap, ActualArgs actualArgs) throws PMException {
        pap.modify().graph().assign(actualArgs.get(ASCENDANT_ARG), actualArgs.get(DESCENDANTS_ARG));
        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs actualArgs) throws PMException {
        privilegeChecker.check(userCtx, actualArgs.get(ASCENDANT_ARG), ASSIGN);
        privilegeChecker.check(userCtx, actualArgs.get(DESCENDANTS_ARG), ASSIGN_TO);
    }
}

