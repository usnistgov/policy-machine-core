package gov.nist.ngac.pm.core.pap.pml.operation.query;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementSerializable;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;
import java.util.Objects;

/**
 * A PML "create query operation" definition's runtime form: executes its {@link PMLStatementBlock} body
 * and casts the result to the declared return type.
 */
public class PMLStmtsQueryOperation<T> extends PMLQueryOperation<T> implements PMLStatementSerializable {

    private PMLStatementBlock stmts;

    public PMLStmtsQueryOperation(String operationName,
                                  Type<T> returnType,
                                  List<FormalParameter<?>> parameters,
                                  List<RequiredCapability> requiredCapabilities,
                                  PMLStatementBlock stmts) {
        super(operationName, returnType, parameters, requiredCapabilities);
        this.stmts = stmts;
    }

    public PMLStmtsQueryOperation(String operationName,
                                  Type<T> returnType,
                                  List<FormalParameter<?>> parameters,
                                  List<FormalParameter<?>> eventParameters,
                                  List<RequiredCapability> requiredCapabilities,
                                  PMLStatementBlock stmts) {
        super(operationName, returnType, parameters, eventParameters, requiredCapabilities);
        this.stmts = stmts;
    }

    public PMLStatementBlock getStmts() {
        return stmts;
    }

    @Override
    public T execute(PolicyQuery policyQuery, UserContext userCtx, Args args) throws PMException {
        ExecutionContext ctx = getCtx();

        Object result = ctx.executeOperationStatements(stmts.getStmts(), args);

        return getReturnType().cast(result);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format(
            "%s%s",
            getSignature().toFormattedString(indentLevel),
            stmts.toFormattedString(indentLevel)
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
