package gov.nist.csd.pm.core.pap.function.op.obligation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.ObligationOpArgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_OBLIGATION;

public class CreateObligationOp extends ObligationOp<ObligationOpArgs> {

    public CreateObligationOp() {
        super(
            "create_obligation",
            List.of(AUTHOR_PARAM, NAME_PARAM, RULES_PARAM),
            CREATE_OBLIGATION
        );
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
