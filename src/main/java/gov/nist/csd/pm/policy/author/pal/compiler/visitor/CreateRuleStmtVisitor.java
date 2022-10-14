package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.scope.VariableAlreadyDefinedInScopeException;
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
        Expression name = Expression.compile(visitorCtx, ctx.ruleName, Type.string());

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
        String evtVar = ctx.VARIABLE_OR_FUNCTION_NAME().getText();

        // create a new local parser scope for the response block
        // add the event name and event context map to the local parser scope
        VisitorContext localVisitorCtx = visitorCtx.copy();
        localVisitorCtx.scope().addVariable(evtVar, Type.map(Type.string(), Type.any()), true);

        PALParser.ResponseBlockContext responseBlockCtx = ctx.responseBlock();
        List<PALParser.ResponseStmtContext> responseStmtsCtx = responseBlockCtx.responseStmt();

        List<PALStatement> stmts = new ArrayList<>();
        for (PALParser.ResponseStmtContext responseStmtCtx : responseStmtsCtx) {
            PALStatement stmt = null;

            if (responseStmtCtx.stmt() != null) {
                stmt = new StatementVisitor(localVisitorCtx)
                        .visitStmt(responseStmtCtx.stmt());
            } else if (responseStmtCtx.createRuleStmt() != null) {
                stmt = new CreateRuleStmtVisitor(localVisitorCtx)
                        .visitCreateRuleStmt(responseStmtCtx.createRuleStmt());
            } else if (responseStmtCtx.deleteRuleStmt() != null) {
                stmt = new DeleteRuleStmtVisitor(localVisitorCtx)
                        .visitDeleteRuleStmt(responseStmtCtx.deleteRuleStmt());
            }

            stmts.add(stmt);
        }

        return new CreateRuleStatement.ResponseBlock(evtVar, stmts);
    }

    private CreateRuleStatement.OnClause getOnClause(PALParser.OnClauseContext onClauseCtx) {
        Expression name = null;
        CreateRuleStatement.TargetType onClauseType = null;
        if (onClauseCtx instanceof PALParser.PolicyElementContext policyElementContext) {
            name = Expression.compile(visitorCtx, policyElementContext.expression(), Type.string());
            onClauseType = CreateRuleStatement.TargetType.POLICY_ELEMENT;
        } else if (onClauseCtx instanceof PALParser.AnyPolicyElementContext) {
            onClauseType = CreateRuleStatement.TargetType.ANY_POLICY_ELEMENT;
        } else if (onClauseCtx instanceof PALParser.AnyContainedInContext anyContainedInContext) {
            name = Expression.compile(visitorCtx, anyContainedInContext.expression(), Type.string());
            onClauseType = CreateRuleStatement.TargetType.ANY_CONTAINED_IN;
        } else if (onClauseCtx instanceof PALParser.AnyOfSetContext anyOfSetContext) {
            name = Expression.compileArray(visitorCtx, anyOfSetContext.expressionArray(), Type.string());
            onClauseType = CreateRuleStatement.TargetType.ANY_OF_SET;
        } else {
            onClauseType = CreateRuleStatement.TargetType.ANY_POLICY_ELEMENT;
        }

        return new CreateRuleStatement.OnClause(name, onClauseType);
    }

    private CreateRuleStatement.PerformsClause getPerformsClause(PALParser.ExpressionArrayContext performsClause) {
        Expression expression = Expression.compileArray(visitorCtx, performsClause, Type.string());
        return new CreateRuleStatement.PerformsClause(expression);
    }

    private CreateRuleStatement.SubjectClause getSubjectClause(PALParser.SubjectClauseContext ctx) {
        CreateRuleStatement.SubjectType type;
        Expression expr;

        if (ctx instanceof PALParser.AnyUserSubjectContext) {
            type = CreateRuleStatement.SubjectType.ANY_USER;
            return new CreateRuleStatement.SubjectClause(type);
        } else if (ctx instanceof PALParser.UserSubjectContext userSubjectCtx) {
            type = CreateRuleStatement.SubjectType.USER;
            expr = Expression.compile(visitorCtx, userSubjectCtx.user, Type.string());

        } else if (ctx instanceof PALParser.UsersListSubjectContext usersListSubjectCtx) {
            type = CreateRuleStatement.SubjectType.USERS;
            expr = Expression.compileArray(visitorCtx, usersListSubjectCtx.users, Type.string());

        } else if (ctx instanceof PALParser.UserAttrSubjectContext userAttrSubjectCtx) {
            type = CreateRuleStatement.SubjectType.USER;
            expr = Expression.compile(visitorCtx, userAttrSubjectCtx.attribute, Type.string());

        } else {
            type = CreateRuleStatement.SubjectType.USER;
            expr = Expression.compile(visitorCtx, ((PALParser.ProcessSubjectContext)ctx).process, Type.string());
        }

        return new CreateRuleStatement.SubjectClause(type, expr);
    }
}
