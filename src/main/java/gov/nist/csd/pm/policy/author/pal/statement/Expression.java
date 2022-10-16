package gov.nist.csd.pm.policy.author.pal.statement;

import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.compiler.Position;
import gov.nist.csd.pm.policy.author.pal.compiler.Variable;
import gov.nist.csd.pm.policy.author.pal.compiler.error.CompileError;
import gov.nist.csd.pm.policy.author.pal.compiler.visitor.FunctionCallVisitor;
import gov.nist.csd.pm.policy.author.pal.compiler.visitor.LiteralExprVisitor;
import gov.nist.csd.pm.policy.author.pal.compiler.visitor.VariableReferenceVisitor;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.exception.PALCompilationException;
import gov.nist.csd.pm.policy.author.pal.model.expression.*;
import gov.nist.csd.pm.policy.author.pal.model.scope.Scope;
import gov.nist.csd.pm.policy.author.pal.model.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.policy.author.pal.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Expression extends PALStatement {

    public static Expression compile(VisitorContext visitorCtx,
                                     PALParser.ExpressionContext expressionCtx,
                                     Type... allowedTypes) {
        Expression expression;
        if (expressionCtx.literal() != null) {
            Literal literal = new LiteralExprVisitor(visitorCtx)
                    .visitLiteral(expressionCtx.literal());
            expression = new Expression(literal);
        } else if (expressionCtx.funcCall() != null) {
            FunctionStatement functionCall = new FunctionCallVisitor(visitorCtx)
                    .visitFuncCall(expressionCtx.funcCall());
            expression = new Expression(functionCall);
        } else {
            VariableReference varRef = new VariableReferenceVisitor(visitorCtx)
                    .visitVarRef(expressionCtx.varRef());
            expression = new Expression(varRef);
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

    public static Expression compileArray(VisitorContext visitorCtx,
                                          PALParser.ExpressionArrayContext expressionArrayCtx, Type allowedType) {
        // can be an array of the allowed type
        // or a function or variable that returns the allowed type - but can ONLY be 1

        List<PALParser.ExpressionContext> expressionCtxs = expressionArrayCtx.expression();
        List<Expression> expressions = new ArrayList<>();
        for (PALParser.ExpressionContext expressionCtx : expressionCtxs) {
            Expression compiledExpr = compile(visitorCtx, expressionCtx);
            expressions.add(compiledExpr);
        }

        Expression compiled = new Expression();
        if (expressions.size() > 1) {
            for (Expression expression : expressions) {
                Type exprType = Type.any();
                try {
                    exprType = expression.getType(visitorCtx.scope());
                } catch (UnknownFunctionInScopeException | UnknownVariableInScopeException e) {
                    visitorCtx.errorLog().addError(expressionArrayCtx, e.getMessage());
                }

                if (!exprType.equals(allowedType)) {
                    visitorCtx.errorLog().addError(expressionArrayCtx, String.format("expected expression array of type %s but got %s", allowedType, exprType));
                }
            }

            compiled = new Expression(expressions);
        } else {
            Expression expression = expressions.get(0);
            Type exprType = Type.any();
            try {
                exprType = expression.getType(visitorCtx.scope());
            } catch (UnknownFunctionInScopeException | UnknownVariableInScopeException e) {
                visitorCtx.errorLog().addError(expressionArrayCtx, e.getMessage());
            }

            // if the type of the only expression equals the allowed type then return an expression array with the single expression as an element
            // if the type is an array of the allowed type (variable or function) then return the expression itself
            if (exprType.equals(allowedType)) {
                return new Expression(expression);
            } else if (exprType.equals(Type.array(allowedType))) {
                return expression;
            } else {
                visitorCtx.errorLog().addError(expressionArrayCtx, String.format("expected expression array of type %s but got %s", allowedType, exprType));
            }
        }

        return compiled;
    }

    private VariableReference variableReference;
    private boolean isVariableReference;
    private FunctionStatement functionCall;
    private boolean isFunctionCall;
    private Literal literal;
    private boolean isLiteral;
    private List<Expression> exprList;
    private boolean isList;

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

    public Expression(Expression ... exprs) {
        this.exprList = new ArrayList<>(List.of(exprs));
        this.isList = true;
    }

    public Expression(List<Expression> exprs) {
        this.exprList = exprs;
        this.isList = true;
    }

    public Expression() {}

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

    public List<Expression> getExprList() {
        return exprList;
    }

    public boolean isList() {
        return isList;
    }

    @Override
    public String toString() {
        if (isFunctionCall) {
            return functionCall.toString();
        } else if (isLiteral) {
            return literal.toString();
        } else if (isVariableReference){
            return variableReference.toString();
        } else {
            StringBuilder s = new StringBuilder();
            for (Expression e : exprList) {
                if (s.length() > 0) {
                    s.append(", ");
                }

                s.append(e.toString());
            }

            return s.toString();
        }
    }

    public Type getType(Scope Scope) throws UnknownFunctionInScopeException, UnknownVariableInScopeException {
        if (isFunctionCall) {
            return getFunctionCallType(Scope);
        } else if (isLiteral) {
            return getLiteralType();
        } else {
            return getVarRefType(Scope);
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

        List<Value> values = new ArrayList<>();
        for (Expression name : exprList) {
            values.add(name.execute(ctx, policyAuthor));
        }

        return new Value(values.toArray(Value[]::new));
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

