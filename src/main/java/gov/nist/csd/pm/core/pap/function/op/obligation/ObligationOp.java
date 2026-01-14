package gov.nist.csd.pm.core.pap.function.op.obligation;

import static gov.nist.csd.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_OBLIGATIONS;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.RuleType;
import gov.nist.csd.pm.core.pap.function.op.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.obligation.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.ArgPatternExpression;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public abstract class ObligationOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter AUTHOR_PARAM = new NodeIdFormalParameter("author");
    public static final FormalParameter<List<Rule>> RULES_PARAM = new FormalParameter<>("rules", ListType.of(new RuleType()));

    public ObligationOp(String opName, List<FormalParameter<?>> formalParameters) {
        super(opName, formalParameters);
    }

    public static void checkObligationRulePrivileges(PAP pap, UserContext userCtx, List<Rule> rules,
                                                 String nodeAR, String anyPatternAR) throws PMException {
        for (Rule rule : rules) {
            EventPattern eventPattern = rule.getEventPattern();

            // Check subject pattern permissions
            checkPatternPermissions(pap, userCtx, eventPattern.getSubjectPattern(), nodeAR, anyPatternAR);

            // Check arg pattern permissions
            for (var argPattern : eventPattern.getArgPatterns().entrySet()) {
                for (ArgPatternExpression argPatternExpression : argPattern.getValue()) {
                    checkPatternPermissions(pap, userCtx, argPatternExpression, nodeAR, anyPatternAR);
                }
            }
        }
    }

    private static void checkPatternPermissions(PAP pap,
                                         UserContext userCtx,
                                         Pattern<?> pattern,
                                         String nodeAccessRight,
                                         String anyPatternAccessRight) throws PMException {
        ReferencedNodes referencedNodes = pattern.getReferencedNodes();
        if (referencedNodes.isAny()) {
            pap.privilegeChecker().check(userCtx, PM_ADMIN_OBLIGATIONS.nodeId(), anyPatternAccessRight);
        } else {
            for (String node : referencedNodes.nodes()) {
                long nodeId = pap.query().graph().getNodeByName(node).getId();
                pap.privilegeChecker().check(userCtx, nodeId, List.of(nodeAccessRight));
            }
        }
    }
}
