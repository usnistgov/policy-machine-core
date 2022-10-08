package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.exception.PALExecutionException;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.scope.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.Performs;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static gov.nist.csd.pm.policy.author.pal.PALFormatter.statementsToString;

public class CreateRuleStatement extends PALStatement {

    private final NameExpression name;
    private final SubjectClause subjectClause;
    private final PerformsClause performsClause;
    private final OnClause onClause;
    private final ResponseBlock responseBlock;

    public CreateRuleStatement(NameExpression name, SubjectClause subjectClause,
                               PerformsClause performsClause, OnClause onClause, ResponseBlock responseBlock) {
        this.name = name;
        this.subjectClause = subjectClause;
        this.performsClause = performsClause;
        this.onClause = onClause;
        this.responseBlock = responseBlock;
    }

    public NameExpression getName() {
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
            subjectValues.add(subjectClause.expr.execute(ctx, policyAuthor).getStringValue());
            subject = EventSubject.users(subjectValues.toArray(new String[]{}));
        } else if (subjectClause.type == SubjectType.ANY_USER) {
            subject = EventSubject.anyUser();
        } else if (subjectClause.type == SubjectType.USER_ATTR) {
            subject = EventSubject.anyUserWithAttribute(
                    subjectClause.expr.execute(ctx, policyAuthor).getStringValue()
            );
        } else {
            // process
            subject = EventSubject.process(
                    subjectClause.expr.execute(ctx, policyAuthor).getStringValue()
            );
        }

        Performs performs = Performs.events(performsClause.events.toArray(new String[]{}));

        Target target = Target.anyPolicyElement();
        Value onValue;
        if (onClause.nameExpr != null) {
            onValue = onClause.nameExpr.execute(ctx, policyAuthor);
        } else {
            onValue = new Value();
        }

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

        ExecutionContext ruleCtx = null;
        try {
            ruleCtx = ctx.copy();
        } catch (PALScopeException e) {
            throw new PALExecutionException(e.getMessage());
        }

        Rule rule = new Rule(
                nameValue.getStringValue(),
                new EventPattern(
                        subject,
                        performs,
                        target
                ),
                new Response(ruleCtx, responseBlock.getStatements())
        );

        return new Value(rule);
    }

    @Override
    public String toString() {
        return String.format(
                "create rule %s %s %s %s do {%s}",
                name, subjectClause, performsClause, onClause,
                statementsToString(responseBlock.statements)
        );
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
        private NameExpression expr;

        public SubjectClause() {
        }

        public SubjectClause(SubjectType type, NameExpression expr) {
            this.type = type;
            this.expr = expr;
        }

        public SubjectClause(SubjectType type) {
            this.type = type;
        }

        public SubjectType getType() {
            return type;
        }

        @Override
        public String toString() {
            String s = "when ";
            switch (type) {
                case ANY_USER -> s += "any user";
                case USER_ATTR -> s += "any user with attribute " + expr;
                case USERS -> s += "users " + expr;
                case USER -> s += "user " + expr;
                case PROCESS -> s += "process " + expr;
            }

            return s;
        }
    }

    public static class PerformsClause {
        private final List<String> events;

        public PerformsClause(List<String> events) {
            this.events = events;
        }

        public List<String> getEvents() {
            return events;
        }

        @Override
        public String toString() {
            StringBuilder s =  new StringBuilder("performs ");
            StringBuilder eventsStr = new StringBuilder();
            for (String event : events) {
                if (!eventsStr.isEmpty()) {
                    eventsStr.append(", ");
                }
                eventsStr.append(event);
            }
            return s.append(eventsStr).toString();
        }

        public record Event(String eventName, String alias) {
            @Override
            public String toString() {
                return String.format("%s%s", eventName, alias == null || alias.isEmpty() ? "" : "as " + alias);
            }
        }
    }

    public enum TargetType {
        ANY_POLICY_ELEMENT, ANY_CONTAINED_IN, ANY_OF_SET, POLICY_ELEMENT

    }

    public static class OnClause {

        private final NameExpression nameExpr;
        private final TargetType onClauseType;

        public OnClause() {
            nameExpr = null;
            onClauseType = null;
        }

        public OnClause(NameExpression nameExpr, TargetType onClauseType) {
            this.nameExpr = nameExpr;
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

        @Override
        public String toString() {
            if (onClauseType == null) {
                return "";
            }

            String s = "on ";
            switch (onClauseType) {
                case POLICY_ELEMENT -> s += nameExpr;
                case ANY_POLICY_ELEMENT -> s += "any policy element";
                case ANY_CONTAINED_IN -> s += "any policy element in " + nameExpr;
                case ANY_OF_SET -> s += "any policy element of " + nameExpr;
            }

            return s;
        }
    }

    public static class ResponseBlock {
        private final List<PALStatement> statements;

        public ResponseBlock() {
            this.statements = new ArrayList<>();
        }

        public ResponseBlock(List<PALStatement> statements) {
            this.statements = statements;
        }

        public List<PALStatement> getStatements() {
            return statements;
        }
    }
}