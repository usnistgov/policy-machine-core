package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.CreateObligationStatement;
import gov.nist.csd.pm.policy.pml.statement.CreateRuleStatement;
import gov.nist.csd.pm.policy.pml.type.Type;

import java.util.ArrayList;
import java.util.List;

public class CreateObligationStmtVisitor extends PMLBaseVisitor<CreateObligationStatement> {

    public CreateObligationStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public CreateObligationStatement visitCreateObligationStatement(PMLParser.CreateObligationStatementContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.expression(), Type.string());

        List<CreateRuleStatement> ruleStmts = new ArrayList<>();
        for (PMLParser.CreateRuleStatementContext ruleStmt : ctx.createRuleStatement()) {
            CreateRuleStatement createRuleStmt = (CreateRuleStatement) new CreateRuleStmtVisitor(visitorCtx)
                    .visitCreateRuleStatement(ruleStmt);
            ruleStmts.add(createRuleStmt);
        }

        return new CreateObligationStatement(name, ruleStmts);
    }
}
