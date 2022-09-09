package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.List;
import java.util.Objects;

import static gov.nist.csd.pm.policy.author.pal.PALExecutor.executeStatementBlock;

public class IfStatement extends PALStatement {

    private final ConditionalBlock ifBlock;
    private final List<ConditionalBlock> ifElseBlocks;
    private final List<PALStatement> elseBlock;

    public IfStatement(ConditionalBlock ifBlock, List<ConditionalBlock> ifElseBlocks, List<PALStatement> elseBlock) {
        this.ifBlock = ifBlock;
        this.ifElseBlocks = ifElseBlocks;
        this.elseBlock = elseBlock;
    }

    public ConditionalBlock getIfBlock() {
        return ifBlock;
    }

    public List<ConditionalBlock> getIfElseBlocks() {
        return ifElseBlocks;
    }

    public List<PALStatement> getElseBlock() {
        return elseBlock;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        boolean condition = ifBlock.condition.execute(ctx, policyAuthor).getBooleanValue();
        if (condition) {
            return executeBlock(ctx, policyAuthor, ifBlock.block);
        }

        // check else ifs
        for (ConditionalBlock conditionalBlock : ifElseBlocks) {
            condition = conditionalBlock.condition.execute(ctx, policyAuthor).getBooleanValue();
            if (condition) {
                return executeBlock(ctx, policyAuthor, conditionalBlock.block);
            }
        }

        return executeBlock(ctx, policyAuthor, elseBlock);
    }

    @Override
    public String toString(int indent) {
        return format(
                indent,
                "%s%s%s",
                ifBlockToString(indent),
                elseIfBlockToString(indent),
                elseBlockToString(indent)
        );
    }

    private String elseBlockToString(int indent) {
        if (!elseBlock.isEmpty()) {
            return "";
        }
        return format(indent, "else {\n%s\n}\n", blockToString(indent, elseBlock));
    }

    private String elseIfBlockToString(int indent) {
        String s = "";
        for (ConditionalBlock b : ifElseBlocks) {
            s += format(indent, " else if %s {\n%s\n}\n", b.condition.toString(indent), blockToString(indent, b.block));
        }

        return s;
    }

    private String ifBlockToString(int indent) {
        return format(indent, "if %s {\n%s\n}\n", ifBlock.condition.toString(indent), blockToString(indent, ifBlock.block));
    }

    private String blockToString(int indent, List<PALStatement> stmts) {
        String s = "";
        indent++;
        for (PALStatement stmt : stmts) {
            s += stmt.toString(indent) + "\n";
        }
        return s;
    }

    private Value executeBlock(ExecutionContext ctx, PolicyAuthor policyAuthor, List<PALStatement> block) throws PMException {
        ExecutionContext copy = ctx.copy();
        Value value = executeStatementBlock(copy, policyAuthor, block);

        ctx.updateVariables(copy);

        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IfStatement ifStmt = (IfStatement) o;
        return Objects.equals(ifBlock, ifStmt.ifBlock) && Objects.equals(ifElseBlocks, ifStmt.ifElseBlocks) && Objects.equals(elseBlock, ifStmt.elseBlock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ifBlock, ifElseBlocks, elseBlock);
    }

    public record ConditionalBlock(Expression condition, List<PALStatement> block) { }
}
