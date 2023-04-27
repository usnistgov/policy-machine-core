package gov.nist.csd.pm.policy.pml.statement;

import com.mysql.cj.log.Log;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.compiler.visitor.FunctionInvokeVisitor;
import gov.nist.csd.pm.policy.pml.compiler.visitor.LiteralExprVisitor;
import gov.nist.csd.pm.policy.pml.compiler.visitor.VariableReferenceVisitor;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.*;
import gov.nist.csd.pm.policy.pml.model.scope.Scope;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Expression extends PMLStatement {

    public static Expression compile(VisitorContext visitorCtx,
                                     PMLParser.ExpressionContext expressionCtx,
                                     Type... allowedTypes) {
        Expression expression;
        if (expressionCtx.literal() != null) {
            PMLParser.LiteralContext litCtx = expressionCtx.literal();
            Literal literal = new LiteralExprVisitor(visitorCtx)
                    .visitLiteral(litCtx);
            expression = new Expression(literal);
        } else if (expressionCtx.functionInvoke() != null) {
            FunctionInvocationStatement functionCall = new FunctionInvokeVisitor(visitorCtx)
                    .visitFunctionInvoke(expressionCtx.functionInvoke());
            expression = new Expression(functionCall);
        } else if (expressionCtx.variableReference() != null){
            VariableReference varRef = new VariableReferenceVisitor(visitorCtx)
                    .visitVariableReference(expressionCtx.variableReference());
            expression = new Expression(varRef);
        } else if (expressionCtx.AND_OP() != null || expressionCtx.OR_OP() != null) {
            Expression left = compile(visitorCtx, expressionCtx.left, Type.bool());
            Expression right = compile(visitorCtx, expressionCtx.right, Type.bool());

            expression = new Expression(new LogicalExpression(left, right, expressionCtx.AND_OP() != null));
        } else {
            Expression left = compile(visitorCtx, expressionCtx.left);
            Expression right = compile(visitorCtx, expressionCtx.right);

            expression = new Expression(new CompareExpression(left, right, expressionCtx.EQUALS_OP() != null));
        }

        Type type;
        try {
            type = expression.getType(visitorCtx.scope());
        } catch (UnknownFunctionInScopeException | UnknownVariableInScopeException e) {
            visitorCtx.errorLog().addError(expressionCtx, e.getMessage());
            type = Type.any();
        }

        // check the expression type is part of the given allowed types
        // if no types are given then any type is allowed
        List<Type> allowedTypesList = Arrays.asList(allowedTypes);
        if (!allowedTypesList.isEmpty()
                && !allowedTypesList.contains(type)) {
            visitorCtx.errorLog().addError(
                    expressionCtx,
                    "expression type " + type + " not allowed, only: " + allowedTypesList
            );
        }

        return expression;
    }

    private VariableReference variableReference;
    private boolean isVariableReference;
    private FunctionInvocationStatement functionCall;
    private boolean isFunctionCall;
    private Literal literal;
    private boolean isLiteral;
    private CompareExpression compareExpression;
    private boolean isCompareExpression;
    private LogicalExpression logicalExpression;
    private boolean isLogicalExpression;

    public Expression(VariableReference variableReference) {
        this.variableReference = variableReference;
        this.isVariableReference = true;
    }

    public Expression(FunctionInvocationStatement functionCall) {
        this.functionCall = functionCall;
        this.isFunctionCall = true;
    }

    public Expression(Literal literal) {
        this.literal = literal;
        this.isLiteral = true;
    }

    public Expression(CompareExpression compareExpression) {
        this.compareExpression = compareExpression;
        this.isCompareExpression = true;
    }

    public Expression(LogicalExpression logicalExpression) {
        this.logicalExpression = logicalExpression;
        this.isLogicalExpression = true;
    }

    public Expression() {}

    public VariableReference getVariableReference() {
        return variableReference;
    }

    public boolean isVariableReference() {
        return isVariableReference;
    }

    public FunctionInvocationStatement getFunctionCall() {
        return functionCall;
    }

    public boolean isFunctionCall() {
        return isFunctionCall;
    }

    public Literal getLiteral() {
        return literal;
    }

    public boolean isLiteral() {
        return isLiteral;
    }

    @Override
    public String toString() {
        if (isFunctionCall) {
            return functionCall.toString();
        } else if (isLiteral) {
            return literal.toString();
        } else {
            return variableReference.toString();
        }
    }

    public Type getType(Scope Scope) throws UnknownFunctionInScopeException, UnknownVariableInScopeException {
        if (isFunctionCall) {
            return getFunctionCallType(Scope);
        } else if (isLiteral) {
            return getLiteralType();
        } else if (isVariableReference){
            return getVarRefType(Scope);
        } else {// (isLogicalExpression || isCompareExpression) {
            return Type.bool();
        }
    }

    private Type getVarRefType(Scope Scope) throws UnknownVariableInScopeException {
        if (variableReference.isID()) {
            return getIDType(Scope, variableReference);
        } else {
            return variableReference.getType();
        }
    }

    private Type getIDType(Scope Scope, VariableReference variableReference) throws UnknownVariableInScopeException {
        Variable variable = Scope.getVariable(variableReference.getID());
        if (variable == null) {
            return null;
        }

        return variable.type();
    }

    private Type getFunctionCallType(Scope Scope) throws UnknownFunctionInScopeException {
        String functionName = functionCall.getFunctionName();
        FunctionDefinitionStatement function = Scope.getFunction(functionName);
        if (function == null) {
            return null;
        }

        return function.getReturnType();
    }

    private Type getLiteralType() {
        if (literal.isStringLiteral()) {
            return Type.string();
        } else if (literal.isNumberLiteral()) {
            return Type.number();
        } else if (literal.isBooleanLiteral()) {
            return Type.bool();
        } else if (literal.isArrayLiteral()) {
            return literal.getArrayLiteral().getType();
        }else if (literal.isMapLiteral()) {
            return literal.getMapLiteral().getType();
        } else {
            return Type.any();
        }
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        if (isLiteral) {
            return literal.execute(ctx, policy);
        } else if (isFunctionCall) {
            return functionCall.execute(ctx, policy);
        } else if (isVariableReference){
            return variableReference.execute(ctx, policy);
        } else if (isCompareExpression) {
            return compareExpression.execute(ctx, policy);
        } else {
            return logicalExpression.execute(ctx, policy);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expression that = (Expression) o;
        return isVariableReference == that.isVariableReference
                && isFunctionCall == that.isFunctionCall
                && isLiteral == that.isLiteral
                && Objects.equals(variableReference, that.variableReference)
                && Objects.equals(functionCall, that.functionCall)
                && Objects.equals(literal, that.literal);
    }

    @Override
    public int hashCode() {
        if (isVariableReference) {
            return variableReference.hashCode();
        } else if (isFunctionCall) {
            return functionCall.hashCode();
        } else {
            return literal.hashCode();
        }
    }
}

