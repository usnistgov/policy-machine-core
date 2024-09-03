package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateFunctionStatement;
import gov.nist.csd.pm.pap.pml.statement.FunctionReturnStatement;
import gov.nist.csd.pm.pap.pml.statement.IfStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatementBlockVisitor extends PMLBaseVisitor<StatementBlockVisitor.Result> {

    private Type returnType;

    public StatementBlockVisitor(VisitorContext visitorCtx, Type returnType) {
        super(visitorCtx);
        this.returnType = returnType;
    }

    @Override
    public Result visitStatementBlock(PMLParser.StatementBlockContext ctx) {
        List<PMLStatement> stmts = new ArrayList<>();
        StatementVisitor statementVisitor = new StatementVisitor(visitorCtx);
        for (PMLParser.StatementContext statementContext : ctx.statement()) {
            PMLStatement pmlStatement = statementVisitor.visitStatement(statementContext);

            if (pmlStatement instanceof CreateFunctionStatement) {
                throw new PMLCompilationRuntimeException(statementContext, "operations are not allowed inside statement blocks");
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

    public static boolean checkAllPathsReturned(VisitorContext visitorCtx, List<PMLStatement> statements, Type returnType)
            throws PMException {
        if (statements.isEmpty()) {
            return false;
        }

        PMLStatement lastStmt = statements.get(statements.size()-1);
        if (lastStmt instanceof FunctionReturnStatement) {
            FunctionReturnStatement functionReturnStatement = (FunctionReturnStatement) lastStmt;
            if (!functionReturnStatement.matchesReturnType(returnType, visitorCtx.scope())) {
                throw new PMException("return statement \"" + functionReturnStatement + "\" does not match return type " + returnType);
            }

            return true;
        }


        boolean allPathsReturned = false;
        PMLStatement pmlStatement;
        for (int i = 0; i < statements.size(); i++) {
            pmlStatement = statements.get(i);

            if (pmlStatement instanceof FunctionReturnStatement) {
                FunctionReturnStatement functionReturnStatement = (FunctionReturnStatement) pmlStatement;
                if (i < statements.size() - 1) {
                    throw new PMException("function return should be last statement in block");
                }

                if (!functionReturnStatement.matchesReturnType(returnType, visitorCtx.scope())) {
                    throw new PMException("return statement \"" + functionReturnStatement + "\" does not match return type " + returnType);
                }

                return true;
            } else if (pmlStatement instanceof IfStatement) {
                IfStatement ifStatement = (IfStatement) pmlStatement;
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
        for (IfStatement.ConditionalBlock conditionalBlock : ifStatement.getIfElseBlocks()) {
            check = checkAllPathsReturned(visitorCtx, conditionalBlock.block().getStmts(), returnType);
            if (!check) {
                return false;
            }
        }

        // check else
        return checkAllPathsReturned(visitorCtx, ifStatement.getElseBlock().getStmts(), returnType);
    }

    public final class Result {
        private final boolean allPathsReturned;
        private final PMLStatementBlock stmts;

        public Result(boolean allPathsReturned, PMLStatementBlock stmts) {
            this.allPathsReturned = allPathsReturned;
            this.stmts = stmts;
        }

        public boolean allPathsReturned() {
            return allPathsReturned;
        }

        public PMLStatementBlock stmts() {
            return stmts;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Result) obj;
            return this.allPathsReturned == that.allPathsReturned &&
                    Objects.equals(this.stmts, that.stmts);
        }

        @Override
        public int hashCode() {
            return Objects.hash(allPathsReturned, stmts);
        }

        @Override
        public String toString() {
            return "Result[" +
                    "allPathsReturned=" + allPathsReturned + ", " +
                    "stmts=" + stmts + ']';
        }


        }
}
