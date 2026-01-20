package gov.nist.csd.pm.core.pap.pml.operation.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;
import java.util.Objects;

public class PMLStmtsQueryOperation<T> extends PMLQueryOperation<T> implements PMLStatementSerializable {

    private PMLStatementBlock stmts;

    public PMLStmtsQueryOperation(String operationName,
                                  Type<T> returnType,
                                  List<FormalParameter<?>> parameters,
                                  PMLStatementBlock stmts) {
        super(operationName, returnType, parameters);
        this.stmts = stmts;
    }

    public PMLStatementBlock getStmts() {
        return stmts;
    }

    @Override
    public T execute(PolicyQuery policyQuery, Args args) throws PMException {
        ExecutionContext ctx = getCtx();

        Object result = ctx.executeOperationStatements(stmts.getStmts(), args);

        return getReturnType().cast(result);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return stmts.toFormattedString(indentLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PMLStmtsQueryOperation<?> that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(stmts, that.stmts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), stmts);
    }
}
