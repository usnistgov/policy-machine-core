package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.executable.AdminExecutable;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.arg.FormalArg;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.exception.PMLExecutionException;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.executable.function.PMLFunction;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperationWrapper;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutine;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutineWrapper;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.scope.UnknownExecutableInScopeException;
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

        PMLExecutableSignature signature;
        try {
            signature = visitorCtx.scope().getExecutable(funcName);
        } catch (UnknownExecutableInScopeException e) {
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
        AdminExecutable<?> executable = funcInvokeCtx.scope().getExecutable(funcName);

        Map<String, Value> actualOperandValues = prepareOperandExpressions(funcInvokeCtx, pap, executable);

        // set the ctx if PML executable
        if (executable instanceof PMLRoutine pmlRoutine) {
            pmlRoutine.setCtx(funcInvokeCtx.copyWithParentScope());
        } else if (executable instanceof PMLOperation pmlOperation) {
            pmlOperation.setCtx(funcInvokeCtx.copyWithParentScope());
        }

        // PMLWrappers dont need Values, just objects
        Map<String, Object> args;
        if ((executable instanceof PMLOperationWrapper) || (executable instanceof PMLRoutineWrapper)) {
            args = valuesMapToObjects(actualOperandValues);
        } else {
            args = new HashMap<>(actualOperandValues);
        }

        // execute the executable
        Object o = pap.executeAdminExecutable(executable, new ActualArgs(args));

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

    private Map<String, Value> prepareOperandExpressions(ExecutionContext ctx, PAP pap, AdminExecutable<?> executable)
    throws PMException {
        List<FormalArg<?>> formalArgs = executable.getFormalArgs();

        if (actualArgsList.size() != formalArgs.size()) {
            throw new PMLExecutionException("expected " + formalArgs.size() + " args for function \""
                + funcName + "\", got " + formalArgs.size());
        }

        List<PMLFormalArg> pmlFormalArgs = switch (executable) {
            case PMLRoutine pmlRoutine -> pmlRoutine.getPmlFormalArgs();
            case PMLFunction pmlFunction -> pmlFunction.getPmlFormalArgs();
            case PMLOperation pmlOperation -> pmlOperation.getPmlFormalArgs();
            default -> throw new PMException("unknown executable type " + executable.getClass().getName());
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
    public Type getType(Scope<Variable, PMLExecutableSignature> scope) throws PMLScopeException {
        return scope.getExecutable(funcName).getReturnType();
    }
}
