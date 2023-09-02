package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;
import gov.nist.csd.pm.policy.pml.PMLFormatter;
import gov.nist.csd.pm.policy.pml.model.expression.ArrayLiteral;
import gov.nist.csd.pm.policy.pml.model.expression.Literal;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateObligationStatement extends PMLStatement {

    private final Expression name;
    private final List<CreateRuleStatement> ruleStmts;

    public CreateObligationStatement(Expression name, List<CreateRuleStatement> ruleStmts) {
        this.name = name;
        this.ruleStmts = ruleStmts;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        UserContext author = ctx.author();
        String nameStr = name.execute(ctx, policy).getStringValue();

        // execute the create rule statements and add to obligation
        List<Rule> rules = new ArrayList<>();
        for (PMLStatement createRuleStmt : ruleStmts) {
            Value createRuleValue = createRuleStmt.execute(ctx, policy);
            Rule rule = createRuleValue.getRule();
            rules.add(rule);
        }

        policy.obligations().create(author, nameStr, rules.toArray(rules.toArray(Rule[]::new)));

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("create obligation %s {%s}", name, PMLFormatter.statementsToString(ruleStmts));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateObligationStatement that = (CreateObligationStatement) o;
        return Objects.equals(name, that.name) && Objects.equals(ruleStmts, that.ruleStmts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ruleStmts);
    }

    public static CreateObligationStatement fromObligation(Obligation obligation) {
        return new CreateObligationStatement(
                new Expression(new Literal(obligation.getName())),
                createRuleStatementsFromObligation(obligation.getRules())
        );
    }

    private static List<CreateRuleStatement> createRuleStatementsFromObligation(List<Rule> rules) {
        List<CreateRuleStatement> createRuleStatements = new ArrayList<>();

        for (Rule rule : rules) {
            EventPattern event = rule.getEventPattern();

            CreateRuleStatement createRuleStatement = new CreateRuleStatement(
                    new Expression(new Literal(rule.getName())),
                    getSubjectClause(event.getSubject()),
                    getPerformsClause(event.getOperations()),
                    getOnClause(event),
                    new CreateRuleStatement.ResponseBlock(
                            rule.getResponse().getEventCtxVariable(),
                            rule.getResponse().getStatements()
                    )
            );

            createRuleStatements.add(createRuleStatement);
        }

        return createRuleStatements;
    }

    private static CreateRuleStatement.OnClause getOnClause(EventPattern event) {
        Target target = event.getTarget();
        Target.Type type = target.getType();
        Expression expression = null;
        CreateRuleStatement.TargetType onClauseType = null;
        switch (type) {
            case ANY_CONTAINED_IN -> {
                expression = new Expression(new Literal(target.anyContainedIn()));
                onClauseType = CreateRuleStatement.TargetType.ANY_CONTAINED_IN;
            }
            case POLICY_ELEMENT -> {
                expression = new Expression(new Literal(target.policyElement()));
                onClauseType = CreateRuleStatement.TargetType.POLICY_ELEMENT;
            }
            case ANY_OF_SET -> {
                List<String> set = target.anyOfSet();
                ArrayLiteral arrayLiteral = new ArrayLiteral(Type.string());
                for (String s : set) {
                    arrayLiteral.add(new Expression(new Literal(s)));
                }

                expression = new Expression(new Literal(arrayLiteral));
                onClauseType = CreateRuleStatement.TargetType.ANY_OF_SET;
            }
            case ANY_POLICY_ELEMENT -> onClauseType = CreateRuleStatement.TargetType.ANY_POLICY_ELEMENT;
        }

        return new CreateRuleStatement.OnClause(expression, onClauseType);
    }

    private static CreateRuleStatement.PerformsClause getPerformsClause(List<String> operations) {
        ArrayLiteral arrayLiteral = new ArrayLiteral(Type.string());
        for (String op : operations) {
            arrayLiteral.add(new Expression(new Literal(op)));
        }
        return new CreateRuleStatement.PerformsClause(
                new Expression(new Literal(arrayLiteral))
        );
    }

    private static CreateRuleStatement.SubjectClause getSubjectClause(EventSubject subject) {
        EventSubject.Type eventSubjectType = subject.getType();
        CreateRuleStatement.SubjectType type = null;
        Expression subjectExpr = null;
        CreateRuleStatement.SubjectClause subjectClause = null;
        switch (eventSubjectType) {
            case USERS -> {
                if (subject.users().size() > 1) {
                    type = CreateRuleStatement.SubjectType.USERS;

                    ArrayLiteral arrayLiteral = new ArrayLiteral(Type.string());
                    for (String user : subject.users()) {
                        arrayLiteral.add(new Expression(new Literal(user)));
                    }

                    subjectExpr = new Expression(new Literal(arrayLiteral));
                } else {
                    type = CreateRuleStatement.SubjectType.USER;
                    subjectExpr = new Expression(new Literal(subject.users().get(0)));
                }
            }
            case PROCESS -> {
                type = CreateRuleStatement.SubjectType.PROCESS;
                subjectExpr = new Expression(new Literal(subject.process()));
            }
            case ANY_USER_WITH_ATTRIBUTE -> {
                type = CreateRuleStatement.SubjectType.USER_ATTR;
                subjectExpr = new Expression(new Literal(subject.anyUserWithAttribute()));
            }
            case ANY_USER -> type = CreateRuleStatement.SubjectType.ANY_USER;
        }

        if (subjectExpr == null) {
            subjectClause = new CreateRuleStatement.SubjectClause(type);
        } else {
            subjectClause = new CreateRuleStatement.SubjectClause(type, subjectExpr);
        }

        return subjectClause;
    }
}
