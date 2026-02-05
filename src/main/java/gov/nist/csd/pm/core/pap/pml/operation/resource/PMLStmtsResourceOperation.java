package gov.nist.csd.pm.core.pap.pml.operation.resource;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;
import java.util.Objects;

public class PMLStmtsResourceOperation<T> extends PMLResourceOperation<T> implements PMLStatementSerializable {

    private final PMLStatementBlock body;

    public PMLStmtsResourceOperation(String name,
                                  Type<T> returnType,
                                  List<FormalParameter<?>> formalParameters,
                                  List<RequiredCapability> requiredCapabilities,
                                  PMLStatementBlock body) {
        super(name, returnType, formalParameters, requiredCapabilities);
        this.body = body;
    }

    public PMLStatementBlock getBody() {
        return body;
    }

    @Override
    public T execute(PolicyQuery query, Args args) throws PMException {
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
        if (!(o instanceof PMLStmtsResourceOperation<?> that)) {
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
