package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;

public class StatementVisitor extends PALBaseVisitor<PALStatement> {

    private final VisitorContext visitorCtx;

    public StatementVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PALStatement visitStmt(PALParser.StmtContext ctx) {
        if (ctx.varStmt() != null) {
            return new VarStmtVisitor(visitorCtx)
                    .visitVarStmt(ctx.varStmt());
        } else if (ctx.funcDefStmt() != null) {
            return new FunctionDefinitionVisitor(visitorCtx)
                    .visitFuncDefStmt(ctx.funcDefStmt());
        } else if (ctx.foreachStmt() != null) {
            return new ForeachStmtVisitor(visitorCtx)
                    .visitForeachStmt(ctx.foreachStmt());
        } else if (ctx.funcCallStmt() != null) {
            return new FunctionCallVisitor(visitorCtx)
                    .visitFuncCallStmt(ctx.funcCallStmt());
        } else if (ctx.ifStmt() != null) {
            return new IfStmtVisitor(visitorCtx)
                    .visitIfStmt(ctx.ifStmt());
        } else if (ctx.createStmt() != null) {
            return new CreateStmtVisitor(visitorCtx)
                    .visitCreateStmt(ctx.createStmt());
        } else if (ctx.setNodePropsStmt() != null) {
            return new SetNodePropertiesStmtVisitor(visitorCtx)
                    .visitSetNodePropsStmt(ctx.setNodePropsStmt());
        } else if (ctx.assignStmt() != null) {
            return new AssignStmtVisitor(visitorCtx)
                    .visitAssignStmt(ctx.assignStmt());
        } else if (ctx.deassignStmt() != null) {
            return new DeassignStmtVisitor(visitorCtx)
                    .visitDeassignStmt(ctx.deassignStmt());
        } else if (ctx.deleteStmt() != null) {
            return new DeleteStmtVisitor(visitorCtx)
                    .visitDeleteStmt(ctx.deleteStmt());
        } else if (ctx.associateStmt() != null) {
            return new AssociateStmtVisitor(visitorCtx)
                    .visitAssociateStmt(ctx.associateStmt());
        } else if (ctx.dissociateStmt() != null) {
            return new DissociateStmtVisitor(visitorCtx)
                    .visitDissociateStmt(ctx.dissociateStmt());
        } else if (ctx.funcReturnStmt() != null) {
            return new FunctionReturnStmtVisitor(visitorCtx)
                    .visitFuncReturnStmt(ctx.funcReturnStmt());
        } else if (ctx.breakStmt() != null) {
            return new BreakStmtVisitor(visitorCtx)
                    .visitBreakStmt(ctx.breakStmt());
        } else if (ctx.continueStmt() != null) {
            return new ContinueStmtVisitor(visitorCtx)
                    .visitContinueStmt(ctx.continueStmt());
        } else if (ctx.setResourceAccessRightsStmt() != null) {
            return new SetResourceAccessRightsStmtVisitor(visitorCtx)
                    .visitSetResourceAccessRightsStmt(ctx.setResourceAccessRightsStmt());
        } else if (ctx.deleteRuleStmt() != null) {
            return new DeleteRuleStmtVisitor(visitorCtx)
                    .visitDeleteRuleStmt(ctx.deleteRuleStmt());
        }
        return null;
    }
}
