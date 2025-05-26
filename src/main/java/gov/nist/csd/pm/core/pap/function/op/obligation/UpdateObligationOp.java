package gov.nist.csd.pm.core.pap.function.op.obligation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.ObligationOpArgs;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateObligationOp extends ObligationOp<ObligationOpArgs> {

    public UpdateObligationOp() {
        super(
            "update_obligation",
            List.of(AUTHOR_PARAM, NAME_PARAM, RULES_PARAM),
            ""
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, ObligationOpArgs args) throws PMException {
        new DeleteObligationOp().canExecute(pap, userCtx, args);
        new CreateObligationOp().canExecute(pap, userCtx, args);
    }

    @Override
    public Void execute(PAP pap, ObligationOpArgs args) throws PMException {
        long author = args.getAuthorId();
        String name = args.getName();
        List<Rule> rules = args.getRules();

        // delete the obligation
        pap.modify().obligations().deleteObligation(name);

        // recreate it with updated ruleset
        pap.modify().obligations().createObligation(author, name, rules);

        return null;
    }

    @Override
    protected ObligationOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        Long authorId = prepareArg(AUTHOR_PARAM, argsMap);
        String name = prepareArg(NAME_PARAM, argsMap);
        List<Object> objs = prepareArg(RULES_PARAM, argsMap);
        List<Rule> rules = new ArrayList<>();
        for (Object obj : objs) {
            rules.add((Rule) obj);
        }
        return new ObligationOpArgs(authorId, name, rules);
    }
}
