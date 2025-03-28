package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.exception.PMLExecutionException;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.arg.PMLActualArgs;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.PMLFunction;
import gov.nist.csd.pm.pap.pml.function.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.function.operation.PMLOperationWrapper;
import gov.nist.csd.pm.pap.pml.function.routine.PMLRoutine;
import gov.nist.csd.pm.pap.pml.function.routine.PMLRoutineWrapper;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ReturnValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.*;

public class FunctionInvokeExpression extends Expression {

    public static Expression compileFunctionInvokeExpression(VisitorContext visitorCtx,
                                                             PMLParser.FunctionInvokeExpressionContext functionInvokeExpressionContext) {
        PMLParser.FunctionInvokeContext functionInvokeContext = functionInvokeExpressionContext.functionInvoke();
        return compileFunctionInvoke(visitorCtx, functionInvokeContext);
    }

    public static Expression compileFunctionInvoke(VisitorContext visitorCtx, PMLParser.FunctionInvokeContext functionInvokeContext) {
        String funcName = functionInvokeContext.ID().getText();

        PMLFunctionSignature signature;
        try {
            signature = visitorCtx.scope().getFunction(funcName);
        } catch (UnknownFunctionInScopeException e) {
            throw new PMLCompilationRuntimeException(functionInvokeContext, e.getMessage());
        }

        PMLParser.FunctionInvokeArgsContext funcCallArgsCtx = functionInvokeContext.functionInvokeArgs();
        List<PMLParser.ExpressionContext> argExpressions =  new ArrayList<>();
        PMLParser.ExpressionListContext expressionListContext = funcCallArgsCtx.expressionList();
        if (expressionListContext != null) {
            argExpressions = expressionListContext.expression();
        }

        List<PMLFormalArg> formalArgs = signature.getFormalArgs();
        if (formalArgs.size() != argExpressions.size()) {
            throw new PMLCompilationRuntimeException(
                functionInvokeContext,
                "wrong number of args for signature call " + funcName + ": " +
                    "expected " + formalArgs.size() + ", got " + argExpressions.size()
            );
        }

        List<Expression> operands = new ArrayList<>();
        for (int i = 0; i < formalArgs.size(); i++) {
            PMLParser.ExpressionContext exprCtx = argExpressions.get(i);
            PMLFormalArg formalArg = formalArgs.get(i);

            Expression expr = Expression.compile(visitorCtx, exprCtx, formalArg.getPmlType());
            operands.add(expr);
        }

        return new FunctionInvokeExpression(funcName, operands);
    }

    private final String funcName;
    private final List<Expression> actualArgsList;

    public FunctionInvokeExpression(String funcName, List<Expression> actualArgsList) {
        this.funcName = funcName;
        this.actualArgsList = actualArgsList;
    }

    public String getFuncName() {
        return funcName;
    }

    public List<Expression> getActualArgsList() {
        return actualArgsList;
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        ExecutionContext funcInvokeCtx = ctx.copy();

        // set the execution context if exec is a PML exec
        AdminFunction<?> function = funcInvokeCtx.scope().getFunction(funcName);

        Map<String, Value> actualOperandValues = prepareOperandExpressions(funcInvokeCtx, pap,
            function);

        // set the ctx if PML function
        if (function instanceof PMLRoutine pmlRoutine) {
            pmlRoutine.setCtx(funcInvokeCtx.copyWithParentScope());
        } else if (function instanceof PMLOperation pmlOperation) {
            pmlOperation.setCtx(funcInvokeCtx.copyWithParentScope());
        }

        ActualArgs args;
        if ((function instanceof PMLOperationWrapper) || (function instanceof PMLRoutineWrapper)) {
            // PMLWrappers do not need PML Values as input, just regular objects
            args = new ActualArgs(valuesMapToObjects(actualOperandValues));
        } else {
            // PML functions do expect Values as input
            args = new PMLActualArgs(actualOperandValues);
        }

        // execute the function
        Object o = pap.executeAdminFunction(function, args);

        // return the value
        Value value = Value.fromObject(o);
        if (value instanceof ReturnValue returnValue) {
            return returnValue.getValue();
        } else {
            return value;
        }
    }

    private Map<String, Object> valuesMapToObjects(Map<String, Value> valuesMap) {
        Map<String, Object> objectMap = new HashMap<>();
        for (Map.Entry<String, Value> entry : valuesMap.entrySet()) {
            objectMap.put(entry.getKey(), entry.getValue().toObject());
        }

        return objectMap;
    }

    private Map<String, Value> prepareOperandExpressions(ExecutionContext ctx, PAP pap, AdminFunction<?> function)
    throws PMException {
        List<FormalArg<?>> formalArgs = function.getFormalArgs();

        if (actualArgsList.size() != formalArgs.size()) {
            throw new PMLExecutionException("expected " + formalArgs.size() + " args for function \""
                + funcName + "\", got " + formalArgs.size());
        }

        List<PMLFormalArg> pmlFormalArgs = switch (function) {
            case PMLRoutine pmlRoutine -> pmlRoutine.getPmlFormalArgs();
            case PMLFunction pmlFunction -> pmlFunction.getPmlFormalArgs();
            case PMLOperation pmlOperation -> pmlOperation.getPmlFormalArgs();
            default -> throw new PMException("unknown function type " + function.getClass().getName());
        };

        Map<String, Value> values = new HashMap<>();
        for (int i = 0; i < pmlFormalArgs.size(); i++) {
            PMLFormalArg formalArg = pmlFormalArgs.get(i);
            Expression operandExpr = actualArgsList.get(i);
            Value argValue = operandExpr.execute(ctx, pap);


            if (!argValue.getType().equals(formalArg.getPmlType())) {
                throw new PMLExecutionException("expected type " + formalArg.getType() + " for arg "
                    + formalArg.getName() + " for function \"" + funcName + "\", got type " + argValue.getType());
            }

            values.put(formalArg.getName(), argValue);
        }

        return values;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("%s%s(%s)", indent(indentLevel), funcName, argsToString());
    }

    private String argsToString() {
        StringBuilder s = new StringBuilder();
        for (Expression arg : actualArgsList) {
            if (!s.isEmpty()) {
                s.append(", ");
            }
            s.append(arg);
        }

        return s.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionInvokeExpression that)) return false;
        return Objects.equals(funcName, that.funcName) && Objects.equals(
            actualArgsList, that.actualArgsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(funcName, actualArgsList);
    }

    @Override
    public Type getType(Scope<Variable, PMLFunctionSignature> scope) throws PMLScopeException {
        return scope.getFunction(funcName).getReturnType();
    }
}
