package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.scope.PALScopeException;
import gov.nist.csd.pm.policy.author.pal.statement.ForRangeStatement;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;

import java.util.ArrayList;
import java.util.List;

public class ForRangeStmtVisitor extends PALBaseVisitor<ForRangeStatement> {
    private final VisitorContext visitorCtx;

    public ForRangeStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public ForRangeStatement visitForRangeStmt(PALParser.ForRangeStmtContext ctx) {
        String varName = ctx.IDENTIFIER().getText();

        int lower = Integer.parseInt(ctx.lower.getText());
        int upper = Integer.parseInt(ctx.upper.getText());

        List<PALStatement> block = new ArrayList<>();
        for (PALParser.StmtContext stmtCtx : ctx.stmtBlock().stmt()) {
            VisitorContext localVisitorCtx = visitorCtx.copy();

            try {
                localVisitorCtx.scope().addVariable(varName, Type.number(), false);
            }catch (PALScopeException e) {
                visitorCtx.errorLog().addError(ctx, e.getMessage());
            }

            PALStatement statement = new StatementVisitor(localVisitorCtx)
                    .visitStmt(stmtCtx);
            block.add(statement);

            visitorCtx.scope().overwriteVariables(localVisitorCtx.scope());
        }

        return new ForRangeStatement(varName, lower, upper, block);
    }
}
