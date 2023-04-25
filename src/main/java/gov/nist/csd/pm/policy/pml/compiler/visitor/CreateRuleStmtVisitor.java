package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.CreateRuleStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;

import java.util.ArrayList;
import java.util.List;

public class CreateRuleStmtVisitor extends PMLBaseVisitor<CreateRuleStatement> {

    private final VisitorContext visitorCtx;

    public CreateRuleStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public CreateRuleStatement visitCreateRuleStmt(PMLParser.CreateRuleStmtContext ctx) {
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

    private CreateRuleStatement.ResponseBlock getResponse(PMLParser.ResponseContext ctx) throws VariableAlreadyDefinedInScopeException {
        String evtVar = ctx.VARIABLE_OR_FUNCTION_NAME().getText();

        // create a new local parser scope for the response block
        // add the event name and event context map to the local parser scope
        VisitorContext localVisitorCtx = visitorCtx.copy();
        localVisitorCtx.scope().addVariable(evtVar, Type.map(Type.string(), Type.any()), true);

        PMLParser.ResponseBlockContext responseBlockCtx = ctx.responseBlock();
        List<PMLParser.ResponseStmtContext> responseStmtsCtx = responseBlockCtx.responseStmt();

        List<PMLStatement> stmts = new ArrayList<>();
        for (PMLParser.ResponseStmtContext responseStmtCtx : responseStmtsCtx) {
            PMLStatement stmt = null;

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

    private CreateRuleStatement.OnClause getOnClause(PMLParser.OnClauseContext onClauseCtx) {
        Expression name = null;
        CreateRuleStatement.TargetType onClauseType = null;
        if (onClauseCtx instanceof PMLParser.PolicyElementContext policyElementContext) {
            name = Expression.compile(visitorCtx, policyElementContext.expression(), Type.string());
            onClauseType = CreateRuleStatement.TargetType.POLICY_ELEMENT;
        } else if (onClauseCtx instanceof PMLParser.AnyPolicyElementContext) {
            onClauseType = CreateRuleStatement.TargetType.ANY_POLICY_ELEMENT;
        } else if (onClauseCtx instanceof PMLParser.AnyContainedInContext anyContainedInContext) {
            name = Expression.compile(visitorCtx, anyContainedInContext.expression(), Type.string());
            onClauseType = CreateRuleStatement.TargetType.ANY_CONTAINED_IN;
        } else if (onClauseCtx instanceof PMLParser.AnyOfSetContext anyOfSetContext) {
            name = Expression.compile(visitorCtx, anyOfSetContext.expression(), Type.array(Type.string()));
            onClauseType = CreateRuleStatement.TargetType.ANY_OF_SET;
        } else {
            onClauseType = CreateRuleStatement.TargetType.ANY_POLICY_ELEMENT;
        }

        return new CreateRuleStatement.OnClause(name, onClauseType);
    }

    private CreateRuleStatement.PerformsClause getPerformsClause(PMLParser.ExpressionContext performsClause) {
        Expression expression = Expression.compile(visitorCtx, performsClause, Type.array(Type.string()));
        return new CreateRuleStatement.PerformsClause(expression);
    }

    private CreateRuleStatement.SubjectClause getSubjectClause(PMLParser.SubjectClauseContext ctx) {
        CreateRuleStatement.SubjectType type;
        Expression expr;

        if (ctx instanceof PMLParser.AnyUserSubjectContext) {
            type = CreateRuleStatement.SubjectType.ANY_USER;
            return new CreateRuleStatement.SubjectClause(type);
        } else if (ctx instanceof PMLParser.UserSubjectContext userSubjectCtx) {
            type = CreateRuleStatement.SubjectType.USER;
            expr = Expression.compile(visitorCtx, userSubjectCtx.user, Type.string());

        } else if (ctx instanceof PMLParser.UsersListSubjectContext usersListSubjectCtx) {
            type = CreateRuleStatement.SubjectType.USERS;
            expr = Expression.compile(visitorCtx, usersListSubjectCtx.users, Type.array(Type.string()));

        } else if (ctx instanceof PMLParser.UserAttrSubjectContext userAttrSubjectCtx) {
            type = CreateRuleStatement.SubjectType.USER;
            expr = Expression.compile(visitorCtx, userAttrSubjectCtx.attribute, Type.string());

        } else {
            type = CreateRuleStatement.SubjectType.USER;
            expr = Expression.compile(visitorCtx, ((PMLParser.ProcessSubjectContext)ctx).process, Type.string());
        }

        return new CreateRuleStatement.SubjectClause(type, expr);
    }
}
