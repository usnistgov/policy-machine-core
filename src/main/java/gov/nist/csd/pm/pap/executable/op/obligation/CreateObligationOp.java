package gov.nist.csd.pm.pap.executable.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;

import java.util.List;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_OBLIGATION;
import static gov.nist.csd.pm.pap.executable.op.obligation.ObligationOp.*;

public class CreateObligationOp extends ObligationOp {

    public CreateObligationOp() {
        super("create_obligation", CREATE_OBLIGATION);
    }

    @Override
    public Void execute(PAP pap, ActualArgs args) throws PMException {
        pap.modify().obligations().createObligation(
                args.get(AUTHOR_ARG),
                args.get(NAME_ARG),
                args.get(RULES_ARG)
        );
        return null;
    }
}
