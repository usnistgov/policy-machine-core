package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CreateRuleStatement extends PALStatement {

    private final Expression name;
    private final SubjectClause subjectClause;
    private final PerformsClause performsClause;
    private final OnClause onClause;
    private final ResponseBlock responseBlock;

    public CreateRuleStatement(Expression name, SubjectClause subjectClause,
                               PerformsClause performsClause, OnClause onClause, ResponseBlock responseBlock) {
        this.name = name;
        this.subjectClause = subjectClause;
        this.performsClause = performsClause;
        this.onClause = onClause;
        this.responseBlock = responseBlock;
    }

    public Expression getName() {
        return name;
    }

    public SubjectClause getSubjectClause() {
        return subjectClause;
    }

    public PerformsClause getPerformsClause() {
        return performsClause;
    }

    public OnClause getOnClause() {
        return onClause;
    }

    public ResponseBlock getResponse() {
        return responseBlock;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        Value nameValue = name.execute(ctx, policyAuthor);

        EventSubject subject;
        if (subjectClause.type == SubjectType.USER || subjectClause.type == SubjectType.USERS) {
            List<String> subjectValues = new ArrayList<>();
            for (Expression expr : subjectClause.exprs) {
                subjectValues.add(expr.execute(ctx, policyAuthor).getStringValue());
            }
            subject = EventSubject.users(subjectValues.toArray(new String[]{}));
        } else if (subjectClause.type == SubjectType.ANY_USER) {
            subject = EventSubject.anyUser();
        } else if (subjectClause.type == SubjectType.USER_ATTR) {
            subject = EventSubject.anyUserWithAttribute(
                    subjectClause.exprs.get(0).execute(ctx, policyAuthor).getStringValue()
            );
        } else {
            // process
            subject = EventSubject.process(
                    subjectClause.exprs.get(0).execute(ctx, policyAuthor).getStringValue()
            );
        }

        Performs performs;
        Value performsValue = performsClause.events.execute(ctx, policyAuthor);
        if (performsValue.isString()) {
            performs = Performs.events(performsValue.getStringValue());
        } else {
            List<String> events = new ArrayList<>();
            Value[] arrayValue = performsValue.getArrayValue();
            for (Value value : arrayValue) {
                events.add(value.getStringValue());
            }
            performs = Performs.events(events.toArray(new String[]{}));
        }

        Target target = Target.anyPolicyElement();
        Value onValue = onClause.expr.execute(ctx, policyAuthor);
        if (onValue.isString()) {
            // with POLICY_ELEMENT or CONTAINED_IN
            if (onClause.isPolicyElement()) {
                target = Target.policyElement(onValue.getStringValue());
            } else {
                target = Target.anyContainedIn(onValue.getStringValue());
            }
        } else if (onValue.isArray()) {
            // ANY_OF_SET
            Value[] values = onValue.getArrayValue();
            List<String> policyElements = new ArrayList<>();
            for (Value value : values) {
                policyElements.add(value.getStringValue());
            }

            target = Target.anyOfSet(policyElements.toArray(String[]::new));
        }

        Rule rule = new Rule(
                nameValue.getStringValue(),
                new EventPattern(
                        subject,
                        performs,
                        target
                ),
                new Response(responseBlock.evtCtxVar, ctx.copy(), responseBlock.getResponseBlock())
        );

        return new Value(rule);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateRuleStatement that = (CreateRuleStatement) o;
        return Objects.equals(name, that.name) && Objects.equals(subjectClause, that.subjectClause) && Objects.equals(performsClause, that.performsClause) && Objects.equals(onClause, that.onClause) && Objects.equals(responseBlock, that.responseBlock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subjectClause, performsClause, onClause, responseBlock);
    }

    public enum SubjectType {
        ANY_USER,
        USER,
        USERS,
        USER_ATTR,
        PROCESS
    }

    public static class SubjectClause {
        private SubjectType type;
        private List<Expression> exprs;

        public SubjectClause() {}

        public SubjectClause(SubjectType type, Expression ... exprs) {
            this.type = type;
            this.exprs = Arrays.asList(exprs);
        }

        public SubjectType getType() {
            return type;
        }

        public List<Expression> getExprs() {
            return exprs;
        }
    }

    public static class PerformsClause {
        private final Expression events;

        public PerformsClause(Expression events) {
            this.events = events;
        }

        public Expression getEvents() {
            return events;
        }
    }

    public enum TargetType {
        ANY_POLICY_ELEMENT, ANY_CONTAINED_IN, ANY_OF_SET, POLICY_ELEMENT

    }

    public static class OnClause {

        private final Expression expr;
        private final TargetType onClauseType;

        public OnClause(Expression expr, TargetType onClauseType) {
            this.expr = expr;
            this.onClauseType = onClauseType;
        }

        public boolean isPolicyElement() {
            return onClauseType == TargetType.POLICY_ELEMENT;
        }

        public boolean isAnyPolicyElement() {
            return onClauseType == TargetType.ANY_POLICY_ELEMENT;
        }

        public boolean isAnyContainedIn() {
            return onClauseType == TargetType.ANY_CONTAINED_IN;
        }

        public boolean isAnyOfSet() {
            return onClauseType == TargetType.ANY_OF_SET;
        }
    }

    public static class ResponseBlock {
        private final String evtCtxVar;
        private final List<PALStatement> responseBlock;

        public ResponseBlock(String evtCtxVar, List<PALStatement> responseBlock) {
            this.evtCtxVar = evtCtxVar;
            this.responseBlock = responseBlock;
        }

        public String getEvtCtxVar() {
            return evtCtxVar;
        }

        public List<PALStatement> getResponseBlock() {
            return responseBlock;
        }
    }
}
