package gov.nist.csd.pm.pap.executable.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.EventPattern;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.executable.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.pml.pattern.operand.OperandPatternExpression;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public abstract class ObligationOp extends Operation<Void> {

    public static final String AUTHOR_OPERAND = "author";
    public static final String RULES_OPERAND = "rules";

    private final String reqCap;

    public ObligationOp(String opName, String reqCap) {
        super(
                opName,
                List.of(AUTHOR_OPERAND, NAME_OPERAND,  RULES_OPERAND)
        );

        this.reqCap = reqCap;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        List<Rule> rules = (List<Rule>) operands.get(RULES_OPERAND);
        for (Rule rule : rules) {
            EventPattern eventPattern = rule.getEventPattern();

            // check subject pattern
            Pattern pattern = eventPattern.getSubjectPattern();
            checkPatternPrivileges(privilegeChecker, userCtx, pattern, reqCap);

            // check operand patterns
            for (Map.Entry<String, List<OperandPatternExpression>> operandPattern : eventPattern.getOperandPatterns().entrySet()) {
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
