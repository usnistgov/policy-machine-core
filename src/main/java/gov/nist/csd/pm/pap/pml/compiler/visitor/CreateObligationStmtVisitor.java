package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateObligationStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateRuleStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

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
            CreateRuleStatement createRuleStmt = new CreateRuleStmtVisitor(visitorCtx)
                    .visitCreateRuleStatement(ruleStmt);
            ruleStmts.add(createRuleStmt);
        }

        return new CreateObligationStatement(name, ruleStmts);
    }
}
