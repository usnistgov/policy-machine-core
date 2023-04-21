package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.IfStatement;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.PALStatement;

import java.util.ArrayList;
import java.util.List;

public class IfStmtVisitor extends PMLBaseVisitor<IfStatement> {

    private final VisitorContext visitorCtx;

    public IfStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public IfStatement visitIfStmt(PMLParser.IfStmtContext ctx) {
        // if block
        VisitorContext localVisitorCtx = visitorCtx.copy();
        boolean isComp = ctx.IS_COMPLEMENT() != null;
        Expression condition = Expression.compile(localVisitorCtx, ctx.condition, Type.bool());

        List<PALStatement> block = new ArrayList<>();
        StatementVisitor statementVisitor = new StatementVisitor(localVisitorCtx);
        for (PMLParser.StmtContext stmtCtx : ctx.stmtBlock().stmt()) {
            PALStatement statement = statementVisitor.visitStmt(stmtCtx);
            block.add(statement);
        }

        // update outer scoped variables
        visitorCtx.scope().overwriteVariables(localVisitorCtx.scope());

        IfStatement.ConditionalBlock ifBlock = new IfStatement.ConditionalBlock(isComp, condition, block);

        // else ifs
        localVisitorCtx = visitorCtx.copy();
        statementVisitor = new StatementVisitor(localVisitorCtx);
        List<IfStatement.ConditionalBlock> elseIfs = new ArrayList<>();
        for (PMLParser.ElseIfStmtContext elseIfStmtCtx : ctx.elseIfStmt()) {
            isComp = elseIfStmtCtx.IS_COMPLEMENT() != null;
            condition = Expression.compile(visitorCtx, elseIfStmtCtx.condition, Type.bool());
            block = new ArrayList<>();
            for (PMLParser.StmtContext stmtCtx : elseIfStmtCtx.stmtBlock().stmt()) {
                PALStatement statement = statementVisitor.visitStmt(stmtCtx);
                block.add(statement);
            }
            elseIfs.add(new IfStatement.ConditionalBlock(isComp, condition, block));

            // update outer scoped variables
            visitorCtx.scope().overwriteVariables(localVisitorCtx.scope());
        }

        // else
        localVisitorCtx = visitorCtx.copy();
        statementVisitor = new StatementVisitor(localVisitorCtx);
        block = new ArrayList<>();
        if (ctx.elseStmt() != null) {
            for (PMLParser.StmtContext stmtCtx : ctx.elseStmt().stmtBlock().stmt()) {
                PALStatement statement = statementVisitor.visitStmt(stmtCtx);
                block.add(statement);
            }

            // update outer scoped variables
            visitorCtx.scope().overwriteVariables(localVisitorCtx.scope());
        }

        return new IfStatement(ifBlock, elseIfs, block);
    }
}
