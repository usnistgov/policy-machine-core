package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.pap.pml.PMLErrorHandler;
import gov.nist.csd.pm.pap.pml.antlr.PMLLexer;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.expression.literal.Literal;
import gov.nist.csd.pm.pap.pml.expression.reference.VariableReference;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.scope.Scope;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Arrays;

public abstract class Expression extends PMLStatement {

    public static Expression fromString(VisitorContext visitorCtx, String input, Type expectedType) {
        PMLErrorHandler pmlErrorHandler = new PMLErrorHandler();

        PMLLexer lexer = new PMLLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(pmlErrorHandler);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(pmlErrorHandler);

        PMLParser.ExpressionContext exprCtx = parser.expression();

        Expression expression = compile(visitorCtx, exprCtx, expectedType);

        if (!visitorCtx.errorLog().getErrors().isEmpty()) {
            throw new PMLCompilationRuntimeException(pmlErrorHandler.getErrors());
        }

        return expression;
    }

    public static Expression compile(VisitorContext visitorCtx,
                                     PMLParser.ExpressionContext expressionCtx,
                                     Type ... expectedTypes) {
        Expression expression = null;

        if (expressionCtx instanceof PMLParser.VariableReferenceExpressionContext variableReferenceExpressionContext) {
            expression = VariableReference.compileVariableReference(visitorCtx, variableReferenceExpressionContext.variableReference());

        } else if (expressionCtx instanceof PMLParser.FunctionInvokeExpressionContext functionInvokeExpressionContext) {
            expression = FunctionInvokeExpression.compileFunctionInvokeExpression(visitorCtx, functionInvokeExpressionContext);

        } else if (expressionCtx instanceof PMLParser.LiteralExpressionContext literalExpressionContext) {
            expression = Literal.compileLiteral(visitorCtx, literalExpressionContext);

        } else if (expressionCtx instanceof PMLParser.NegateExpressionContext negatedExprContext) {
            expression = NegatedExpression.compileNegatedExpression(visitorCtx, negatedExprContext);

        } else if (expressionCtx instanceof PMLParser.PlusExpressionContext plusExpressionsContext) {
            expression = PlusExpression.compilePlusExpression(visitorCtx, plusExpressionsContext);

        } else if (expressionCtx instanceof PMLParser.EqualsExpressionContext equalsExpressionContext) {
            expression = EqualsExpression.compileEqualsExpression(visitorCtx, equalsExpressionContext);

        } else if (expressionCtx instanceof PMLParser.LogicalExpressionContext logicalExpressionsContext) {
            expression = LogicalExpression.compileLogicalExpression(visitorCtx, logicalExpressionsContext);

        } else if (expressionCtx instanceof PMLParser.ParenExpressionContext parenExpressionContext) {
            expression = ParenExpression.compileParenExpression(visitorCtx, parenExpressionContext.expression());

        }

        if (expression == null) {
            throw new PMLCompilationRuntimeException(expressionCtx, "unrecognized expression context");
        }

        Type expressionType;
        try {
            expressionType = expression.getType(visitorCtx.scope());
        } catch (PMLScopeException e) {
            throw new PMLCompilationRuntimeException(expressionCtx,e.getMessage());
        }

        // check the expression type is part of the given allowed types
        // if no types are given then any type is allowed
        boolean ok = expectedTypes.length == 0;
        for (Type expectedType : expectedTypes) {
            if (expectedType.equals(expressionType)) {
                ok = true;
                break;
            }
        }

        if(!ok) {
            throw new PMLCompilationRuntimeException(expressionCtx, "expected expression type(s) " +
                    Arrays.toString(expectedTypes) + ", got " + expressionType);
        }

        return expression;
    }

    public abstract Type getType(Scope<Variable, PMLFunctionSignature> scope) throws PMLScopeException;

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);
}

