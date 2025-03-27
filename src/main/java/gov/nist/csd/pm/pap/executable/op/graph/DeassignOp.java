package gov.nist.csd.pm.pap.executable.op.graph;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.DEASSIGN;
import static gov.nist.csd.pm.pap.AdminAccessRights.DEASSIGN_FROM;

public class DeassignOp extends GraphOp<Void> {

    public DeassignOp() {
        super(
                "deassign",
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
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs operands) throws PMException {
        privilegeChecker.check(userCtx, operands.get(ASCENDANT_ARG), DEASSIGN);
        privilegeChecker.check(userCtx, operands.get(DESCENDANTS_ARG), DEASSIGN_FROM);
    }

    @Override
    public Void execute(PAP pap, ActualArgs actualArgs) throws PMException {
        long asc = actualArgs.get(ASCENDANT_ARG);
        List<Long> descs = actualArgs.get(DESCENDANTS_ARG);

        pap.modify().graph().deassign(asc, descs);

        return null;
    }
}