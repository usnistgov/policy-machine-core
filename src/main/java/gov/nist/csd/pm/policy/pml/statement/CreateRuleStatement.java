package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.model.obligation.event.*;
import gov.nist.csd.pm.policy.model.obligation.event.subject.*;
import gov.nist.csd.pm.policy.model.obligation.event.target.*;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Response;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.pml.value.ArrayValue;
import gov.nist.csd.pm.policy.pml.value.RuleValue;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CreateRuleStatement extends PMLStatement {

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
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        StringValue nameValue = (StringValue) name.execute(ctx, policy);

        Subject subject = executeEventSubject(ctx, policy);
        Performs performs = executePerforms(ctx, policy);
        Target target = executeTarget(ctx, policy);

        ExecutionContext ruleCtx = ctx.copy();

        Rule rule = new Rule(
                nameValue.getValue(),
                new EventPattern(
                        subject,
                        performs,
                        target
                ),
                new Response(responseBlock.evtVar, ruleCtx, responseBlock.getStatements())
        );

        return new RuleValue(rule);
    }

    private Subject executeEventSubject(ExecutionContext ctx, Policy policy) throws PMException {
        switch (subjectClause.type) {
            case ANY_USER -> {
                return new AnyUserSubject();
            }
            case USERS -> {
                return new UsersSubject(getListFromExpression(ctx, policy, subjectClause.expr));
            }
            case USERS_IN_UNION -> {
                return new UsersInUnionSubject(getListFromExpression(ctx, policy, subjectClause.expr));
            }
            case USERS_IN_INTERSECTION -> {
                return new UsersInIntersectionSubject(getListFromExpression(ctx, policy, subjectClause.expr));
            }
            default  -> {
                return new ProcessesSubject(getListFromExpression(ctx, policy, subjectClause.expr));
            }
        }
    }

    private List<String> getListFromExpression(ExecutionContext ctx, Policy policy, Expression expr) throws PMException {
        List<Value> arrayValue = expr.execute(ctx, policy).getArrayValue();
        List<String> s = new ArrayList<>();
        for (Value tv : arrayValue) {
            s.add(tv.getStringValue());
        }

        return s;
    }

    private Performs executePerforms(ExecutionContext ctx, Policy policy) throws PMException {
        Value performsValue = performsClause.events.execute(ctx, policy);

        List<String> events = new ArrayList<>();
        List<Value> arrayValue = performsValue.to(ArrayValue.class).getValue();
        for (Value value : arrayValue) {
            events.add(value.to(StringValue.class).getValue());
        }

        return Performs.events(events.toArray(new String[]{}));
    }

    private Target executeTarget(ExecutionContext ctx, Policy policy) throws PMException {
        if (onClause == null || onClause.targets == null) {
            return new AnyTarget();
        }

        List<Value> targetValues = onClause.targets.execute(ctx, policy).getArrayValue();
        List<String> targetStrs = new ArrayList<>();
        for (Value v : targetValues) {
            targetStrs.add(v.getStringValue());
        }

        switch (onClause.onClauseType) {
            case ANY_TARGET -> {
                return new AnyTarget();
            }
            case ANY_IN_UNION -> {
                return new AnyInUnionTarget(targetStrs);
            }
            case ANY_IN_INTERSECTION -> {
                return new AnyInIntersectionTarget(targetStrs);
            }
            case ON_TARGETS -> {
                return new OnTargets(targetStrs);
            }
        }

        return new AnyTarget();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        PMLStatementBlock block = new PMLStatementBlock(responseBlock.statements);

        String indent = indent(indentLevel);

        return String.format(
                """
                %screate rule %s
                %s%s
                %s%s
                %s
                %sdo (%s) %s""",
                indent, name,
                indent, subjectClause,
                indent, performsClause,
                onClause == null ? "" : indent + onClause,
                indent, responseBlock.evtVar, block.toFormattedString(indentLevel)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateRuleStatement that = (CreateRuleStatement) o;
        return Objects.equals(name, that.name) && Objects.equals(
                subjectClause, that.subjectClause) && Objects.equals(
                performsClause, that.performsClause) && Objects.equals(
                onClause, that.onClause) && Objects.equals(responseBlock, that.responseBlock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subjectClause, performsClause, onClause, responseBlock);
    }

    public enum SubjectType implements Serializable {
        ANY_USER,
        USERS,
        USERS_IN_UNION,
        USERS_IN_INTERSECTION,
        PROCESSES
    }

    public static class SubjectClause implements Serializable {
        private SubjectType type;
        private Expression expr;

        public SubjectClause(SubjectType type, Expression expr) {
            this.type = type;
            this.expr = expr;
        }

        public SubjectType getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SubjectClause that = (SubjectClause) o;
            return type == that.type && Objects.equals(expr, that.expr);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, expr);
        }

        @Override
        public String toString() {
            String s = "when ";
            switch (type) {
                case ANY_USER -> s += "any user";
                case USERS -> s += "users " + expr;
                case USERS_IN_INTERSECTION -> s += "users in intersection of " + expr;
                case USERS_IN_UNION -> s += "users in union of " + expr;
                case PROCESSES -> s += "processes " + expr;
            }

            return s;
        }
    }

    public static class PerformsClause implements Serializable {
        private final Expression events;

        public PerformsClause(Expression events) {
            this.events = events;
        }

        public Expression getEvents() {
            return events;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PerformsClause that = (PerformsClause) o;
            return Objects.equals(events, that.events);
        }

        @Override
        public int hashCode() {
            return Objects.hash(events);
        }

        @Override
        public String toString() {
            return "performs " + events.toString();
        }

        public record Event(String eventName, String alias) {
            @Override
            public String toString() {
                return String.format("%s%s", eventName, alias == null || alias.isEmpty() ? "" : "as " + alias);
            }
        }
    }

    public enum TargetType {
        ANY_TARGET,
        ANY_IN_UNION,
        ANY_IN_INTERSECTION,
        ON_TARGETS

    }

    public static class OnClause implements Serializable {

        private final Expression targets;
        private final TargetType onClauseType;

        public OnClause(Expression targets, TargetType onClauseType) {
            this.targets = targets;
            this.onClauseType = onClauseType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            OnClause onClause = (OnClause) o;
            return Objects.equals(targets, onClause.targets) && onClauseType == onClause.onClauseType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(targets, onClauseType);
        }

        @Override
        public String toString() {
            if (onClauseType == null) {
                return "";
            }

            String s = "on ";
            switch (onClauseType) {
                case ON_TARGETS -> s += targets;
                case ANY_TARGET -> s += "any";
                case ANY_IN_UNION -> s += "union of " + targets;
                case ANY_IN_INTERSECTION -> s += "intersection of " + targets;
            }

            return s;
        }
    }

    public static class ResponseBlock implements Serializable {
        private final String evtVar;
        private final List<PMLStatement> statements;

        public ResponseBlock(String evtVar, List<PMLStatement> statements) {
            this.evtVar = evtVar;
            this.statements = statements;
        }

        public String getEvtVar() {
            return evtVar;
        }

        public List<PMLStatement> getStatements() {
            return statements;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ResponseBlock that = (ResponseBlock) o;
            return Objects.equals(evtVar, that.evtVar) && Objects.equals(statements, that.statements);
        }

        @Override
        public int hashCode() {
            return Objects.hash(evtVar, statements);
        }
    }
}