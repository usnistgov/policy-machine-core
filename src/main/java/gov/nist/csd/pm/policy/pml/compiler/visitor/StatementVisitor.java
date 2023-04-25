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
    public PMLStatement visitStmt(PMLParser.StmtContext ctx) {
        PMLStatement statement = null;
        if (ctx.varStmt() != null) {
            statement = new VarStmtVisitor(visitorCtx)
                    .visitVarStmt(ctx.varStmt());
        } else if (ctx.funcDefStmt() != null) {
            statement = new FunctionDefinitionVisitor(visitorCtx)
                    .visitFuncDefStmt(ctx.funcDefStmt());
        } else if (ctx.foreachStmt() != null) {
            statement = new ForeachStmtVisitor(visitorCtx)
                    .visitForeachStmt(ctx.foreachStmt());
        } else if (ctx.forRangeStmt() != null) {
            statement = new ForRangeStmtVisitor(visitorCtx)
                    .visitForRangeStmt(ctx.forRangeStmt());
        } else if (ctx.funcCallStmt() != null) {
            statement = new FunctionCallVisitor(visitorCtx)
                    .visitFuncCallStmt(ctx.funcCallStmt());
        } else if (ctx.ifStmt() != null) {
            statement = new IfStmtVisitor(visitorCtx)
                    .visitIfStmt(ctx.ifStmt());
        } else if (ctx.createAttrStmt() != null) {
            statement = new CreateAttrStmtVisitor(visitorCtx)
                    .visitCreateAttrStmt(ctx.createAttrStmt());
        } else if (ctx.createPolicyStmt() != null) {
            statement = new CreatePolicyStmtVisitor(visitorCtx)
                    .visitCreatePolicyStmt(ctx.createPolicyStmt());
        } else if (ctx.createUserOrObjectStmt() != null) {
            statement = new CreateUserOrObjectStmtVisitor(visitorCtx)
                    .visitCreateUserOrObjectStmt(ctx.createUserOrObjectStmt());
        } else if (ctx.createProhibitionStmt() != null) {
            statement = new CreateProhibitionStmtVisitor(visitorCtx)
                    .visitCreateProhibitionStmt(ctx.createProhibitionStmt());
        } else if (ctx.createObligationStmt() != null) {
            statement = new CreateObligationStmtVisitor(visitorCtx)
                    .visitCreateObligationStmt(ctx.createObligationStmt());
        } else if (ctx.setNodePropsStmt() != null) {
            statement = new SetNodePropertiesStmtVisitor(visitorCtx)
                    .visitSetNodePropsStmt(ctx.setNodePropsStmt());
        } else if (ctx.assignStmt() != null) {
            statement = new AssignStmtVisitor(visitorCtx)
                    .visitAssignStmt(ctx.assignStmt());
        } else if (ctx.deassignStmt() != null) {
            statement = new DeassignStmtVisitor(visitorCtx)
                    .visitDeassignStmt(ctx.deassignStmt());
        } else if (ctx.deleteStmt() != null) {
            statement = new DeleteStmtVisitor(visitorCtx)
                    .visitDeleteStmt(ctx.deleteStmt());
        } else if (ctx.associateStmt() != null) {
            statement = new AssociateStmtVisitor(visitorCtx)
                    .visitAssociateStmt(ctx.associateStmt());
        } else if (ctx.dissociateStmt() != null) {
            statement = new DissociateStmtVisitor(visitorCtx)
                    .visitDissociateStmt(ctx.dissociateStmt());
        } else if (ctx.funcReturnStmt() != null) {
            statement = new FunctionReturnStmtVisitor(visitorCtx)
                    .visitFuncReturnStmt(ctx.funcReturnStmt());
        } else if (ctx.breakStmt() != null) {
            statement = new BreakStmtVisitor(visitorCtx)
                    .visitBreakStmt(ctx.breakStmt());
        } else if (ctx.continueStmt() != null) {
            statement = new ContinueStmtVisitor(visitorCtx)
                    .visitContinueStmt(ctx.continueStmt());
        } else if (ctx.setResourceAccessRightsStmt() != null) {
            statement = new SetResourceAccessRightsStmtVisitor(visitorCtx)
                    .visitSetResourceAccessRightsStmt(ctx.setResourceAccessRightsStmt());
        } else if (ctx.deleteRuleStmt() != null) {
            statement = new DeleteRuleStmtVisitor(visitorCtx)
                    .visitDeleteRuleStmt(ctx.deleteRuleStmt());
        }

        return statement;
    }
}
