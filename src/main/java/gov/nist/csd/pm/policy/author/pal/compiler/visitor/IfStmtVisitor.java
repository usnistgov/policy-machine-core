package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.IfStatement;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;

import java.util.ArrayList;
import java.util.List;

public class IfStmtVisitor extends PALBaseVisitor<IfStatement> {

    private final VisitorContext visitorCtx;

    public IfStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public IfStatement visitIfStmt(PALParser.IfStmtContext ctx) {
        // if block
        VisitorContext localVisitorCtx = visitorCtx.copy();
        Expression condition = Expression.compile(localVisitorCtx, ctx.condition, Type.bool());

        List<PALStatement> block = new ArrayList<>();
        StatementVisitor statementVisitor = new StatementVisitor(localVisitorCtx);
        for (PALParser.StmtContext stmtCtx : ctx.stmtBlock().stmt()) {
            PALStatement statement = statementVisitor.visitStmt(stmtCtx);
            block.add(statement);
        }

        // update outer scoped variables
        visitorCtx.scope().overwriteVariables(localVisitorCtx.scope());

        IfStatement.ConditionalBlock ifBlock = new IfStatement.ConditionalBlock(condition, block);

        // else ifs
        localVisitorCtx = visitorCtx.copy();
        statementVisitor = new StatementVisitor(localVisitorCtx);
        List<IfStatement.ConditionalBlock> elseIfs = new ArrayList<>();
        for (PALParser.ElseIfStmtContext elseIfStmtCtx : ctx.elseIfStmt()) {
            condition = Expression.compile(visitorCtx, elseIfStmtCtx.condition, Type.bool());
            block = new ArrayList<>();
            for (PALParser.StmtContext stmtCtx : elseIfStmtCtx.stmtBlock().stmt()) {
                PALStatement statement = statementVisitor.visitStmt(stmtCtx);
                block.add(statement);
            }
            elseIfs.add(new IfStatement.ConditionalBlock(condition, block));

            // update outer scoped variables
            visitorCtx.scope().overwriteVariables(localVisitorCtx.scope());
        }

        // else
        localVisitorCtx = visitorCtx.copy();
        statementVisitor = new StatementVisitor(localVisitorCtx);
        block = new ArrayList<>();
        if (ctx.elseStmt() != null) {
            for (PALParser.StmtContext stmtCtx : ctx.elseStmt().stmtBlock().stmt()) {
                PALStatement statement = statementVisitor.visitStmt(stmtCtx);
                block.add(statement);
            }

            // update outer scoped variables
            visitorCtx.scope().overwriteVariables(localVisitorCtx.scope());
        }

        return new IfStatement(ifBlock, elseIfs, block);
    }
}
