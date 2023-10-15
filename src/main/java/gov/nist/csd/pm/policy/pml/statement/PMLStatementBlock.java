package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;

import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

import java.util.List;
import java.util.Objects;


public class PMLStatementBlock extends PMLStatement{

    private List<PMLStatement> stmts;

    public PMLStatementBlock(List<PMLStatement> stmts) {
        this.stmts = stmts;
    }

    public List<PMLStatement> getStmts() {
        return stmts;
    }

    public void setStmts(List<PMLStatement> stmts) {
        this.stmts = stmts;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        for (PMLStatement stmt : stmts) {
            stmt.execute(ctx, policy);
        }

        return new VoidValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PMLStatementBlock that = (PMLStatementBlock) o;
        return Objects.equals(stmts, that.stmts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stmts);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        StringBuilder sb = new StringBuilder("{\n");
        for (PMLStatement stmt : stmts) {
            sb.append(stmt.toFormattedString(indentLevel+1)).append("\n");
        }

        return sb.append(indent(indentLevel)).append("}").toString();
    }
}
