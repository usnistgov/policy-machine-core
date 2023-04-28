package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;

public class StatementVisitor extends PMLBaseVisitor<PMLStatement> {

    private final VisitorContext visitorCtx;

    public StatementVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitStatement(PMLParser.StatementContext ctx) {
        PMLStatement statement = null;
        if (ctx.variableDeclarationStatement() != null) {
            statement = new VarStmtVisitor(visitorCtx)
                    .visitVariableDeclarationStatement(ctx.variableDeclarationStatement());
        } else if (ctx.functionDefinitionStatement() != null) {
            statement = new FunctionDefinitionVisitor(visitorCtx)
                    .visitFunctionDefinitionStatement(ctx.functionDefinitionStatement());
        } else if (ctx.foreachStatement() != null) {
            statement = new ForeachStmtVisitor(visitorCtx)
                    .visitForeachStatement(ctx.foreachStatement());
        } else if (ctx.forRangeStatement() != null) {
            statement = new ForRangeStmtVisitor(visitorCtx)
                    .visitForRangeStatement(ctx.forRangeStatement());
        } else if (ctx.functionInvokeStatement() != null) {
            statement = new FunctionInvokeVisitor(visitorCtx)
                    .visitFunctionInvokeStatement(ctx.functionInvokeStatement());
        } else if (ctx.ifStatement() != null) {
            statement = new IfStmtVisitor(visitorCtx)
                    .visitIfStatement(ctx.ifStatement());
        } else if (ctx.createAttributeStatement() != null) {
            statement = new CreateAttrStmtVisitor(visitorCtx)
                    .visitCreateAttributeStatement(ctx.createAttributeStatement());
        } else if (ctx.createPolicyStatement() != null) {
            statement = new CreatePolicyStmtVisitor(visitorCtx)
                    .visitCreatePolicyStatement(ctx.createPolicyStatement());
        } else if (ctx.createUserOrObjectStatement() != null) {
            statement = new CreateUserOrObjectStmtVisitor(visitorCtx)
                    .visitCreateUserOrObjectStatement(ctx.createUserOrObjectStatement());
        } else if (ctx.createProhibitionStatement() != null) {
            statement = new CreateProhibitionStmtVisitor(visitorCtx)
                    .visitCreateProhibitionStatement(ctx.createProhibitionStatement());
        } else if (ctx.createObligationStatement() != null) {
            statement = new CreateObligationStmtVisitor(visitorCtx)
                    .visitCreateObligationStatement(ctx.createObligationStatement());
        } else if (ctx.setNodePropertiesStatement() != null) {
            statement = new SetNodePropertiesStmtVisitor(visitorCtx)
                    .visitSetNodePropertiesStatement(ctx.setNodePropertiesStatement());
        } else if (ctx.assignStatement() != null) {
            statement = new AssignStmtVisitor(visitorCtx)
                    .visitAssignStatement(ctx.assignStatement());
        } else if (ctx.deassignStatement() != null) {
            statement = new DeassignStmtVisitor(visitorCtx)
                    .visitDeassignStatement(ctx.deassignStatement());
        } else if (ctx.deleteStatement() != null) {
            statement = new DeleteStmtVisitor(visitorCtx)
                    .visitDeleteStatement(ctx.deleteStatement());
        } else if (ctx.associateStatement() != null) {
            statement = new AssociateStmtVisitor(visitorCtx)
                    .visitAssociateStatement(ctx.associateStatement());
        } else if (ctx.dissociateStatement() != null) {
            statement = new DissociateStmtVisitor(visitorCtx)
                    .visitDissociateStatement(ctx.dissociateStatement());
        } else if (ctx.functionReturnStatement() != null) {
            statement = new FunctionReturnStmtVisitor(visitorCtx)
                    .visitFunctionReturnStatement(ctx.functionReturnStatement());
        } else if (ctx.breakStatement() != null) {
            statement = new BreakStmtVisitor(visitorCtx)
                    .visitBreakStatement(ctx.breakStatement());
        } else if (ctx.continueStatement() != null) {
            statement = new ContinueStmtVisitor(visitorCtx)
                    .visitContinueStatement(ctx.continueStatement());
        } else if (ctx.setResourceAccessRightsStatement() != null) {
            statement = new SetResourceAccessRightsStmtVisitor(visitorCtx)
                    .visitSetResourceAccessRightsStatement(ctx.setResourceAccessRightsStatement());
        } else if (ctx.deleteRuleStatement() != null) {
            statement = new DeleteRuleStmtVisitor(visitorCtx)
                    .visitDeleteRuleStatement(ctx.deleteRuleStatement());
        }

        return statement;
    }
}
