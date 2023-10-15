package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.model.obligation.event.*;
import gov.nist.csd.pm.policy.model.obligation.event.subject.*;
import gov.nist.csd.pm.policy.model.obligation.event.target.*;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.pml.type.Type;

import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

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

    public Expression getName() {
        return name;
    }

    public List<CreateRuleStatement> getRuleStmts() {
        return ruleStmts;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        UserContext author = ctx.author();
        String nameStr = name.execute(ctx, policy).getStringValue();

        // execute the create rule statements and add to obligation
        List<Rule> rules = new ArrayList<>();
        for (CreateRuleStatement createRuleStmt : ruleStmts) {
            Rule rule = createRuleStmt.execute(ctx, policy).getRuleValue();
            rules.add(rule);
        }

        policy.obligations().create(author, nameStr, rules.toArray(rules.toArray(Rule[]::new)));

        return new VoidValue();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        for (CreateRuleStatement createRuleStatement : ruleStmts) {
            sb.append(createRuleStatement.toFormattedString(indentLevel+1)).append("\n");
        }

        String indent = indent(indentLevel);
        return String.format(
                """
                %screate obligation %s {
                %s%s}""", indent, name, sb, indent);
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
                new StringLiteral(obligation.getName()),
                createRuleStatementsFromObligation(obligation.getRules())
        );
    }

    private static List<CreateRuleStatement> createRuleStatementsFromObligation(List<Rule> rules) {
        List<CreateRuleStatement> createRuleStatements = new ArrayList<>();

        for (Rule rule : rules) {
            EventPattern event = rule.getEventPattern();

            CreateRuleStatement createRuleStatement = new CreateRuleStatement(
                    new StringLiteral(rule.getName()),
                    getSubjectClause(event.getSubject()),
                    getPerformsClause(event.getOperations()),
                    getOnClause(event.getTarget()),
                    new CreateRuleStatement.ResponseBlock(
                            rule.getResponse().getEventCtxVariable(),
                            rule.getResponse().getStatements()
                    )
            );

            createRuleStatements.add(createRuleStatement);
        }

        return createRuleStatements;
    }

    private static CreateRuleStatement.OnClause getOnClause(Target target) {
        CreateRuleStatement.TargetType type;

        if (target instanceof AnyTarget) {
            type = CreateRuleStatement.TargetType.ANY_TARGET;

        } else if (target instanceof AnyInUnionTarget) {
            type = CreateRuleStatement.TargetType.ANY_IN_UNION;

        } else if (target instanceof AnyInIntersectionTarget) {
            type = CreateRuleStatement.TargetType.ANY_IN_INTERSECTION;

        } else {
            type = CreateRuleStatement.TargetType.ON_TARGETS;

        }

        ArrayLiteral arrayLiteral = new ArrayLiteral(Type.string());
        for (String user : target.getTargets()) {
            arrayLiteral.add(new StringLiteral(user));
        }

        return new CreateRuleStatement.OnClause(arrayLiteral, type);
    }

    private static CreateRuleStatement.PerformsClause getPerformsClause(List<String> operations) {
        ArrayLiteral arrayLiteral = new ArrayLiteral(Type.string());
        for (String op : operations) {
            arrayLiteral.add(new StringLiteral(op));
        }
        return new CreateRuleStatement.PerformsClause(arrayLiteral);
    }

    private static CreateRuleStatement.SubjectClause getSubjectClause(Subject subject) {
        CreateRuleStatement.SubjectType type;

        if (subject instanceof AnyUserSubject) {
            type = CreateRuleStatement.SubjectType.ANY_USER;

        } else if (subject instanceof UsersSubject) {
            type = CreateRuleStatement.SubjectType.USERS;

        } else if (subject instanceof UsersInUnionSubject) {
            type = CreateRuleStatement.SubjectType.USERS_IN_UNION;

        } else if (subject instanceof UsersInIntersectionSubject) {
            type = CreateRuleStatement.SubjectType.USERS_IN_INTERSECTION;

        } else {
            type = CreateRuleStatement.SubjectType.PROCESSES;

        }

        ArrayLiteral arrayLiteral = new ArrayLiteral(Type.string());
        for (String user : subject.getSubjects()) {
            arrayLiteral.add(new StringLiteral(user));
        }

        return new CreateRuleStatement.SubjectClause(type, arrayLiteral);
    }
}
