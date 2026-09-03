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

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;

import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.compiler.error.CompileError;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.IfStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Compiles a PML if / else if / else statement into an {@link IfStatement}.
 */
public class IfStmtVisitor extends PMLBaseVisitor<PMLStatement<?>> {

    public IfStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public PMLStatement<?> visitIfStatement(PMLParser.IfStatementContext ctx) {
        List<CompileError> errors = new ArrayList<>();

        // if block
        VisitorContext localVisitorCtx = visitorCtx.copy();
        Expression<Boolean> condition = ExpressionVisitor.compile(localVisitorCtx, ctx.condition, BOOLEAN_TYPE);

        List<PMLStatement<?>> block = new ArrayList<>();
        StatementVisitor statementVisitor = new StatementVisitor(localVisitorCtx);
        for (PMLParser.StatementContext stmtCtx : ctx.statementBlock().statement()) {
            try {
                block.add(statementVisitor.visitStatement(stmtCtx));
            } catch (PMLCompilationRuntimeException e) {
                errors.addAll(e.getErrors());
            }
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
                try {
                    block.add(statementVisitor.visitStatement(stmtCtx));
                } catch (PMLCompilationRuntimeException e) {
                    errors.addAll(e.getErrors());
                }
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
                try {
                    block.add(statementVisitor.visitStatement(stmtCtx));
                } catch (PMLCompilationRuntimeException e) {
                    errors.addAll(e.getErrors());
                }
            }

            // update outer scoped variables
            visitorCtx.scope().overwriteFromScope(localVisitorCtx.scope());
        }

        if (!errors.isEmpty()) {
            throw new PMLCompilationRuntimeException(errors);
        }

        return new IfStatement(ifBlock, elseIfs, new PMLStatementBlock(block));
    }
}
