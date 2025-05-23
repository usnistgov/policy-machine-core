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
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.op.arg.IdNodeFormalParameter;
import gov.nist.csd.pm.core.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.core.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.ArgPatternExpression;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public abstract class ObligationOp<A extends ObligationOp.ObligationOpArgs> extends Operation<Void, A> {

    public static final IdNodeFormalParameter AUTHOR_PARAM = new IdNodeFormalParameter("author");
    public static final FormalParameter<List<Object>> RULES_PARAM = new FormalParameter<>("rules", ListType.of(ANY_TYPE));

    private final String reqCap;

    public ObligationOp(String opName, List<FormalParameter<?>> formalParameters, String reqCap) {
        super(opName, formalParameters);
        this.reqCap = reqCap;
    }

    @Override
    protected abstract A prepareArgs(Map<FormalParameter<?>, Object> argsMap);

    @Override
    public void canExecute(PAP pap, UserContext userCtx, A args) throws PMException {
        List<Rule> rules = args.getRules();
        if (rules != null) {
            for (Rule rule : rules) {
                EventPattern eventPattern = rule.getEventPattern();

                Pattern pattern = eventPattern.getSubjectPattern();
                checkPatternPrivileges(pap, userCtx, pattern, reqCap);

                for (var argPattern : eventPattern.getArgPatterns().entrySet()) {
                    for (ArgPatternExpression argPatternExpression : argPattern.getValue()) {
                        checkPatternPrivileges(pap, userCtx, argPatternExpression, reqCap);
                    }
                }
            }
        } else {
            pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), reqCap);
        }
    }

    public static void checkPatternPrivileges(PAP pap, UserContext userCtx, Pattern pattern, String toCheck) throws PMException {
        ReferencedNodes referencedNodes = pattern.getReferencedNodes();
        if (referencedNodes.isAny()) {
            pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), toCheck);
            return;
        }

        for (String node : referencedNodes.nodes()) {
            long nodeId = pap.query().graph().getNodeByName(node).getId();
            pap.privilegeChecker().check(userCtx, nodeId, List.of(toCheck));
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
