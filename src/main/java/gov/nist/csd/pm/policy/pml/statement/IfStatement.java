package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


public class IfStatement extends PMLStatement {

    private final ConditionalBlock ifBlock;
    private final List<ConditionalBlock> ifElseBlocks;
    private final List<PMLStatement> elseBlockStatements;

    public IfStatement(ConditionalBlock ifBlock, List<ConditionalBlock> ifElseBlocks, List<PMLStatement> elseBlock) {
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

    public List<PMLStatement> getElseBlock() {
        return elseBlockStatements;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        boolean condition = ifBlock.condition.execute(ctx, policy).getBooleanValue();

        if (condition) {
            return executeBlock(ctx, policy, ifBlock.block);
        }

        // check else ifs
        for (ConditionalBlock conditionalBlock : ifElseBlocks) {
            condition = conditionalBlock.condition.execute(ctx, policy).getBooleanValue();
            if (condition) {
                return executeBlock(ctx, policy, conditionalBlock.block);
            }
        }

        return executeBlock(ctx, policy, elseBlockStatements);
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
        if (elseBlockStatements.isEmpty()) {
            return "";
        }

        return String.format(" else %s", new PMLStatementBlock(elseBlockStatements).toFormattedString(indentLevel));
    }

    private String elseIfBlockToString(int indentLevel) {
        StringBuilder s = new StringBuilder();
        for (ConditionalBlock b : ifElseBlocks) {
            s.append(String.format(" else if %s %s", b.condition, new PMLStatementBlock(b.block).toFormattedString(indentLevel)));
        }

        return s.toString();
    }

    private String ifBlockToString(int indentLevel) {
        return String.format("%sif %s %s", indent(indentLevel), ifBlock.condition, new PMLStatementBlock(ifBlock.block).toFormattedString(indentLevel));
    }

    private Value executeBlock(ExecutionContext ctx, Policy policy, List<PMLStatement> block) throws PMException {
        ExecutionContext copy = ctx.copy();

        Value value = PMLExecutor.executeStatementBlock(copy, policy, block);

        ctx.scope().local().overwriteFromLocalScope(copy.scope().local());

        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IfStatement that = (IfStatement) o;
        return Objects.equals(ifBlock, that.ifBlock) && Objects.equals(
                ifElseBlocks, that.ifElseBlocks) && Objects.equals(elseBlockStatements, that.elseBlockStatements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ifBlock, ifElseBlocks, elseBlockStatements);
    }

    public static class ConditionalBlock implements Serializable {
        private final Expression condition;
        private final List<PMLStatement> block;

        public ConditionalBlock(Expression condition, List<PMLStatement> block) {
            this.condition = condition;
            this.block = block;
        }

        public Expression condition() {
            return condition;
        }

        public List<PMLStatement> block() {
            return block;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ConditionalBlock that = (ConditionalBlock) o;
            return Objects.equals(condition, that.condition) && Objects.equals(block, that.block);
        }

        @Override
        public int hashCode() {
            return Objects.hash(condition, block);
        }
    }
}
