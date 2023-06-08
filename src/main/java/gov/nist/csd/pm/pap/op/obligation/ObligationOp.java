package gov.nist.csd.pm.pap.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.EventPattern;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.pattern.operand.OperandPatternExpression;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;
import java.util.Map;

public abstract class ObligationOp extends Operation<Void> {

    public static final String AUTHOR_OPERAND = "author";
    public static final String RULES_OPERAND = "rules";

    private String reqCap;

    public ObligationOp(String opName, String reqCap) {
        super(
                opName,
                List.of(AUTHOR_OPERAND, NAME_OPERAND,  RULES_OPERAND)
        );

        this.reqCap = reqCap;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {
        List<Rule> rules = (List<Rule>) operands.get(RULES_OPERAND);
        for (Rule rule : rules) {
            EventPattern eventPattern = rule.getEventPattern();

            // check subject pattern
            Pattern pattern = eventPattern.getSubjectPattern();
            checkPatternPrivileges(pap, userCtx, pattern, reqCap);

            // check operand patterns
            for (Map.Entry<String, List<OperandPatternExpression>> operandPattern : eventPattern.getOperandPatterns().entrySet()) {
                for (OperandPatternExpression operandPatternExpression : operandPattern.getValue()) {
                    checkPatternPrivileges(pap, userCtx, operandPatternExpression, reqCap);
                }
            }
        }
    }

    static void checkPatternPrivileges(PAP pap, UserContext userCtx, Pattern pattern, String toCheck) throws PMException {
        ReferencedNodes referencedNodes = pattern.getReferencedNodes();
        if (referencedNodes.isAny()) {
            PrivilegeChecker.check(pap, userCtx, AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName(), toCheck);

            return;
        }

        for (String entity : referencedNodes.nodes()) {
            PrivilegeChecker.check(pap, userCtx, entity, toCheck);
        }
    }
}
