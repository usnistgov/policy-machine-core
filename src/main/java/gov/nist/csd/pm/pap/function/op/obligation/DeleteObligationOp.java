package gov.nist.csd.pm.pap.function.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_OBLIGATION;

public class DeleteObligationOp extends ObligationOp {

    public DeleteObligationOp() {
        super("delete_obligation", DELETE_OBLIGATION);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().obligations().deleteObligation(args.get(NAME_ARG));
        return null;
    }
}
