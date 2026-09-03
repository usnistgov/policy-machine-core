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

package gov.nist.ngac.pm.core.pap.pml.statement.basic;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.ngac.pm.core.pap.pml.statement.result.StatementResult;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A PML if / else if / else statement.
 */
public class IfStatement extends BasicStatement<StatementResult> {

    private final ConditionalBlock ifBlock;
    private final List<ConditionalBlock> elseIfBlocks;
    private final PMLStatementBlock elseBlockStatements;

    public IfStatement(ConditionalBlock ifBlock, List<ConditionalBlock> elseIfBlocks, PMLStatementBlock elseBlock) {
        this.ifBlock = ifBlock;
        this.elseIfBlocks = elseIfBlocks;
        this.elseBlockStatements = elseBlock;
    }

    public ConditionalBlock getIfBlock() {
        return ifBlock;
    }

    public List<ConditionalBlock> getElseIfBlocks() {
        return elseIfBlocks;
    }

    public PMLStatementBlock getElseBlock() {
        return elseBlockStatements;
    }

    @Override
    public StatementResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        boolean condition = ifBlock.condition.execute(ctx, pap);

        if (condition) {
            return ifBlock.block.execute(ctx, pap);
        }

        // check else ifs
        for (ConditionalBlock conditionalBlock : elseIfBlocks) {
            condition = conditionalBlock.condition.execute(ctx, pap);
            if (condition) {
                return ctx.executeStatements(conditionalBlock.block.getStmts(), new Args());
            }
        }

        return elseBlockStatements.execute(ctx, pap);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format(
                "%s%s%s",
                ifBlockToString(indentLevel),
                elseIfBlockToString(indentLevel),
                elseBlockToString(indentLevel)
        );
    }

    private String elseBlockToString(int indentLevel) {
        if (elseBlockStatements.getStmts().isEmpty()) {
            return "";
        }

        return String.format(" else %s", elseBlockStatements.toFormattedString(indentLevel));
    }

    private String elseIfBlockToString(int indentLevel) {
        StringBuilder s = new StringBuilder();
        for (ConditionalBlock b : elseIfBlocks) {
            s.append(String.format(" else if %s %s", b.condition, b.block.toFormattedString(indentLevel)));
        }

        return s.toString();
    }

    private String ifBlockToString(int indentLevel) {
        return String.format("%sif %s %s", indent(indentLevel), ifBlock.condition, ifBlock.block.toFormattedString(indentLevel));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IfStatement ifStmt = (IfStatement) o;
        return Objects.equals(ifBlock, ifStmt.ifBlock) && Objects.equals(
            elseIfBlocks, ifStmt.elseIfBlocks) && Objects.equals(elseBlockStatements, ifStmt.elseBlockStatements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ifBlock, elseIfBlocks, elseBlockStatements);
    }

    /**
     * A single if / else if branch. The condition guarding it and the block to run when it's true.
     *
     * @param condition the branch's guarding condition
     * @param block the block to run when the condition is true
     */
    public record ConditionalBlock(Expression<Boolean> condition, PMLStatementBlock block) implements Serializable { }
} 