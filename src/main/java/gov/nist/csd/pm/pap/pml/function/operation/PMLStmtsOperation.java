package gov.nist.csd.pm.pap.pml.function.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PMLStmtsOperation extends PMLOperation implements PMLStatementSerializable {

    private final CheckAndStatementsBlock body;

    public PMLStmtsOperation(String name,
                             ArgType<?> returnType,
                             List<FormalParameter<?>> formalParameters,
                             CheckAndStatementsBlock body) {
        super(name, returnType, formalParameters);
        this.body = body;
    }

    public CheckAndStatementsBlock getBody() {
        return body;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        ExecutionContext ctx = getCtx();

        ctx.executeOperationStatements(this.body.getChecks().getStmts(), args);
    }

    @Override
    protected Args prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        return new Args(argsMap);
    }

    @Override
    public Object execute(PAP pap, Args args) throws PMException {
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
