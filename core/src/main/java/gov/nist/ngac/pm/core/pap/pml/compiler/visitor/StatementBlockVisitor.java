/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.OnPatternBlockContext;
import gov.nist.ngac.pm.core.pap.pml.compiler.error.CompileError;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.IfStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.ReturnStatement;
import gov.nist.ngac.pm.core.pap.pml.type.TypeStringer;
import java.util.ArrayList;
import java.util.List;

/**
 * Compiles a PML statement block into a {@link Result}. Checks that every path returns a value matching
 * the expected return type.
 */
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
        List<CompileError> stmtErrors = new ArrayList<>();
        for (PMLParser.BasicStatementContext statementContext : ctx.basicStatement()) {
            try {
                stmts.add(statementVisitor.visitBasicStatement(statementContext));
            } catch (PMLCompilationRuntimeException e) {
                stmtErrors.addAll(e.getErrors());
            }
        }

        if (!stmtErrors.isEmpty()) {
            throw new PMLCompilationRuntimeException(stmtErrors);
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
        List<CompileError> stmtErrors = new ArrayList<>();
        for (PMLParser.BasicStatementContext statementContext : ctx.basicStatement()) {
            try {
                stmts.add(statementVisitor.visitBasicStatement(statementContext));
            } catch (PMLCompilationRuntimeException e) {
                stmtErrors.addAll(e.getErrors());
            }
        }

        if (!stmtErrors.isEmpty()) {
            throw new PMLCompilationRuntimeException(stmtErrors);
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
        List<CompileError> stmtErrors = new ArrayList<>();
        for (PMLParser.StatementContext statementContext : ctx.statement()) {
            try {
                stmts.add(statementVisitor.visitStatement(statementContext));
            } catch (PMLCompilationRuntimeException e) {
                stmtErrors.addAll(e.getErrors());
            }
        }

        if (!stmtErrors.isEmpty()) {
            throw new PMLCompilationRuntimeException(stmtErrors);
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

    /**
     * A compiled statement block, plus whether every control-flow path through it returns a value.
     *
     * @param allPathsReturned whether every path returns a value
     * @param stmts the compiled statement block
     */
    public record Result(boolean allPathsReturned, PMLStatementBlock stmts) {

    }
}
