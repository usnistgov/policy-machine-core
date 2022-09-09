package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.List;
import java.util.Objects;

import static gov.nist.csd.pm.policy.author.pal.PALExecutor.executeStatementBlock;

public class ForeachStatement extends PALStatement {

    private final String varName;
    private final String valueVarName;
    private final Expression iter;
    private final List<PALStatement> block;

    public ForeachStatement(String varName, String valueVarName, Expression iter, List<PALStatement> block) {
        this.varName = varName;
        this.valueVarName = valueVarName;
        this.iter = iter;
        this.block = block;
    }

    public String getVarName() {
        return varName;
    }

    public Expression getIter() {
        return iter;
    }

    public List<PALStatement> getBlock() {
        return block;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        if (block.isEmpty()) {
            return new Value();
        }

        Value iterValue = iter.execute(ctx, policyAuthor);
        if (iterValue.isArray()) {
            for (Value v : iterValue.getArrayValue()) {
                ExecutionContext localExecutionCtx = ctx.copy();
                localExecutionCtx.addVariable(varName, v, false);

                Value value = executeStatementBlock(localExecutionCtx, policyAuthor, block);

                if (value.isBreak()) {
                    break;
                } else if (value.isReturn()) {
                    return value;
                }

                ctx.updateVariables(localExecutionCtx);
            }
        } else if (iterValue.isMap()) {
            for (Value key : iterValue.getMapValue().keySet()) {
                ExecutionContext localExecutionCtx = ctx.copy();
                Value mapValue = iterValue.getMapValue().get(key);

                localExecutionCtx.addVariable(varName, key, false);
                if (valueVarName != null) {
                    localExecutionCtx.addVariable(valueVarName, mapValue, false);
                }

                Value value = executeStatementBlock(localExecutionCtx, policyAuthor, block);

                if (value.isBreak()) {
                    break;
                } else if (value.isReturn()) {
                    return value;
                }

                ctx.updateVariables(localExecutionCtx);
            }
        }

        return new Value();
    }

    @Override
    public String toString(int indent) {
        return format(indent, "foreach %s in %s {\n%s\n}",
                (valueVarName != null ? String.format("%s, %s", varName, valueVarName) : varName),
                iter.toString(indent),
                blockToString(indent+1)
        );
    }

    private String blockToString(int indent) {
        String s = "";
        for (PALStatement stmt : block) {
            s += stmt.toString(indent) + "\n";
        }

        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForeachStatement that = (ForeachStatement) o;
        return Objects.equals(varName, that.varName) && Objects.equals(valueVarName, that.valueVarName) && Objects.equals(iter, that.iter) && Objects.equals(block, that.block);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName, valueVarName, iter, block);
    }
}
