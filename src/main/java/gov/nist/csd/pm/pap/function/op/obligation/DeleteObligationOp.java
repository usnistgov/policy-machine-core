package gov.nist.csd.pm.pap.function.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_OBLIGATION;

public class DeleteObligationOp extends ObligationOp {

    public DeleteObligationOp() {
        super("delete_obligation", DELETE_OBLIGATION);
    }

    @Override
    public Void execute(PAP pap, ActualArgs actualArgs) throws PMException {
        pap.modify().obligations().deleteObligation(actualArgs.get(NAME_ARG));
        return null;
    }
}
