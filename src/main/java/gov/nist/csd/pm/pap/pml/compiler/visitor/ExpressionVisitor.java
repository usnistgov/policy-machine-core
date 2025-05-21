package gov.nist.csd.pm.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.pap.function.arg.type.Type.BOOLEAN_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.ANY_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.type.AnyType;
import gov.nist.csd.pm.pap.pml.PMLErrorHandler;
import gov.nist.csd.pm.pap.pml.antlr.PMLLexer;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.ExpressionListContext;
import gov.nist.csd.pm.pap.pml.exception.UnexpectedExpressionTypeException;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.function.arg.type.MapType;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.BracketIndexContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.DotIndexContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.EqualsExpressionContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.ExpressionContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.FunctionInvokeContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.IndexContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.LogicalExpressionContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.NegateExpressionContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.ParenExpressionContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.PlusExpressionContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.VariableReferenceContext;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.expression.ParenExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.BoolLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.reference.BracketIndexExpression;
import gov.nist.csd.pm.pap.pml.expression.reference.DotIndexExpression;
import gov.nist.csd.pm.pap.pml.expression.EqualsExpression;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.expression.FunctionInvokeExpression;
import gov.nist.csd.pm.pap.pml.expression.LogicalExpression;
import gov.nist.csd.pm.pap.pml.expression.NegatedExpression;
import gov.nist.csd.pm.pap.pml.expression.PlusExpression;
import gov.nist.csd.pm.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.MapLiteralExpression;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.scope.UnknownFunctionInScopeException;
import gov.nist.csd.pm.pap.pml.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.pap.pml.type.TypeStringer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

public class ExpressionVisitor extends PMLBaseVisitor<Expression<?>> {

    public static <T> Expression<T> compile(VisitorContext visitorCtx,
                                            ExpressionContext ctx,
                                            Type<T> expectedType) {
        ExpressionVisitor visitor = new ExpressionVisitor(visitorCtx);
        Expression<?> compiledExpression = visitor.visit(ctx);
        Type<?> resultType = compiledExpression.getType();

        if (expectedType.equals(ANY_TYPE) || resultType.equals(expectedType)) {
            return (Expression<T>) compiledExpression;
        } else if ((resultType.equals(ANY_TYPE) && !expectedType.equals(ANY_TYPE))
            || resultType.isCastableTo(expectedType)) {
            return new ExpressionWrapper<>(compiledExpression, expectedType);
        } else {
            throw new PMLCompilationRuntimeException(ctx, 
                new UnexpectedExpressionTypeException(resultType, expectedType).getMessage());
        }
    }

    public static Expression<?> compile(VisitorContext visitorCtx,
                                        ExpressionContext ctx) {
        Objects.requireNonNull(visitorCtx);
        Objects.requireNonNull(ctx);

        ExpressionVisitor visitor = new ExpressionVisitor(visitorCtx);
        return visitor.visit(ctx);
    }

    public static <T> Expression<T> fromString(VisitorContext visitorCtx, String input, Type<T> expectedType) {
        PMLErrorHandler pmlErrorHandler = new PMLErrorHandler();

        PMLLexer lexer = new PMLLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(pmlErrorHandler);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(pmlErrorHandler);

        PMLParser.ExpressionContext exprCtx = parser.expression();

        Expression<T> expression = compile(visitorCtx, exprCtx, expectedType);

        if (!visitorCtx.errorLog().getErrors().isEmpty()) {
            throw new PMLCompilationRuntimeException(pmlErrorHandler.getErrors());
        }

        return expression;
    }

    public static <T> Expression<T> compileFunctionInvoke(VisitorContext visitorCtx,
                                                          FunctionInvokeContext ctx,
                                                          Type<T> expectedType) {
        Objects.requireNonNull(visitorCtx);
        Objects.requireNonNull(ctx);

        ExpressionVisitor visitor = new ExpressionVisitor(visitorCtx);
        FunctionInvokeExpression<?> compiled = visitor.visitFunctionInvoke(ctx);

        try {
            return compiled.asType(expectedType);
        } catch (UnexpectedExpressionTypeException e) {
            throw new PMLCompilationRuntimeException(ctx, e.getMessage());
        }
    }

    private ExpressionVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public Expression<?> visitNegateExpression(NegateExpressionContext ctx) {
        Expression<Boolean> expression = ExpressionVisitor.compile(visitorCtx, ctx.expression(), BOOLEAN_TYPE);

        return new NegatedExpression(expression);
    }

