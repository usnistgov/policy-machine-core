package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.NameExpression;
import gov.nist.csd.pm.policy.author.pal.statement.CreateObligationStatement;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;

import java.util.ArrayList;
import java.util.List;

public class CreateObligationStmtVisitor extends PALBaseVisitor<CreateObligationStatement> {

    private final VisitorContext visitorCtx;

    public CreateObligationStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public CreateObligationStatement visitCreateObligationStmt(PALParser.CreateObligationStmtContext ctx) {
        NameExpression name = NameExpression.compile(visitorCtx, ctx.nameExpression());

        List<PALStatement> ruleStmts = new ArrayList<>();
        for (PALParser.CreateRuleStmtContext ruleStmt : ctx.createRuleStmt()) {
            PALStatement createRuleStmt = new CreateRuleStmtVisitor(visitorCtx)
                    .visitCreateRuleStmt(ruleStmt);
            ruleStmts.add(createRuleStmt);
        }

        return new CreateObligationStatement(name, ruleStmts);
    }
}
