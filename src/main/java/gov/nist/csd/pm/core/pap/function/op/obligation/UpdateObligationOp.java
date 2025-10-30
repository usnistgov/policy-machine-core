package gov.nist.csd.pm.core.pap.function.op.obligation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class UpdateObligationOp extends ObligationOp {

    public UpdateObligationOp() {
        super(
            "update_obligation",
            List.of(AUTHOR_PARAM, NAME_PARAM, RULES_PARAM)
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        new DeleteObligationOp().canExecute(pap, userCtx, args);
        new CreateObligationOp().canExecute(pap, userCtx, args);
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        long author = args.get(AUTHOR_PARAM);
        String name = args.get(NAME_PARAM);
        List<Rule> rules = args.get(RULES_PARAM);

        // delete the obligation
        pap.modify().obligations().deleteObligation(name);

        // recreate it with updated ruleset
        pap.modify().obligations().createObligation(author, name, rules);

        return null;
    }
}
