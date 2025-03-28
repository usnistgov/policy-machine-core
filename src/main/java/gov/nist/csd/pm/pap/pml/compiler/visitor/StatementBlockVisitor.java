package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunctionSignature;

import gov.nist.csd.pm.pap.pml.expression.FunctionInvokeExpression;
import gov.nist.csd.pm.pap.pml.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.pap.pml.statement.basic.ReturnStatement;
import gov.nist.csd.pm.pap.pml.statement.basic.IfStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.ArrayList;
import java.util.List;

public class StatementBlockVisitor extends PMLBaseVisitor<StatementBlockVisitor.Result> {

    private final Type returnType;

    public StatementBlockVisitor(VisitorContext visitorCtx, Type returnType) {
        super(visitorCtx);
        this.returnType = returnType;
    }

    @Override
    public Result visitBasicStatementBlock(PMLParser.BasicStatementBlockContext ctx) {
        List<PMLStatement> stmts = new ArrayList<>();
        StatementVisitor statementVisitor = new StatementVisitor(visitorCtx);
        for (PMLParser.BasicStatementContext statementContext : ctx.basicStatement()) {
            PMLStatement pmlStatement = statementVisitor.visitBasicStatement(statementContext);

            if (pmlStatement instanceof FunctionInvokeExpression functionInvokeExpression) {
                String functionName = functionInvokeExpression.getFuncName();

	            try {
                    PMLFunctionSignature signature = visitorCtx.scope().getFunction(functionName);

                    if (!(signature instanceof PMLBasicFunctionSignature)){
                        visitorCtx.errorLog().addError(statementContext, "only PML basic functions (defined as 'function') allowed in basic statement block");
                    }
                } catch (UnknownFunctionInScopeException e) {
                    visitorCtx.errorLog().addError(statementContext, e.getMessage());
	            }
            }

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
        List<PMLStatement> stmts = new ArrayList<>();
        StatementVisitor statementVisitor = new StatementVisitor(visitorCtx);
        for (PMLParser.StatementContext statementContext : ctx.statement()) {
            PMLStatement pmlStatement = statementVisitor.visitStatement(statementContext);
            stmts.add(pmlStatement);
        }

        try {
            boolean allPathsReturned = checkAllPathsReturned(visitorCtx, stmts, returnType);
            return new Result(allPathsReturned, new PMLStatementBlock(stmts));
        } catch (PMException e) {
            throw new PMLCompilationRuntimeException(ctx, e.getMessage());
        }
    }

    public static boolean checkAllPathsReturned(VisitorContext visitorCtx, List<PMLStatement> statements, Type returnType)
            throws PMException {
        if (statements.isEmpty()) {
            return false;
        }

        PMLStatement lastStmt = statements.getLast();
        if (lastStmt instanceof ReturnStatement returnStatement) {
            if (!returnStatement.matchesReturnType(returnType, visitorCtx.scope())) {
                throw new PMException("return statement \"" + returnStatement + "\" does not match return type " + returnType);
            }

            return true;
        }


        boolean allPathsReturned = false;
        PMLStatement pmlStatement;
        for (int i = 0; i < statements.size(); i++) {
            pmlStatement = statements.get(i);

            if (pmlStatement instanceof ReturnStatement returnStatement) {
                if (i < statements.size() - 1) {
                    throw new PMException("function return should be last statement in block");
                }

                if (!returnStatement.matchesReturnType(returnType, visitorCtx.scope())) {
                    throw new PMException("return statement \"" + returnStatement + "\" does not match return type " + returnType);
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

    private static boolean allIfStatementPathsReturned(VisitorContext visitorCtx, IfStatement ifStatement, Type returnType)
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
