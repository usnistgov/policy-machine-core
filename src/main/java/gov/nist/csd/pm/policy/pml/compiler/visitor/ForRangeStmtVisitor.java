package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.ForRangeStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;

import java.util.ArrayList;
import java.util.List;

public class ForRangeStmtVisitor extends PMLBaseVisitor<ForRangeStatement> {
    private final VisitorContext visitorCtx;

    public ForRangeStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public ForRangeStatement visitForRangeStatement(PMLParser.ForRangeStatementContext ctx) {
        String varName = ctx.ID().getText();

        boolean lowerBound = ctx.lowerBound.getText().equals("(");
        boolean upperBound = ctx.upperBound.getText().equals(")");

        Expression lower = Expression.compile(visitorCtx, ctx.lower, Type.number());
        Expression upper = Expression.compile(visitorCtx, ctx.upper, Type.number());

        VisitorContext localVisitorCtx = visitorCtx.copy();
        List<PMLStatement> block = new ArrayList<>();

        for (PMLParser.StatementContext stmtCtx : ctx.statementBlock().statement()) {

            try {
                localVisitorCtx.scope().addVariable(varName, Type.number(), false);
            }catch (PMLScopeException e) {
                visitorCtx.errorLog().addError(ctx, e.getMessage());
            }

            PMLStatement statement = new StatementVisitor(localVisitorCtx)
                    .visitStatement(stmtCtx);
            block.add(statement);

            visitorCtx.scope().overwriteVariables(localVisitorCtx.scope());
        }

        return new ForRangeStatement(varName, lower, lowerBound, upper, upperBound, block);
    }
}
