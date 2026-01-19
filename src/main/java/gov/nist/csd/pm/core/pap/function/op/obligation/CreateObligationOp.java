package gov.nist.csd.pm.core.pap.function.op.obligation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class CreateObligationOp extends ObligationOp {

    public CreateObligationOp() {
        super(
            "create_obligation",
            List.of(AUTHOR_PARAM, NAME_PARAM, EVENT_PATTERN_PARAM, OBLIGATION_RESPONSE_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().obligations().createObligation(
            args.get(AUTHOR_PARAM),
            args.get(NAME_PARAM),
            args.get(EVENT_PATTERN_PARAM),
            args.get(OBLIGATION_RESPONSE_PARAM)
        );
        return null;
    }
}
