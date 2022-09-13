package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.compiler.Variable;
import gov.nist.csd.pm.policy.author.pal.compiler.VisitorScope;
import gov.nist.csd.pm.policy.author.pal.compiler.visitor.FunctionCallVisitor;
import gov.nist.csd.pm.policy.author.pal.compiler.visitor.LiteralExprVisitor;
import gov.nist.csd.pm.policy.author.pal.compiler.visitor.VariableReferenceVisitor;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Expression extends PALStatement {

    public static Expression compile(VisitorContext visitorCtx,
                                     PALParser.ExpressionContext expressionCtx,
                                     Type... allowedTypes) {
        Expression expression;
        if (expressionCtx instanceof PALParser.VariableReferenceContext varRefCtx) {
            VariableReference varRef = new VariableReferenceVisitor(visitorCtx)
                    .visitVariableReference(varRefCtx);
            expression = new Expression(varRef);

        } else if (expressionCtx instanceof PALParser.FunctionCallContext funcCallCtx) {
            FunctionStatement functionCall = new FunctionCallVisitor(visitorCtx)
                    .visitFunctionCall(funcCallCtx);
            expression = new Expression(functionCall);

        } else if (expressionCtx instanceof PALParser.LiteralExprContext literalExprCtx) {
            Literal literal = new LiteralExprVisitor(visitorCtx)
                    .visitLiteralExpr(literalExprCtx);
            expression = new Expression(literal);

        } else {
            expression = new Expression(new Literal(""));
            visitorCtx.errorLog().addError(
                    expressionCtx,
                    "expression is not a variable reference, function call, or literal"
            );
        }

        Type type = expression.getType(visitorCtx.scope());

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
    private FunctionStatement functionCall;
    private boolean isFunctionCall;
    private Literal literal;
    private boolean isLiteral;

    public Expression(VariableReference variableReference) {
        this.variableReference = variableReference;
        this.isVariableReference = true;
    }

    public Expression(FunctionStatement functionCall) {
        this.functionCall = functionCall;
        this.isFunctionCall = true;
    }

    public Expression(Literal literal) {
        this.literal = literal;
        this.isLiteral = true;
    }

    public VariableReference getVariableReference() {
        return variableReference;
    }

    public boolean isVariableReference() {
        return isVariableReference;
    }

    public FunctionStatement getFunctionCall() {
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

    public Type getType(VisitorScope visitorScope) {
        if (isFunctionCall) {
            return getFunctionCallType(visitorScope);
        } else if (isLiteral) {
            return getLiteralType();
        } else {
            return getVarRefType(visitorScope);
        }
    }

    private Type getVarRefType(VisitorScope visitorScope) {
        if (variableReference.isID()) {
            return getIDType(visitorScope, variableReference);
        } else {
            return variableReference.getType();
        }
    }

    private Type getIDType(VisitorScope visitorScope, VariableReference variableReference) {
        Variable variable = visitorScope.getVariable(variableReference.getID());
        if (variable == null) {
            return null;
        }

        return variable.type();
    }

    private Type getFunctionCallType(VisitorScope visitorScope) {
        String functionName = functionCall.getFunctionName();
        FunctionDefinitionStatement function = visitorScope.getFunction(functionName);
        if (function == null) {
            return null;
        }

        return function.getReturnType();
    }

    private Type getLiteralType() {
        if (literal.isStringLiteral()) {
            return Type.string();
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
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        if (isLiteral) {
            return literal.execute(ctx, policyAuthor);
        } else if (isFunctionCall) {
            return functionCall.execute(ctx, policyAuthor);
        } else if (isVariableReference) {
            return variableReference.execute(ctx, policyAuthor);
        }

        return new Value();
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

