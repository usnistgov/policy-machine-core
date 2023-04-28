package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.PMLFormatter;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;

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

    public List<PMLStatement> getElseBlockStatements() {
        return elseBlockStatements;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        boolean not = ifBlock.not;
        boolean condition = ifBlock.condition.execute(ctx, policy).getBooleanValue();
        if ((condition && !not) || (!condition && not)) {
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
    public String toString() {
        return String.format(
                "%s%s%s",
                ifBlockToString(),
                elseIfBlockToString(),
                elseBlockToString()
        );
    }

    private String elseBlockToString() {
        if (elseBlockStatements.isEmpty()) {
            return "";
        }
        return String.format("else {%s}", PMLFormatter.statementsToString(elseBlockStatements));
    }

    private String elseIfBlockToString() {
        StringBuilder s = new StringBuilder();
        for (ConditionalBlock b : ifElseBlocks) {
            s.append(String.format(" else if %s {%s} ", b.condition, PMLFormatter.statementsToString(b.block)));
        }

        return s.toString();
    }

    private String ifBlockToString() {
        return String.format("if %s {%s}", ifBlock.condition, PMLFormatter.statementsToString(ifBlock.block));
    }

    private Value executeBlock(ExecutionContext ctx, Policy policy, List<PMLStatement> block) throws PMException {
        ExecutionContext copy = null;
        try {
            copy = ctx.copy();
        } catch (PMLScopeException e) {
            throw new PMException(e.getMessage());
        }
        Value value = PMLExecutor.executeStatementBlock(copy, policy, block);

        ctx.scope().overwriteValues(copy.scope());

        return value;
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

    public record ConditionalBlock(boolean not, Expression condition, List<PMLStatement> block) implements Serializable { }
}
