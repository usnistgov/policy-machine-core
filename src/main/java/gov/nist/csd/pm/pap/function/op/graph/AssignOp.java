package gov.nist.csd.pm.pap.function.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.Args;
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

    public Args actualArgs(long ascendant, LongArrayList descendants) {
        Args args = new Args();
        args.put(ASCENDANT_ARG, ascendant);
        args.put(DESCENDANTS_ARG, descendants);
        return args;
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().graph().assign(args.get(ASCENDANT_ARG), args.get(DESCENDANTS_ARG));
        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args args) throws PMException {
        privilegeChecker.check(userCtx, args.get(ASCENDANT_ARG), ASSIGN);
        privilegeChecker.check(userCtx, args.get(DESCENDANTS_ARG), ASSIGN_TO);
    }
}

