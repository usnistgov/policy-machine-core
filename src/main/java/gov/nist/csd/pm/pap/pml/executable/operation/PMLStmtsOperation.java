package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PMLStmtsOperation extends PMLOperation implements PMLStatementSerializable {

    private PMLStatementBlock checks;
    private PMLStatementBlock statements;

    public PMLStmtsOperation(String name,
                             Type returnType,
                             List<String> allOperands,
                             List<String> nodeOperands,
                             Map<String, Type> operandTypes,
                             PMLStatementBlock checks,
                             PMLStatementBlock statements) {
        super(name, returnType, allOperands, nodeOperands, operandTypes);
        this.checks = checks;
        this.statements = statements;
    }

    public PMLStatementBlock getChecks() {
        return checks;
    }

    public PMLStatementBlock getStatements() {
        return statements;
    }

    @Override
    public PMLExecutableSignature getSignature() {
        return new PMLStmtsOperationSignature(
                getName(),
                getReturnType(),
                getOperandNames(),
                getNodeOperands(),
                getOperandTypes(),
                getChecks()
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {
        ctx.executeOperationStatements(getChecks().getStmts(), operands);
    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        ExecutionContext ctx = getCtx();

        return ctx.executeOperationStatements(statements.getStmts(), operands);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format(
                "%s%s",
                getSignature().toFormattedString(indentLevel),
                getStatements().toFormattedString(indentLevel)
        );
    }

    @Override
    public String toString() {
        return toFormattedString(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PMLStmtsOperation that = (PMLStmtsOperation) o;
        return Objects.equals(statements, that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), statements);
    }
}
