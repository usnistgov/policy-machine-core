package gov.nist.csd.pm.core.pap.pml.expression;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLExecutionException;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OperationInvokeExpression<T> extends Expression<T> {

    private final String name;
    private final List<Expression<?>> actualArgsList;
    private final Type<T> expectedReturnType;

    public OperationInvokeExpression(String name,
                                     List<Expression<?>> actualArgsList,
                                     Type<T> expectedReturnType) {
        this.name = name;
        this.actualArgsList = actualArgsList;
        this.expectedReturnType = expectedReturnType;
    }

    public String getName() {
        return name;
    }

    public List<Expression<?>> getActualArgsList() {
        return actualArgsList;
    }

    public Type<T> getExpectedReturnType() {
        return expectedReturnType;
    }

    @Override
    public Type<T> getType() {
        return expectedReturnType;
    }

    @Override
    public T execute(ExecutionContext ctx, PAP pap) throws PMException {
        ExecutionContext funcInvokeCtx = ctx.copy();
        Operation<?> operation = funcInvokeCtx.scope().getOperation(name);

        // ensure the function return type matches the expected return type
        Type<?> funcReturnType = operation.getReturnType();
        if (!funcReturnType.isCastableTo(expectedReturnType)) {
            throw new PMException("expected return type " + expectedReturnType +
                " but function " + operation.getName() + " returns " + funcReturnType);
        }

        Args actualArgValues = prepareArgExpressions(funcInvokeCtx, pap, operation);

        // set the ctx if PML function
        if (operation instanceof PMLOperation pmlOperation) {
            pmlOperation.setCtx(funcInvokeCtx.copyWithParentScope());
        }

        Object result = pap.executeOperation(operation, actualArgValues);
        Type<?> actualType = Type.resolveTypeOfObject(result);

        if (!actualType.isCastableTo(expectedReturnType)) {
            throw new PMException("Function return type mismatch. Expected " + expectedReturnType + " but got " + actualType);
        }

        return expectedReturnType.cast(result);
    }

    private Args prepareArgExpressions(ExecutionContext ctx, PAP pap, Operation<?> function) throws PMException {
        List<FormalParameter<?>> formalParams = function.getFormalParameters();

        if (actualArgsList.size() != formalParams.size()) {
            throw new PMLExecutionException("expected " + formalParams.size() + " args for function \""
                + name + "\", got " + formalParams.size());
        }

        Map<FormalParameter<?>, Object> values = new HashMap<>();
        for (int i = 0; i < formalParams.size(); i++) {
            FormalParameter<?> formalParam = formalParams.get(i);
            Expression<?> argExpr = actualArgsList.get(i);
            Object argValue = argExpr.execute(ctx, pap);

            if (!argExpr.getType().isCastableTo(formalParam.getType())) {
                throw new PMLExecutionException("expected type " + formalParam.getType() + ", got type " + argExpr.getType());
            }

            values.put(formalParam, argValue);
        }

        return new Args(values);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("%s%s(%s)", indent(indentLevel), name, argsToString());
    }

    private String argsToString() {
        StringBuilder s = new StringBuilder();
        for (Expression<?> arg : actualArgsList) {
            if (!s.isEmpty()) {
                s.append(", ");
            }
            s.append(arg);
        }

        return s.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperationInvokeExpression<?> that)) {
            return false;
        }
        return Objects.equals(name, that.name) && Objects.equals(actualArgsList, that.actualArgsList)
            && Objects.equals(expectedReturnType, that.expectedReturnType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, actualArgsList, expectedReturnType);
    }
}
