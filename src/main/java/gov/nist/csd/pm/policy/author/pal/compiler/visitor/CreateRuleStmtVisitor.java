package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.compiler.Position;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.author.pal.statement.NameExpression;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.CreateRuleStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;

import java.util.ArrayList;
import java.util.List;

public class CreateRuleStmtVisitor extends PALBaseVisitor<CreateRuleStatement> {

    private final VisitorContext visitorCtx;

    public CreateRuleStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public CreateRuleStatement visitCreateRuleStmt(PALParser.CreateRuleStmtContext ctx) {
        NameExpression name = NameExpression.compile(visitorCtx, ctx.ruleName);

        CreateRuleStatement.SubjectClause subjectClause = getSubjectClause(ctx.subjectClause());
        CreateRuleStatement.PerformsClause performsClause = getPerformsClause(ctx.performsClause);
        CreateRuleStatement.OnClause onClause = getOnClause(ctx.onClause());
        CreateRuleStatement.ResponseBlock responseBlock = new CreateRuleStatement.ResponseBlock();
        try {
            responseBlock = getResponse(ctx.response());
        } catch (VariableAlreadyDefinedInScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());
        }

        return new CreateRuleStatement(name, subjectClause, performsClause, onClause, responseBlock);
    }

    private CreateRuleStatement.ResponseBlock getResponse(PALParser.ResponseContext ctx) throws VariableAlreadyDefinedInScopeException {
        String evtCtxVar = ctx.IDENTIFIER().getText();

        // create a new local parser scope for the response block
        // add the event name and event context map to the local parser scope
        VisitorContext localVisitorCtx = visitorCtx.copy();
        localVisitorCtx.scope().addVariable(evtCtxVar, Type.map(Type.string(), Type.any()), false);

        PALParser.ResponseBlockContext responseBlockCtx = ctx.responseBlock();
        PALParser.ResponseStmtsContext responseStmtsCtx = responseBlockCtx.responseStmts();

        List<PALStatement> stmts = new ArrayList<>();
        for (PALParser.ResponseStmtContext responseStmtCtx : responseStmtsCtx.responseStmt()) {
            PALStatement stmt = null;
            if (responseStmtCtx.stmt() != null) {
                stmt = new StatementVisitor(localVisitorCtx)
                        .visitStmt(responseStmtCtx.stmt());
            } else if (responseStmtCtx.createRuleStmt() != null) {
                stmt = new CreateRuleStmtVisitor(localVisitorCtx)
                        .visitCreateRuleStmt(responseStmtCtx.createRuleStmt());
            }

            stmts.add(stmt);
        }

        return new CreateRuleStatement.ResponseBlock(evtCtxVar, stmts);
    }

    private CreateRuleStatement.OnClause getOnClause(PALParser.OnClauseContext onClauseCtx) {
        NameExpression name = null;
        CreateRuleStatement.TargetType onClauseType = null;
        if (onClauseCtx instanceof PALParser.PolicyElementContext policyElementContext) {
            name = NameExpression.compile(visitorCtx, policyElementContext.nameExpression());
            onClauseType = CreateRuleStatement.TargetType.POLICY_ELEMENT;
        } else if (onClauseCtx instanceof PALParser.AnyPolicyElementContext anyPolicyElementContext) {
            onClauseType = CreateRuleStatement.TargetType.ANY_POLICY_ELEMENT;
        } else if (onClauseCtx instanceof PALParser.AnyContainedInContext anyContainedInContext) {
            name = NameExpression.compile(visitorCtx, anyContainedInContext.nameExpression());
            onClauseType = CreateRuleStatement.TargetType.ANY_CONTAINED_IN;
        } else if (onClauseCtx instanceof PALParser.AnyOfSetContext anyOfSetContext) {
            name = NameExpression.compileArray(visitorCtx, anyOfSetContext.nameExpressionArray());
            onClauseType = CreateRuleStatement.TargetType.ANY_OF_SET;
        } else {
            onClauseType = CreateRuleStatement.TargetType.ANY_POLICY_ELEMENT;
        }

        return new CreateRuleStatement.OnClause(name, onClauseType);
    }

    private CreateRuleStatement.PerformsClause getPerformsClause(PALParser.ExpressionContext ctx) {
        Expression performsExpr = Expression.compile(visitorCtx, ctx);
        return new CreateRuleStatement.PerformsClause(performsExpr);
    }

    private CreateRuleStatement.SubjectClause getSubjectClause(PALParser.SubjectClauseContext ctx) {
        CreateRuleStatement.SubjectType type;
        NameExpression expr;

        if (ctx instanceof PALParser.AnyUserSubjectContext) {
            type = CreateRuleStatement.SubjectType.ANY_USER;
            return new CreateRuleStatement.SubjectClause(type);
        } else if (ctx instanceof PALParser.UserSubjectContext userSubjectCtx) {
            type = CreateRuleStatement.SubjectType.USER;
            expr = NameExpression.compile(visitorCtx, userSubjectCtx.user);

        } else if (ctx instanceof PALParser.UsersListSubjectContext usersListSubjectCtx) {
            type = CreateRuleStatement.SubjectType.USERS;
            expr = NameExpression.compileArray(visitorCtx, usersListSubjectCtx.users);

        } else if (ctx instanceof PALParser.UserAttrSubjectContext userAttrSubjectCtx) {
            type = CreateRuleStatement.SubjectType.USER;
            expr = NameExpression.compile(visitorCtx, userAttrSubjectCtx.attribute);

        } else {
            type = CreateRuleStatement.SubjectType.USER;
            expr = NameExpression.compile(visitorCtx, ((PALParser.ProcessSubjectContext)ctx).process);
        }

        return new CreateRuleStatement.SubjectClause(type, expr);
    }


}
