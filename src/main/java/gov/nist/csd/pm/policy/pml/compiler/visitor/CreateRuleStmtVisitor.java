package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.VariableAlreadyDefinedInScopeException;
import gov.nist.csd.pm.policy.pml.statement.CreateRuleStatement;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.ArrayList;
import java.util.List;

public class CreateRuleStmtVisitor extends PMLBaseVisitor<CreateRuleStatement> {

    public CreateRuleStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreateRuleStatement visitCreateRuleStatement(PMLParser.CreateRuleStatementContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.ruleName, Type.string());

        CreateRuleStatement.SubjectClause subjectClause = getSubjectClause(ctx.subjectClause());
        CreateRuleStatement.PerformsClause performsClause = getPerformsClause(ctx.performsClause);
        CreateRuleStatement.OnClause onClause = getOnClause(ctx.onClause());
        CreateRuleStatement.ResponseBlock responseBlock;
        try {
            responseBlock = getResponse(ctx.response());
        } catch (VariableAlreadyDefinedInScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());

            return new CreateRuleStatement(ctx);
        }

        return new CreateRuleStatement(name, subjectClause, performsClause, onClause, responseBlock);
    }

    private CreateRuleStatement.ResponseBlock getResponse(PMLParser.ResponseContext ctx) throws VariableAlreadyDefinedInScopeException {
        String evtVar = ctx.ID().getText();

        // create a new local parser scope for the response block
        // add the event name and event context map to the local parser scope
        VisitorContext localVisitorCtx = visitorCtx.copy();
        localVisitorCtx.scope().addVariable(evtVar, new Variable(evtVar, Type.map(Type.string(), Type.any()), true));

        PMLParser.ResponseBlockContext responseBlockCtx = ctx.responseBlock();
        List<PMLParser.ResponseStatementContext> responseStmtsCtx = responseBlockCtx.responseStatement();

        StatementVisitor statementVisitor = new StatementVisitor(localVisitorCtx);
        CreateRuleStmtVisitor createRuleStmtVisitor = new CreateRuleStmtVisitor(localVisitorCtx);
        DeleteRuleStmtVisitor deleteRuleStmtVisitor = new DeleteRuleStmtVisitor(localVisitorCtx);

        List<PMLStatement> stmts = new ArrayList<>();
        for (PMLParser.ResponseStatementContext responseStmtCtx : responseStmtsCtx) {
            PMLStatement stmt = null;

            if (responseStmtCtx.statement() != null) {
                stmt = statementVisitor.visitStatement(responseStmtCtx.statement());
            } else if (responseStmtCtx.createRuleStatement() != null) {
                stmt = createRuleStmtVisitor.visitCreateRuleStatement(responseStmtCtx.createRuleStatement());
            } else if (responseStmtCtx.deleteRuleStatement() != null) {
                stmt = deleteRuleStmtVisitor.visitDeleteRuleStatement(responseStmtCtx.deleteRuleStatement());
            }

            if (stmt instanceof FunctionDefinitionStatement) {
                visitorCtx.errorLog().addError(responseStmtCtx, "functions are not allowed inside response blocks");
            }

            stmts.add(stmt);
        }

        return new CreateRuleStatement.ResponseBlock(evtVar, stmts);
    }

    private CreateRuleStatement.OnClause getOnClause(PMLParser.OnClauseContext onClauseCtx) {
        Expression targets = null;
        CreateRuleStatement.TargetType onClauseType = CreateRuleStatement.TargetType.ANY_TARGET;
        if (onClauseCtx instanceof PMLParser.AnyInUnionTargetContext anyInUnionTargetContext) {
            targets = Expression.compile(visitorCtx, anyInUnionTargetContext.expression(), Type.array(Type.string()));
            onClauseType = CreateRuleStatement.TargetType.ANY_IN_UNION;

        } else if (onClauseCtx instanceof PMLParser.AnyInIntersectionTargetContext anyInIntersectionTargetContext) {
            targets = Expression.compile(visitorCtx, anyInIntersectionTargetContext.expression(), Type.array(Type.string()));
            onClauseType = CreateRuleStatement.TargetType.ANY_IN_INTERSECTION;

        } else if (onClauseCtx instanceof PMLParser.OnTargetsContext onTargetsContext) {
            targets = Expression.compile(visitorCtx, onTargetsContext.expression(), Type.array(Type.string()));
            onClauseType = CreateRuleStatement.TargetType.ON_TARGETS;

        }

        return new CreateRuleStatement.OnClause(targets, onClauseType);
    }

    private CreateRuleStatement.PerformsClause getPerformsClause(PMLParser.ExpressionContext performsClause) {
        Expression expression = Expression.compile(visitorCtx, performsClause, Type.array(Type.string()));
        return new CreateRuleStatement.PerformsClause(expression);
    }

    private CreateRuleStatement.SubjectClause getSubjectClause(PMLParser.SubjectClauseContext ctx) {
        CreateRuleStatement.SubjectType type = CreateRuleStatement.SubjectType.ANY_USER;
        Expression expr = null;

        if (ctx instanceof PMLParser.UsersSubjectContext usersSubjectContext) {
            type = CreateRuleStatement.SubjectType.USERS;
            expr = Expression.compile(visitorCtx, usersSubjectContext.expression(), Type.array(Type.string()));

        } else if (ctx instanceof PMLParser.UsersInUnionSubjectContext usersInUnionSubjectContext){
            type = CreateRuleStatement.SubjectType.USERS_IN_UNION;
            expr = Expression.compile(visitorCtx, usersInUnionSubjectContext.expression(), Type.array(Type.string()));

        } else if (ctx instanceof PMLParser.UsersInIntersectionSubjectContext usersInIntersectionSubjectContext) {
            type = CreateRuleStatement.SubjectType.USERS_IN_INTERSECTION;
            expr = Expression.compile(visitorCtx, usersInIntersectionSubjectContext.expression(), Type.array(Type.string()));

        } else if (ctx instanceof PMLParser.ProcessesSubjectContext processesSubjectContext) {
            type = CreateRuleStatement.SubjectType.PROCESSES;
            expr = Expression.compile(visitorCtx, processesSubjectContext.expression(), Type.array(Type.string()));

        }

        return new CreateRuleStatement.SubjectClause(type, expr);
    }
}
