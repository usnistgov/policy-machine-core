package gov.nist.csd.pm.pap.function.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_OBLIGATION;

public class CreateObligationOp extends ObligationOp {

    public CreateObligationOp() {
        super("create_obligation", CREATE_OBLIGATION);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().obligations().createObligation(
                args.get(AUTHOR_ARG),
                args.get(NAME_ARG),
                args.get(RULES_ARG)
        );
        return null;
    }
}
