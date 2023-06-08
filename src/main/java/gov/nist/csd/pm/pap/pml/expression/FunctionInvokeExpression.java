package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.AdminExecutable;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLExecutionException;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperationWrapper;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutineWrapper;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLRoutine;
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

        PMLExecutableSignature signature;
        try {
            signature = visitorCtx.scope().getFunction(funcName);
        } catch (UnknownFunctionInScopeException e) {
            throw new PMLCompilationRuntimeException(functionInvokeContext, e.getMessage());
        }

        List<String> operandNames = signature.getOperands();
        Map<String, Type> operandTypes = signature.getOperandTypes();
        PMLParser.FunctionInvokeArgsContext funcCallArgsCtx = functionInvokeContext.functionInvokeArgs();
        List<PMLParser.ExpressionContext> argExpressions =  new ArrayList<>();
        PMLParser.ExpressionListContext expressionListContext = funcCallArgsCtx.expressionList();
        if (expressionListContext != null) {
            argExpressions = expressionListContext.expression();
        }

        if (operandNames.size() != argExpressions.size()) {
            throw new PMLCompilationRuntimeException(
                    functionInvokeContext,
                    "wrong number of args for signature call " + funcName + ": " +
                            "expected " + operandNames.size() + ", got " + argExpressions.size()
            );
        }

        Map<String, Expression> operands = new HashMap<>();
        for (int i = 0; i < operandNames.size(); i++) {
            PMLParser.ExpressionContext exprCtx = argExpressions.get(i);
            String operand = operandNames.get(i);
            Type operandType = operandTypes.get(operand);

            Expression expr = Expression.compile(visitorCtx, exprCtx, operandType);
            operands.put(operand, expr);
        }

        return new FunctionInvokeExpression(signature, operands);
    }

    private PMLExecutableSignature signature;
    private List<Expression> actualArgsList;
    private Map<String, Expression> operands;

    public FunctionInvokeExpression(PMLExecutableSignature signature, Map<String, Expression> actualOperands) {
        this.signature = signature;
        this.actualArgsList = getActualArgsList(this.signature, actualOperands);
        this.operands = new HashMap<>(actualOperands);
    }

    public PMLExecutableSignature getSignature() {
        return signature;
    }

    public List<Expression> getActualArgsList() {
        return actualArgsList;
    }

    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        String name = signature.getFunctionName();
        ExecutionContext funcInvokeCtx = ctx.copy();
        Map<String, Value> operandValues = prepareOperandExpressions(ctx, pap);

        // set the execution context if exec is a PML exec
        AdminExecutable<?> executable = funcInvokeCtx.scope().getFunction(name);
        if (executable instanceof PMLRoutine pmlRoutine) {
            pmlRoutine.setCtx(funcInvokeCtx);
        } else if (executable instanceof PMLOperation pmlOperation) {
            pmlOperation.setCtx(funcInvokeCtx);
        }

        // PMLWrappers dont need Values, just objects
        Map<String, Object> operands;
        if ((executable instanceof PMLOperationWrapper) || (executable instanceof PMLRoutineWrapper)) {
            operands = valuesMapToObjects(operandValues);
        } else {
            operands = new HashMap<>(operandValues);
        }

        // execute the executable
        Object o = pap.executeAdminExecutable(executable, operands);

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

    private Map<String, Value> prepareOperandExpressions(ExecutionContext ctx, PAP pap)
            throws PMException {
        String funcName = signature.getFunctionName();
        List<String> operandsNames = signature.getOperands();

        if (operands.size() != operandsNames.size()) {
            throw new PMLExecutionException("expected " + operandsNames.size() + " args for function \""
                    + funcName + "\", got " + operands.size());
        }

        Map<String, Value> values = new HashMap<>();
        for (int i = 0; i < operandsNames.size(); i++) {
            String operand = operandsNames.get(i);
            Type operandType = signature.getOperandTypes().get(operand);
            Expression operandExpr = operands.get(operand);
            Value argValue = operandExpr.execute(ctx, pap);

            if (!argValue.getType().equals(operandType)) {
                throw new PMLExecutionException("expected " + operandType + " for arg " + i + " for function \""
                        + funcName + "\", got " + argValue.getType());
            }

            values.put(operand, argValue);
        }

        return values;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("%s%s(%s)", indent(indentLevel), signature.getFunctionName(), argsToString());
    }

    private String argsToString() {
        StringBuilder s = new StringBuilder();
        for (Expression arg : actualArgsList) {
            if (s.length() > 0) {
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
        return Objects.equals(signature, that.signature) && Objects.equals(actualArgsList, that.actualArgsList) && Objects.equals(operands, that.operands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(signature, actualArgsList, operands);
    }

    @Override
    public Type getType(Scope<Variable, PMLExecutableSignature> scope) throws PMLScopeException {
        return signature.getReturnType();
    }

    private List<Expression> getActualArgsList(PMLExecutableSignature signature, Map<String, Expression> args) {
        List<String> pmlOperandDefs = signature.getOperands();
        List<Expression> actualArgs = new ArrayList<>();
        for (String operandDef : pmlOperandDefs) {
            actualArgs.add(args.get(operandDef));
        }

        return actualArgs;
    }
}
