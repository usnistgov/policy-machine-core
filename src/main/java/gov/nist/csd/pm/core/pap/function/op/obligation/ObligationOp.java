package gov.nist.csd.pm.core.pap.function.op.obligation;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.ANY_TYPE;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.obligation.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.pap.function.op.arg.IdNodeFormalParameter;
import gov.nist.csd.pm.core.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.ArgPatternExpression;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_OBLIGATIONS;

public abstract class ObligationOp extends Operation<Void, ObligationOp.ObligationOpArgs> {

    public static final IdNodeFormalParameter AUTHOR_PARAM = new IdNodeFormalParameter("author");
    public static final FormalParameter<List<Object>> RULES_PARAM = new FormalParameter<>("rules", ListType.of(ANY_TYPE));

    public ObligationOp(String opName, List<FormalParameter<?>> formalParameters) {
        super(opName, formalParameters);
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
                                         Pattern pattern,
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

    public static class ObligationOpArgs extends Args {
        private Long authorId;
        private String name;
        private List<Rule> rules;

        public ObligationOpArgs(Long authorId, String name, List<Rule> rules) {
            super(Map.of(
                AUTHOR_PARAM, authorId,
                NAME_PARAM, name,
                RULES_PARAM, rules
            ));

            this.authorId = authorId;
            this.name = name;
            this.rules = rules;
        }

        public ObligationOpArgs(String name) {
            this.name = name;
            this.authorId = null;
            this.rules = null;
        }

        public Long getAuthorId() {
            return authorId;
        }

        public String getName() {
            return name;
        }

        public List<Rule> getRules() {
            return rules;
        }
    }
}