    @Override
    public Expression<?> visitLogicalExpression(LogicalExpressionContext ctx) {
        Expression<Boolean> left = ExpressionVisitor.compile(visitorCtx, ctx.left, BOOLEAN_TYPE);
        Expression<Boolean> right = ExpressionVisitor.compile(visitorCtx, ctx.right, BOOLEAN_TYPE);

        return new LogicalExpression(left, right, ctx.LOGICAL_AND() != null);
    }

    @Override
    public Expression<?> visitPlusExpression(PlusExpressionContext ctx) {
        return new PlusExpression(
            ExpressionVisitor.compile(visitorCtx, ctx.left, STRING_TYPE),
            ExpressionVisitor.compile(visitorCtx, ctx.right, STRING_TYPE)
        );
    }

    @Override
    public FunctionInvokeExpression<?> visitFunctionInvoke(FunctionInvokeContext ctx) {
        String funcName = ctx.ID().getText();

        PMLFunctionSignature signature;
        try {
            signature = visitorCtx.scope().getFunction(funcName);
        } catch (UnknownFunctionInScopeException e) {
            throw new PMLCompilationRuntimeException(ctx, e.getMessage());
        }

        PMLParser.FunctionInvokeArgsContext funcCallArgsCtx = ctx.functionInvokeArgs();
        List<ExpressionContext> argExpressions =  new ArrayList<>();
        PMLParser.ExpressionListContext expressionListContext = funcCallArgsCtx.expressionList();
        if (expressionListContext != null) {
            argExpressions = expressionListContext.expression();
        }

        List<FormalParameter<?>> formalArgs = signature.getFormalArgs();
        if (formalArgs.size() != argExpressions.size()) {
            throw new PMLCompilationRuntimeException(
                ctx,
                "wrong number of args for function call " + funcName + ": " +
                    "expected " + formalArgs.size() + ", got " + argExpressions.size()
            );
        }

        List<Expression<?>> args = new ArrayList<>();
        for (int i = 0; i < formalArgs.size(); i++) {
            PMLParser.ExpressionContext exprCtx = argExpressions.get(i);
            FormalParameter<?> formalArg = formalArgs.get(i);

            Expression<?> expr = ExpressionVisitor.compile(visitorCtx, exprCtx, formalArg.getType());
            args.add(expr);
        }

        return new FunctionInvokeExpression<>(signature, args, signature.getReturnType());
    }

    @Override
    public Expression<?> visitVariableReference(VariableReferenceContext ctx) {
        String varName = ctx.ID().getText();
        Variable variable;
        try {
            variable = visitorCtx.scope().getVariable(varName);
        } catch (UnknownVariableInScopeException e) {
            throw new PMLCompilationRuntimeException(ctx, e.getMessage());
        }

        Expression<?> baseExpr = new VariableReferenceExpression<>(varName, variable.type());

        for (IndexContext indexCtx : ctx.index()) {
            try {
                if (indexCtx instanceof BracketIndexContext bracketIndexContext) {
                    baseExpr = createBracketIndexExpression(baseExpr, bracketIndexContext);
                } else if (indexCtx instanceof DotIndexContext dotIndexContext) {
                    baseExpr = createDotIndexExpression(baseExpr, dotIndexContext);
                }
            } catch (UnexpectedExpressionTypeException e) {
                throw new PMLCompilationRuntimeException(ctx, e.getMessage());
            }
        }

        return baseExpr;
    }

    @Override
    public Expression<?> visitParenExpression(ParenExpressionContext ctx) {
        return new ParenExpression<>(ExpressionVisitor.compile(visitorCtx, ctx.expression(), ANY_TYPE));
    }

    @Override
    public Expression<?> visitEqualsExpression(EqualsExpressionContext ctx) {
        Expression<Object> left = ExpressionVisitor.compile(visitorCtx, ctx.left, ANY_TYPE);
        Expression<Object> right = ExpressionVisitor.compile(visitorCtx, ctx.right, ANY_TYPE);
        boolean isEquals = ctx.EQUALS() != null;

        return new EqualsExpression(left, right, isEquals);
    }

    @Override
    public Expression<?> visitStringLiteral(PMLParser.StringLiteralContext ctx) {
        String text = ctx.stringLit().getText();
        return new StringLiteralExpression(removeQuotes(text));
    }

    @Override
    public Expression<?> visitBoolLiteral(PMLParser.BoolLiteralContext ctx) {
        boolean value = Boolean.parseBoolean(ctx.getText());
        return new BoolLiteralExpression(value);
    }

