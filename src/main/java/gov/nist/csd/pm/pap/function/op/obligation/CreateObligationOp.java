package gov.nist.csd.pm.pap.function.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.function.op.obligation.ObligationOp.ObligationOpArgs;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_OBLIGATION;

public class CreateObligationOp extends ObligationOp<ObligationOpArgs> {

    public CreateObligationOp() {
        super(
            "create_obligation",
            List.of(AUTHOR_ARG, NAME_ARG, RULES_ARG),
            CREATE_OBLIGATION
        );
    }

    @Override
    public ObligationOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        Long authorId = prepareArg(AUTHOR_ARG, argsMap);
        String name = prepareArg(NAME_ARG, argsMap);
        List<Rule> rules = prepareArg(RULES_ARG, argsMap);
        return new ObligationOpArgs(authorId, name, rules);
    }

    @Override
    public Void execute(PAP pap, ObligationOpArgs args) throws PMException {
        pap.modify().obligations().createObligation(
                args.getAuthorId(),
                args.getName(),
                args.getRules()
        );
        return null;
    }
}
