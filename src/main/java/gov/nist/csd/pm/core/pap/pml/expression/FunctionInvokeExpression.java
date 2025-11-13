package gov.nist.csd.pm.core.pap.pml.expression;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.AdminFunction;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLExecutionException;

import gov.nist.csd.pm.core.pap.pml.function.PMLFunction;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;

import java.util.*;

public class FunctionInvokeExpression<T> extends Expression<T> {

    private final PMLFunctionSignature functionSignature;
    private final List<Expression<?>> actualArgsList;
    private final Type<T> expectedReturnType;

    public FunctionInvokeExpression(PMLFunctionSignature functionSignature,
                                    List<Expression<?>> actualArgsList,
                                    Type<T> expectedReturnType) {
        this.functionSignature = functionSignature;
        this.actualArgsList = actualArgsList;
        this.expectedReturnType = expectedReturnType;
    }

    public PMLFunctionSignature getFunctionSignature() {
        return functionSignature;
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
        AdminFunction<?> function = funcInvokeCtx.scope().getFunction(functionSignature.getName());
        Map<String, Object> actualArgValues = prepareArgExpressions(funcInvokeCtx, pap, function);

        // set the ctx if PML function
        if (function instanceof PMLFunction pmlFunction) {
            pmlFunction.setCtx(funcInvokeCtx.copyWithParentScope());
        }

        // execute the function
        return (T) pap.executeAdminFunction(function, actualArgValues);
    }

    private Map<String, Object> prepareArgExpressions(ExecutionContext ctx, PAP pap, AdminFunction<?> function) throws PMException {
        List<FormalParameter<?>> formalParams = function.getFormalParameters();

        if (actualArgsList.size() != formalParams.size()) {
            throw new PMLExecutionException("expected " + formalParams.size() + " args for function \""
                + functionSignature.getName() + "\", got " + formalParams.size());
        }

        Map<String, Object > values = new HashMap<>();
        for (int i = 0; i < formalParams.size(); i++) {
            FormalParameter<?> formalParam = formalParams.get(i);
            Expression<?> argExpr = actualArgsList.get(i);
            Object argValue = argExpr.execute(ctx, pap);

            if (!argExpr.getType().isCastableTo(formalParam.getType())) {
                throw new PMLExecutionException("expected type " + formalParam.getType() + ", got type " + argExpr.getType());
            }

            values.put(formalParam.getName(), argValue);
        }

        return values;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("%s%s(%s)", indent(indentLevel), functionSignature.getName(), argsToString());
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
        if (!(o instanceof FunctionInvokeExpression<?> that)) {
            return false;
        }
        return Objects.equals(functionSignature, that.functionSignature) && Objects.equals(
            actualArgsList, that.actualArgsList) && Objects.equals(expectedReturnType, that.expectedReturnType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionSignature, actualArgsList, expectedReturnType);
    }
}
