package gov.nist.csd.pm.pap.pml.statement;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


public class IfStatement extends ControlStatement {

    private ConditionalBlock ifBlock;
    private List<ConditionalBlock> ifElseBlocks;
    private PMLStatementBlock elseBlockStatements;

    public IfStatement(ConditionalBlock ifBlock, List<ConditionalBlock> ifElseBlocks, PMLStatementBlock elseBlock) {
        this.ifBlock = ifBlock;
        this.ifElseBlocks = ifElseBlocks;
        this.elseBlockStatements = elseBlock;
    }

    public ConditionalBlock getIfBlock() {
        return ifBlock;
    }

    public List<ConditionalBlock> getIfElseBlocks() {
        return ifElseBlocks;
    }

    public PMLStatementBlock getElseBlock() {
        return elseBlockStatements;
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        boolean condition = ifBlock.condition.execute(ctx, pap).getBooleanValue();

        if (condition) {
            return ifBlock.block.execute(ctx, pap);
        }

        // check else ifs
        for (ConditionalBlock conditionalBlock : ifElseBlocks) {
            condition = conditionalBlock.condition.execute(ctx, pap).getBooleanValue();
            if (condition) {
                return conditionalBlock.block.execute(ctx, pap);
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
        for (ConditionalBlock b : ifElseBlocks) {
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
        return Objects.equals(ifBlock, ifStmt.ifBlock) && Objects.equals(ifElseBlocks, ifStmt.ifElseBlocks) && Objects.equals(elseBlockStatements, ifStmt.elseBlockStatements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ifBlock, ifElseBlocks, elseBlockStatements);
    }

    public static final class ConditionalBlock {
        private final Expression condition;
        private final PMLStatementBlock block;

        public ConditionalBlock(Expression condition, PMLStatementBlock block) {
            this.condition = condition;
            this.block = block;
        }

        public Expression condition() {
            return condition;
        }

        public PMLStatementBlock block() {
            return block;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (ConditionalBlock) obj;
            return Objects.equals(this.condition, that.condition) &&
                    Objects.equals(this.block, that.block);
        }

        @Override
        public int hashCode() {
            return Objects.hash(condition, block);
        }

        @Override
        public String toString() {
            return "ConditionalBlock[" +
                    "condition=" + condition + ", " +
                    "block=" + block + ']';
        }
    }
}
