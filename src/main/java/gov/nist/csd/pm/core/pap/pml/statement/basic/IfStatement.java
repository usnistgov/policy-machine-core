package gov.nist.csd.pm.core.pap.pml.statement.basic;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.result.StatementResult;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    public record ConditionalBlock(Expression<Boolean> condition, PMLStatementBlock block) implements Serializable { }
} 