    @Override
    public Expression<?> visitArrayLiteral(PMLParser.ArrayLiteralContext ctx) {
        ExpressionListContext expressionListContext = ctx.arrayLit().expressionList();
        if (expressionListContext == null || expressionListContext.expression().isEmpty()) {
            return new ArrayLiteralExpression<>(new ArrayList<>(), ANY_TYPE);
        }

        Type<?> elementType = null;
        List<Expression<?>> elements = new ArrayList<>();

        for (PMLParser.ExpressionContext elementCtx : ctx.arrayLit().expressionList().expression()) {
            Expression<?> element = ExpressionVisitor.compile(visitorCtx, elementCtx);
            elements.add(element);

            if (elementType == null) {
                elementType = element.getType();
            } else if (!elementType.equals(element.getType())) {
                elementType = ANY_TYPE;
            }
        }

        return new ArrayLiteralExpression<>(elements, elementType);
    }

    @Override
    public Expression<?> visitMapLiteral(PMLParser.MapLiteralContext ctx) {
        Type<?> keyType = null;
        Type<?> valueType = null;

        if (ctx.mapLit().element() == null || ctx.mapLit().element().isEmpty()) {
            return new MapLiteralExpression<>(new HashMap<>(), ANY_TYPE, ANY_TYPE);
        }

        Map<Expression<?>, Expression<?>> entries = new HashMap<>();

        for (PMLParser.ElementContext elementCtx : ctx.mapLit().element()) {
            Expression<?> key = ExpressionVisitor.compile(visitorCtx, elementCtx.key);
            Expression<?> value = ExpressionVisitor.compile(visitorCtx, elementCtx.value);
            entries.put(key, value);

            if (keyType == null) {
                keyType = key.getType();
            } else if (!keyType.equals(key.getType())) {
                keyType = ANY_TYPE;
            }

            if (valueType == null) {
                valueType = value.getType();
            } else if (!valueType.equals(value.getType())) {
                valueType = ANY_TYPE;
            }
        }

        return new MapLiteralExpression<>(entries, keyType, valueType);
    }

    private Expression<?> createDotIndexExpression(Expression<?> baseExpr, PMLParser.DotIndexContext ctx) throws
                                                                                                          UnexpectedExpressionTypeException {
        String key = ctx.key.getText();
        MapType<?, ?> mapType = validateMapType(baseExpr.getType(), ctx);

        // check that the key type of this map is string which is the only type supported for dot indexes
        assertIsCastableTo(mapType.getKeyType(), STRING_TYPE);

        Type<?> valueType = mapType.getValueType();

        return new DotIndexExpression<>(baseExpr, key, valueType);
    }

    private Expression<?> createBracketIndexExpression(Expression<?> baseExpr, PMLParser.BracketIndexContext ctx) throws
                                                                                                                  UnexpectedExpressionTypeException {
        Expression<?> indexExpr = ExpressionVisitor.compile(visitorCtx, ctx.expression(), ANY_TYPE);
        MapType<?, ?> mapType = validateMapType(baseExpr.getType(), ctx);

        // check taht the maps key type is castable to the index expressions type
        assertIsCastableTo(mapType.getKeyType(), indexExpr.getType());

        Type<?> valueType = mapType.getValueType();

        return new BracketIndexExpression<>(baseExpr, indexExpr, valueType);
    }

    private MapType<?, ?> validateMapType(Type<?> type, ParserRuleContext ctx) {
        if (type instanceof AnyType) {
            return MapType.of(ANY_TYPE, ANY_TYPE);
        }

        if (!(type instanceof MapType<?, ?> mapType)) {
            throw new PMLCompilationRuntimeException(ctx,
                String.format("Type mismatch: Cannot apply indexing to type %s. Expected Map.",
                    TypeStringer.toPMLString(type)));
        }

        return mapType;
    }

    public static String removeQuotes(String s) {
        return s.trim().substring(1, s.length() - 1);
    }

    private static void assertIsCastableTo(Type<?> type, Type<?> targetType) throws UnexpectedExpressionTypeException {
        if (type.isCastableTo(targetType)) {
            return;
        }

        throw new UnexpectedExpressionTypeException(type, targetType);
    }


    private static class ExpressionWrapper<T> extends Expression<T> {
        private final Expression<?> wrapped;
        private final Type<T> expectedType;
        
        ExpressionWrapper(Expression<?> wrapped, Type<T> expectedType) {
            this.wrapped = wrapped;
            this.expectedType = expectedType;
        }
        
        @Override
        public Type<T> getType() {
            return expectedType;
        }
        
        @Override
        public T execute(ExecutionContext ctx, PAP pap) throws PMException {
            Object result = wrapped.execute(ctx, pap);
            return expectedType.cast(result);
        }
        
        @Override
        public String toFormattedString(int indentLevel) {
            return wrapped.toFormattedString(indentLevel);
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ExpressionWrapper<?> that)) return false;
            return wrapped.equals(that.wrapped) && expectedType.equals(that.expectedType);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(wrapped, expectedType);
        }
    }
}