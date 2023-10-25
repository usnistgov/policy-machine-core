package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.policy.pml.PMLErrorHandler;
import gov.nist.csd.pm.policy.pml.antlr.PMLLexer;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.Literal;
import gov.nist.csd.pm.policy.pml.expression.reference.VariableReference;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.Scope;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

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

        visitorCtx.errorLog().addErrors(pmlErrorHandler.getErrors());
        if (visitorCtx.errorLog().getErrors().size() > 0) {
            return new ErrorExpression(exprCtx);
        }

        return expression;
    }

    public static Expression compile(VisitorContext visitorCtx,
                                     PMLParser.ExpressionContext expressionCtx,
                                     Type expectedType) {
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
            visitorCtx.errorLog().addError(expressionCtx, "unrecognized expression context");

            return new ErrorExpression(expressionCtx);
        }

        Type expressionType;
        try {
            expressionType = expression.getType(visitorCtx.scope());
        } catch (PMLScopeException e) {
            visitorCtx.errorLog().addError(expressionCtx, e.getMessage());
            return new ErrorExpression(expressionCtx);
        }

        // check the expression type is part of the given allowed types
        // if no types are given then any type is allowed
        if (!expectedType.equals(expressionType)) {
            visitorCtx.errorLog().addError(
                    expressionCtx,
                    "expected expression type " + expectedType + ", got " + expressionType
            );

            return new ErrorExpression(expressionCtx);
        }

        return expression;
    }

    public abstract Type getType(Scope scope) throws PMLScopeException;
}

