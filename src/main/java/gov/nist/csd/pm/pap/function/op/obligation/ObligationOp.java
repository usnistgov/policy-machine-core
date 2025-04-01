package gov.nist.csd.pm.pap.function.op.obligation;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.stringType;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.function.arg.type.RuleType;
import gov.nist.csd.pm.pap.obligation.EventPattern;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.function.op.arg.IdNodeFormalArg;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.pml.pattern.arg.ArgPatternExpression;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

public abstract class ObligationOp extends Operation<Void> {

    public static final IdNodeFormalArg AUTHOR_ARG = new IdNodeFormalArg("author");
    public static final FormalArg<String> NAME_ARG = new FormalArg<>("name", stringType());
    public static final FormalArg<List<Rule>> RULES_ARG = new FormalArg<>("rules", listType(new RuleType()));

    private final String reqCap;

    public ObligationOp(String opName, String reqCap) {
        super(
                opName,
                List.of(AUTHOR_ARG, NAME_ARG, RULES_ARG)
        );

        this.reqCap = reqCap;
    }

    public Args actualArgs(long author, String name, List<Rule> rules) {
        Args args = new Args();
        args.put(AUTHOR_ARG, author);
        args.put(NAME_ARG, name);
        args.put(RULES_ARG, rules);
        return args;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args actulArgs) throws PMException {
        List<Rule> rules = actulArgs.get(RULES_ARG);
        for (Rule rule : rules) {
            EventPattern eventPattern = rule.getEventPattern();

            // check subject pattern
            Pattern pattern = eventPattern.getSubjectPattern();
            checkPatternPrivileges(privilegeChecker, userCtx, pattern, reqCap);

            // check arg patterns
            for (var argPattern : eventPattern.getArgPatterns().entrySet()) {
                for (ArgPatternExpression argPatternExpression : argPattern.getValue()) {
                    checkPatternPrivileges(privilegeChecker, userCtx, argPatternExpression, reqCap);
                }
            }
        }
    }

    static void checkPatternPrivileges(PrivilegeChecker privilegeChecker, UserContext userCtx, Pattern pattern, String toCheck) throws PMException {
        ReferencedNodes referencedNodes = pattern.getReferencedNodes();
        if (referencedNodes.isAny()) {
            privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), toCheck);

            return;
        }

        for (String node : referencedNodes.nodes()) {
            privilegeChecker.check(userCtx, node, List.of(toCheck));
        }
    }
}
