package gov.nist.csd.pm.pap.pml.executable.operation;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PMLStmtsOperation extends PMLOperation implements PMLStatementSerializable {

    private PMLStmtsOperationBody body;

    public PMLStmtsOperation(String name,
                             Type returnType,
                             List<String> allOperands,
                             List<String> nodeOperands,
                             Map<String, Type> operandTypes,
                             PMLStmtsOperationBody body) {
        super(name, returnType, allOperands, nodeOperands, operandTypes);
        this.body = body;
    }

    public PMLStmtsOperationBody getBody() {
        return body;
    }

    @Override
    public PMLExecutableSignature getSignature() {
        return new PMLOperationSignature(
                getName(),
                getReturnType(),
                getOperandNames(),
                getNodeOperands(),
                getOperandTypes()
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        ctx.executeOperationStatements(this.body.getChecks().getStmts(), operands);
    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        ExecutionContext ctx = getCtx();

        return ctx.executeOperationStatements(this.body.getStatements().getStmts(), operands);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format(
                "%s%s",
                getSignature().toFormattedString(indentLevel),
                body.toFormattedString(indentLevel)
        );
    }

    @Override
    public String toString() {
        return toFormattedString(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PMLStmtsOperation)) return false;
	    PMLStmtsOperation that = (PMLStmtsOperation) o;
	    if (!super.equals(o)) return false;
	    return Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), body);
    }
}
