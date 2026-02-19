package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.OnPatternBlockContext;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.basic.IfStatement;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ReturnStatement;
import gov.nist.csd.pm.core.pap.pml.type.TypeStringer;
import java.util.ArrayList;
import java.util.List;

public class StatementBlockVisitor extends PMLBaseVisitor<StatementBlockVisitor.Result> {

    private final Type<?> returnType;
    private final boolean allowQueryOps;

    public StatementBlockVisitor(VisitorContext visitorCtx, Type<?> returnType, boolean allowQueryOps) {
        super(visitorCtx);
        this.returnType = returnType;
        this.allowQueryOps = allowQueryOps;
    }

    @Override
    public Result visitBasicStatementBlock(PMLParser.BasicStatementBlockContext ctx) {
        List<PMLStatement<?>> stmts = new ArrayList<>();

        VisitorContext localCtx;
        if (allowQueryOps) {
            localCtx = visitorCtx.copyFunctionsAndQueriesOnly();
        } else {
            localCtx = visitorCtx.copyFunctionsOnly();
        }

        StatementVisitor statementVisitor = new StatementVisitor(localCtx);
        for (PMLParser.BasicStatementContext statementContext : ctx.basicStatement()) {
            PMLStatement<?> pmlStatement = statementVisitor.visitBasicStatement(statementContext);
            stmts.add(pmlStatement);
        }

        try {
            boolean allPathsReturned = checkAllPathsReturned(visitorCtx, stmts, returnType);
            return new Result(allPathsReturned, new PMLStatementBlock(stmts));
        } catch (PMException e) {
            throw new PMLCompilationRuntimeException(ctx, e.getMessage());
        }
    }

    @Override
    public Result visitOnPatternBlock(OnPatternBlockContext ctx) {
        List<PMLStatement<?>> stmts = new ArrayList<>();
        StatementVisitor statementVisitor = new StatementVisitor(visitorCtx.copyFunctionsAndQueriesOnly());
        for (PMLParser.BasicStatementContext statementContext : ctx.basicStatement()) {
            PMLStatement<?> pmlStatement = statementVisitor.visitBasicStatement(statementContext);
            stmts.add(pmlStatement);
        }

        try {
            boolean allPathsReturned = checkAllPathsReturned(visitorCtx, stmts, returnType);
            return new Result(allPathsReturned, new PMLStatementBlock(stmts));
        } catch (PMException e) {
            throw new PMLCompilationRuntimeException(ctx, e.getMessage());
        }
    }

    @Override
    public Result visitStatementBlock(PMLParser.StatementBlockContext ctx) {
        List<PMLStatement<?>> stmts = new ArrayList<>();
        StatementVisitor statementVisitor = new StatementVisitor(visitorCtx);
        for (PMLParser.StatementContext statementContext : ctx.statement()) {
            PMLStatement<?> pmlStatement = statementVisitor.visitStatement(statementContext);
            stmts.add(pmlStatement);
        }

        try {
            boolean allPathsReturned = checkAllPathsReturned(visitorCtx, stmts, returnType);
            return new Result(allPathsReturned, new PMLStatementBlock(stmts));
        } catch (PMException e) {
            throw new PMLCompilationRuntimeException(ctx, e.getMessage());
        }
    }

    public static boolean checkAllPathsReturned(VisitorContext visitorCtx, List<PMLStatement<?>> statements, Type<?> returnType)
            throws PMException {
        if (statements.isEmpty()) {
            return false;
        }

        PMLStatement<?> lastStmt = statements.getLast();
        if (lastStmt instanceof ReturnStatement returnStatement) {
            if (!returnStatement.matchesReturnType(returnType)) {
                throw new PMException("return statement \"" + returnStatement + "\" does not match return type " + TypeStringer.toPMLString(returnType));
            }

            return true;
        }


        boolean allPathsReturned = false;
        PMLStatement<?> pmlStatement;
        for (int i = 0; i < statements.size(); i++) {
            pmlStatement = statements.get(i);

            if (pmlStatement instanceof ReturnStatement returnStatement) {
                if (i < statements.size() - 1) {
                    throw new PMException("return should be last statement in block");
                }

                if (!returnStatement.matchesReturnType(returnType)) {
                    throw new PMException("return statement \"" + returnStatement + "\" does not match return type " + TypeStringer.toPMLString(returnType));
                }

                return true;
            } else if (pmlStatement instanceof IfStatement ifStatement) {
                if (!allIfStatementPathsReturned(visitorCtx, ifStatement, returnType)) {
                    return false;
                } else {
                    allPathsReturned = true;
                }
            }
        }

        return allPathsReturned;
    }

    private static boolean allIfStatementPathsReturned(VisitorContext visitorCtx, IfStatement ifStatement, Type<?> returnType)
            throws PMException {
        boolean check = checkAllPathsReturned(visitorCtx, ifStatement.getIfBlock().block().getStmts(), returnType);
        if (!check) {
            return false;
        }

        // check else ifs
        for (IfStatement.ConditionalBlock conditionalBlock : ifStatement.getElseIfBlocks()) {
            check = checkAllPathsReturned(visitorCtx, conditionalBlock.block().getStmts(), returnType);
            if (!check) {
                return false;
            }
        }

        // check else
        return checkAllPathsReturned(visitorCtx, ifStatement.getElseBlock().getStmts(), returnType);
    }

    public record Result(boolean allPathsReturned, PMLStatementBlock stmts) {

    }
}
