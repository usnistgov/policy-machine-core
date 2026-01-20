package gov.nist.csd.pm.core.pap.pml.operation.admin;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import java.util.List;
import java.util.Objects;

public class PMLStmtsAdminOperation<T> extends PMLAdminOperation<T> implements PMLStatementSerializable {

    private final PMLStatementBlock body;

    public PMLStmtsAdminOperation(String name,
                                  Type<T> returnType,
                                  List<FormalParameter<?>> formalParameters,
                                  PMLStatementBlock body) {
        super(name, returnType, formalParameters);
        this.body = body;
    }

    public PMLStatementBlock getBody() {
        return body;
    }

    @Override
    public T execute(PAP pap, Args args) throws PMException {
        ExecutionContext ctx = getCtx();

        Object result = ctx.executeOperationStatements(this.body.getStmts(), args);

        return getReturnType().cast(result);
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof PMLStmtsAdminOperation<?> that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), body);
    }
}
