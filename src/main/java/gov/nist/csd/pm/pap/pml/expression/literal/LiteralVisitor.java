package gov.nist.csd.pm.pap.pml.expression.literal;

import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.PMLBaseVisitor;

import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.type.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiteralVisitor extends PMLBaseVisitor<Expression> {

    public LiteralVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public StringLiteral visitStringLiteral(PMLParser.StringLiteralContext ctx) {
        return visitStringLit(ctx.stringLit());
    }

    @Override
    public StringLiteral visitStringLit(PMLParser.StringLitContext ctx) {
        return new StringLiteral(removeQuotes(ctx.DOUBLE_QUOTE_STRING().toString()));
    }

    @Override
    public BoolLiteral visitBoolLiteral(PMLParser.BoolLiteralContext ctx) {
        return new BoolLiteral(ctx.boolLit().TRUE() != null);
    }

    @Override
    public Expression visitArrayLiteral(PMLParser.ArrayLiteralContext ctx) {
        PMLParser.ArrayLitContext arrayCtx = ctx.arrayLit();

        PMLParser.ExpressionListContext expressionListContext = arrayCtx.expressionList();
        if (expressionListContext == null) {
            return new ArrayLiteral(new ArrayList<>(), Type.any());
        }

        // set the element type to any if the list is empty
        // element type being null is used in the following for loop to determine type dynamically
        Type elementType = null;
        if (expressionListContext.isEmpty()) {
            elementType = Type.any();
        }

        // determine the type of the array literal elements
        // if all the elements are of the same type then that is the element type
        // if the elements are of different types then the type is ANY
        List<Expression> exprs = new ArrayList<>();
        for (PMLParser.ExpressionContext expressionCtx : expressionListContext.expression()) {
            Expression expr = Expression.compile(visitorCtx, expressionCtx, Type.any());
            Type type;
            try {
                type = expr.getType(visitorCtx.scope());
            } catch (PMLScopeException e) {
                throw new PMLCompilationRuntimeException(expressionCtx, e.getMessage());
            }

            if (elementType == null) {
                elementType = type;
            } else if (!type.equals(elementType)) {
                elementType = Type.any();
            }

            exprs.add(expr);
        }

        return new ArrayLiteral(new ArrayList<>(exprs), elementType);
    }

    @Override
    public Expression visitMapLiteral(PMLParser.MapLiteralContext ctx) {
        Map<Expression, Expression> map = new HashMap<>();

        Type keyType = null;
        Type valueType = null;

        for(PMLParser.ElementContext elementCtx : ctx.mapLit().element()) {
            Expression keyExpr = Expression.compile(visitorCtx, elementCtx.key, Type.any());
            Expression valueExpr = Expression.compile(visitorCtx, elementCtx.value, Type.any());

            Type keyExprType;
            Type valueExprType;
            try {
                keyExprType = keyExpr.getType(visitorCtx.scope());
                valueExprType = valueExpr.getType(visitorCtx.scope());
            } catch (PMLScopeException e) {
                throw new PMLCompilationRuntimeException(elementCtx, e.getMessage());
            }

            // check that all map keys are the same type
            if (keyType == null) {
                keyType = keyExprType;
            } else if (!keyExprType.equals(keyType)) {
                keyType = Type.any();
            }

            // if map values are different types then the value type for this map is ANY
            if (valueType == null) {
                valueType = valueExprType;
            } else if (!valueExprType.equals(valueType)) {
                valueType = Type.any();
            }

            map.put(keyExpr, valueExpr);
        }

        if (keyType == null) {
            keyType = Type.any();
        }

        if (valueType == null) {
            valueType = Type.any();
        }

        return new MapLiteral(map, keyType, valueType);
    }

    public static String removeQuotes(String s) {
        if (s.startsWith("\"") && s.endsWith("\"")) {
            return s.substring(1, s.length() - 1);
        } else {
            return s;
        }
    }
}