package gov.nist.csd.pm.pap.pml.function.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Objects;

public class PMLStmtsOperation extends PMLOperation implements PMLStatementSerializable {

    private final CheckAndStatementsBlock body;

    public PMLStmtsOperation(String name,
                             Type returnType,
                             List<PMLFormalArg> formalArgs,
                             CheckAndStatementsBlock body) {
        super(name, returnType, formalArgs);
        this.body = body;
    }

    public CheckAndStatementsBlock getBody() {
        return body;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args operands) throws PMException {
        ctx.executeOperationStatements(this.body.getChecks().getStmts(), operands);
    }

    @Override
    public Value execute(PAP pap, Args args) throws PMException {
        ExecutionContext ctx = getCtx();

        return ctx.executeOperationStatements(this.body.getStatements().getStmts(), args);
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
        if (!(o instanceof PMLStmtsOperation that)) return false;
        if (!super.equals(o)) return false;
	    return Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), body);
    }
}
