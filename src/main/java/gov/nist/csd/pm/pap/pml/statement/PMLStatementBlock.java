package gov.nist.csd.pm.pap.pml.statement;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.statement.basic.BasicStatement;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class PMLStatementBlock extends BasicStatement {

    private List<PMLStatement> stmts;

    public PMLStatementBlock(List<PMLStatement> stmts) {
        this.stmts = stmts;
    }

    public PMLStatementBlock(PMLStatement ... statements) {
        stmts = new ArrayList<>(List.of(statements));
    }

    public List<PMLStatement> getStmts() {
        return stmts;
    }

    public void setStmts(List<PMLStatement> stmts) {
        this.stmts = stmts;
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

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        return ctx.executeStatements(stmts, Map.of());
    }
}
