package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.basic.IfStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;


import java.util.ArrayList;
import java.util.List;

public class IfStmtVisitor extends PMLBaseVisitor<PMLStatement<?>> {

    public IfStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public PMLStatement<?> visitIfStatement(PMLParser.IfStatementContext ctx) {
        // if block
        VisitorContext localVisitorCtx = visitorCtx.copy();
        Expression<Boolean> condition = ExpressionVisitor.compile(localVisitorCtx, ctx.condition, BOOLEAN_TYPE);

        List<PMLStatement<?>> block = new ArrayList<>();
        StatementVisitor statementVisitor = new StatementVisitor(localVisitorCtx);
        for (PMLParser.StatementContext stmtCtx : ctx.statementBlock().statement()) {
            PMLStatement<?> statement = statementVisitor.visitStatement(stmtCtx);
            block.add(statement);
        }

        // update outer scoped variables
        visitorCtx.scope().overwriteFromScope(localVisitorCtx.scope());

        IfStatement.ConditionalBlock ifBlock = new IfStatement.ConditionalBlock(condition, new PMLStatementBlock(block));

        // else ifs
        localVisitorCtx = visitorCtx.copy();
        statementVisitor = new StatementVisitor(localVisitorCtx);
        List<IfStatement.ConditionalBlock> elseIfs = new ArrayList<>();
        for (PMLParser.ElseIfStatementContext elseIfStmtCtx : ctx.elseIfStatement()) {
            condition = ExpressionVisitor.compile(visitorCtx, elseIfStmtCtx.condition, BOOLEAN_TYPE);
            block = new ArrayList<>();
            for (PMLParser.StatementContext stmtCtx : elseIfStmtCtx.statementBlock().statement()) {
                PMLStatement<?> statement = statementVisitor.visitStatement(stmtCtx);
                block.add(statement);
            }
            elseIfs.add(new IfStatement.ConditionalBlock(condition, new PMLStatementBlock(block)));

            // update outer scoped variables
            visitorCtx.scope().overwriteFromScope(localVisitorCtx.scope());
        }

        // else
        localVisitorCtx = visitorCtx.copy();
        statementVisitor = new StatementVisitor(localVisitorCtx);
        block = new ArrayList<>();
        if (ctx.elseStatement() != null) {
            for (PMLParser.StatementContext stmtCtx : ctx.elseStatement().statementBlock().statement()) {
                PMLStatement<?> statement = statementVisitor.visitStatement(stmtCtx);
                block.add(statement);
            }

            // update outer scoped variables
            visitorCtx.scope().overwriteFromScope(localVisitorCtx.scope());
        }

        return new IfStatement(ifBlock, elseIfs, new PMLStatementBlock(block));
    }
}
