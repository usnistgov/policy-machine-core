package gov.nist.csd.pm.pap.executable.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_OBLIGATION;
import static gov.nist.csd.pm.pap.executable.op.obligation.ObligationOp.NAME_ARG;

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
