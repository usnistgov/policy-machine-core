package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.ArrayLiteral;
import gov.nist.csd.pm.policy.author.pal.model.expression.Literal;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.model.obligation.event.EventPattern;
import gov.nist.csd.pm.policy.model.obligation.event.EventSubject;
import gov.nist.csd.pm.policy.model.obligation.event.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static gov.nist.csd.pm.policy.author.pal.PALFormatter.statementsToString;

public class CreateObligationStatement extends PALStatement {

    private final Expression labelExpr;
    private final List<CreateRuleStatement> ruleStmts;

    public CreateObligationStatement(Expression labelExpr, List<CreateRuleStatement> ruleStmts) {
        this.labelExpr = labelExpr;
        this.ruleStmts = ruleStmts;
    }

    public Expression getLabelExpr() {
        return labelExpr;
    }

    public List<CreateRuleStatement> getRuleStmts() {
        return ruleStmts;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        UserContext author = ctx.getAuthor();
        String label = labelExpr.execute(ctx, policyAuthor).getStringValue();

        // execute the create rule statements and add to obligation
        List<Rule> rules = new ArrayList<>();
        for (CreateRuleStatement createRuleStmt : ruleStmts) {
            Value createRuleValue = createRuleStmt.execute(ctx, policyAuthor);
            Rule rule = createRuleValue.getRule();
            rules.add(rule);
        }

        policyAuthor.obligations().create(author, label, rules.toArray(rules.toArray(Rule[]::new)));

        return new Value();
    }

    @Override
    public String toString() {
        return String.format("create obligation %s {%s}", labelExpr, statementsToString(ruleStmts));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateObligationStatement that = (CreateObligationStatement) o;
        return Objects.equals(labelExpr, that.labelExpr) && Objects.equals(ruleStmts, that.ruleStmts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labelExpr, ruleStmts);
    }

    public static CreateObligationStatement fromObligation(Obligation obligation) {
        return new CreateObligationStatement(
                new Expression(new Literal(obligation.getLabel())),
                createRuleStatementsFromObligation(obligation.getRules())
        );
    }

    private static List<CreateRuleStatement> createRuleStatementsFromObligation(List<Rule> rules) {
        List<CreateRuleStatement> createRuleStatements = new ArrayList<>();

        for (Rule rule : rules) {
            EventPattern event = rule.getEvent();

            createRuleStatements.add(
                    new CreateRuleStatement(
                            new Expression(new Literal(rule.getLabel())),
                            getSubjectClause(event),
                            getPerformsClause(event),
                            getOnClause(event),
                            new CreateRuleStatement.ResponseBlock(
                                    rule.getResponse().getEventCtxVariable(),
                                    rule.getResponse().getStatements()
                            )
                    )
            );
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
                List<Expression> exprs = new ArrayList<>();
                for (String s : set) {
                    exprs.add(new Expression(new Literal(s)));
                }

                expression = new Expression(new Literal(new ArrayLiteral(exprs.toArray(Expression[]::new), Type.string())));
                onClauseType = CreateRuleStatement.TargetType.ANY_OF_SET;
            }
            case ANY_POLICY_ELEMENT -> {
                onClauseType = CreateRuleStatement.TargetType.ANY_POLICY_ELEMENT;
            }
        }

        return new CreateRuleStatement.OnClause(expression, onClauseType);
    }

    private static CreateRuleStatement.PerformsClause getPerformsClause(EventPattern event) {
        List<String> operations = event.getOperations();
        List<Expression> exprs = new ArrayList<>();
        for (String op : operations) {
            exprs.add(new Expression(new Literal(op)));
        }
        return new CreateRuleStatement.PerformsClause(
                new Expression(new Literal(new ArrayLiteral(exprs.toArray(Expression[]::new), Type.string()))));
    }

    private static CreateRuleStatement.SubjectClause getSubjectClause(EventPattern event) {
        EventSubject subject = event.getSubject();
        EventSubject.Type eventSubjectType = subject.getType();
        CreateRuleStatement.SubjectType type = null;
        Expression subjectExpr = null;
        CreateRuleStatement.SubjectClause subjectClause = null;
        switch (eventSubjectType) {
            case USERS -> {
                if (subject.users().size() > 1) {
                    type = CreateRuleStatement.SubjectType.USERS;

                    List<Expression> userExprs = new ArrayList<>();
                    for (String user : subject.users()) {
                        userExprs.add(new Expression(new Literal(user)));
                    }

                    subjectExpr = new Expression(new Literal(new ArrayLiteral(
                            userExprs.toArray(new Expression[]{}),
                            Type.string()
                    )));
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
            case ANY_USER -> {
                type = CreateRuleStatement.SubjectType.ANY_USER;
            }
        }

        if (subjectExpr == null) {
            subjectClause = new CreateRuleStatement.SubjectClause(type);
        } else {
            subjectClause = new CreateRuleStatement.SubjectClause(type, subjectExpr);
        }

        return subjectClause;
    }
}
