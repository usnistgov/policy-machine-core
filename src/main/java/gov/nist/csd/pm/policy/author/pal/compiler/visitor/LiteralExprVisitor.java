package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Literal;
import gov.nist.csd.pm.policy.author.pal.model.expression.MapLiteral;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.ArrayLiteral;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiteralExprVisitor extends PALBaseVisitor<Literal> {

    private final VisitorContext visitorCtx;

    public LiteralExprVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public Literal visitLiteralExpr(PALParser.LiteralExprContext ctx) {
        return visitLiteral(ctx.literal());
    }

    private Literal visitLiteral(PALParser.LiteralContext literalCtx) {
        if (literalCtx instanceof PALParser.StringLiteralContext stringLiteralCtx) {
            return parseStringLiteral(stringLiteralCtx);
        } else if (literalCtx instanceof PALParser.BooleanLiteralContext booleanLiteralCtx) {
            return parseBooleanLiteral(booleanLiteralCtx);
        } else if (literalCtx instanceof PALParser.ArrayLiteralContext arrayLiteralCtx) {
            return parseArrayLiteral(arrayLiteralCtx);
        } else if (literalCtx instanceof PALParser.MapLiteralContext mapLiteralCtx) {
            return parseMapLiteral(mapLiteralCtx);
        }

        return null;
    }

    private Literal parseStringLiteral(PALParser.StringLiteralContext ctx) {
        return new Literal(removeQuotes(ctx.STRING().getText()));
    }

    private Literal parseBooleanLiteral(PALParser.BooleanLiteralContext ctx) {
        return new Literal(ctx.BOOLEAN().getText().equalsIgnoreCase("true"));
    }

    private Literal parseArrayLiteral(PALParser.ArrayLiteralContext ctx) {
        PALParser.ArrayContext arrayCtx = ctx.array();
        Type elementType = null;

        // determine the type of the array literal elements
        // if all the elements are of the same type then that is the element type
        // if the elements are of different types then the type is ANY
        List<Expression> exprs = new ArrayList<>();
        for (PALParser.ExpressionContext expressionCtx : arrayCtx.expression()) {
            Expression expr = Expression.compile(visitorCtx, expressionCtx);
            Type type = expr.getType(visitorCtx.scope());

            if (elementType == null) {
                elementType = type;
            } else if (!type.equals(elementType)) {
                elementType = Type.any();
            }

            exprs.add(expr);
        }

        return new Literal(new ArrayLiteral(exprs.toArray(new Expression[]{}), elementType));
    }

    private Literal parseMapLiteral(PALParser.MapLiteralContext ctx) {
        Map<Expression, Expression> map = new HashMap<>();

        Type keyType = null;
        Type valueType = null;

        for(PALParser.MapEntryContext mapEntryCtx : ctx.map().mapEntry()) {
            Expression keyExpr = Expression.compile(visitorCtx, mapEntryCtx.key);
            Expression valueExpr = Expression.compile(visitorCtx, mapEntryCtx.value);


            Type keyExprType = keyExpr.getType(visitorCtx.scope());
            Type valueExprType = valueExpr.getType(visitorCtx.scope());

            // check that all map keys are the same type
            if (keyType == null) {
                keyType = keyExprType;
            } else if (!keyExprType.equals(keyType)) {
                visitorCtx.errorLog().addError(
                        ctx,
                        "expected map keys to be of the same type but found: "
                                + keyExprType + " and " + keyType
                );
            }

            // if map values are different types then the value type for this map is ANY
            if (valueType == null) {
                valueType = valueExprType;
            } else if (!valueExprType.equals(valueType)) {
                valueType = Type.any();
            }

            map.put(keyExpr, valueExpr);
        }

        return new Literal(new MapLiteral(map, keyType, valueType));
    }

    private String removeQuotes(String s) {
        if (s.startsWith("'") && s.endsWith("'")) {
            return s.substring(1, s.length() - 1);
        } else {
            return s;
        }
    }
}
