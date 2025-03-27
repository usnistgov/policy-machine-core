package gov.nist.csd.pm.pap.executable.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.EventPattern;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.arg.FormalArg;
import gov.nist.csd.pm.pap.executable.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.executable.op.arg.IdNodeFormalArg;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.pml.pattern.operand.OperandPatternExpression;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

public abstract class ObligationOp extends Operation<Void> {

    public static final IdNodeFormalArg AUTHOR_ARG = new IdNodeFormalArg("author");
    public static final FormalArg<String> NAME_ARG = new FormalArg<>("name", String.class);
    public static final FormalArg<RuleList> RULES_ARG = new FormalArg<>("rules", RuleList.class);

    private final String reqCap;

    public ObligationOp(String opName, String reqCap) {
        super(
                opName,
                List.of(AUTHOR_ARG, NAME_ARG, RULES_ARG)
        );

        this.reqCap = reqCap;
    }

    public ActualArgs actualArgs(long author, String name, RuleList rules) {
        ActualArgs args = new ActualArgs();
        args.put(AUTHOR_ARG, author);
        args.put(NAME_ARG, name);
        args.put(RULES_ARG, rules);
        return args;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs actulArgs) throws PMException {
        List<Rule> rules = actulArgs.get(RULES_ARG);
        for (Rule rule : rules) {
            EventPattern eventPattern = rule.getEventPattern();

            // check subject pattern
            Pattern pattern = eventPattern.getSubjectPattern();
            checkPatternPrivileges(privilegeChecker, userCtx, pattern, reqCap);

            // check operand patterns
            for (var operandPattern : eventPattern.getArgPatterns().entrySet()) {
                for (OperandPatternExpression operandPatternExpression : operandPattern.getValue()) {
                    checkPatternPrivileges(privilegeChecker, userCtx, operandPatternExpression, reqCap);
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
