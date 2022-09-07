package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.author.pal.statement.CreateStatement;

public class CreateStmtVisitor extends PALBaseVisitor<CreateStatement> {

    private final VisitorContext visitorCtx;

    public CreateStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public CreateStatement visitCreateStmt(PALParser.CreateStmtContext ctx) {
        PALStatement statement = null;
        if (ctx.createAttrStmt() != null) {
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
        }

        return new CreateStatement(statement);
    }
}
