package gov.nist.csd.pm.policy.pml.expression.reference;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.ErrorExpression;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.pml.type.Type;

public abstract class VariableReference extends Expression {

    public static Expression compileVariableReference(VisitorContext visitorCtx, PMLParser.VariableReferenceContext ctx) {
        Expression e = null;
        if (ctx instanceof PMLParser.ReferenceByIDContext referenceByIDContext) {
            e = visitReferenceByID(visitorCtx, referenceByIDContext);
        } else if (ctx instanceof PMLParser.ReferenceByIndexContext referenceByIndexContext) {
            e = visitReferenceByIndex(visitorCtx, referenceByIndexContext);
        } else {
            visitorCtx.errorLog().addError(ctx, "invalid variable reference");
        }

        if (e == null) {
            return new ErrorExpression(ctx);
        }

        return e;
    }

    private static ReferenceByID visitReferenceByID(VisitorContext visitorCtx, PMLParser.ReferenceByIDContext ctx) {
        String name = ctx.ID().getText();

        // check variable name is in scope
        try {
            visitorCtx.scope().getVariable(name);
        } catch (UnknownVariableInScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());

            return null;
        }

        return new ReferenceByID(name);
    }

    private static ReferenceByIndex visitReferenceByIndex(VisitorContext visitorCtx, PMLParser.ReferenceByIndexContext ctx) {
        Expression e = compileVariableReference(visitorCtx, ctx.variableReference());
        if (e instanceof ErrorExpression) {
            return null;
        }

        VariableReference mapVarRef = (VariableReference) e;

        // check that the varref is a map
        Type t;
        try {
            t = mapVarRef.getType(visitorCtx.scope());
        } catch (PMLScopeException ex) {
            visitorCtx.errorLog().addError(ctx, ex.getMessage());

            return null;
        }

        if (!t.isMap()) {
            visitorCtx.errorLog().addError(ctx, "expected type map but got " + t);

            return null;
        }

        Type allowedKeyType = t.getMapKeyType();

        return visitIndex(visitorCtx, ctx.index(), mapVarRef, allowedKeyType);
    }

    private static ReferenceByIndex visitIndex(VisitorContext visitorCtx, PMLParser.IndexContext indexCtx,
                                               VariableReference varRef, Type allowedKeyType) {
        if (indexCtx instanceof PMLParser.BracketIndexContext bracketIndexContext) {
            return new ReferenceByBracketIndex(
                    varRef,
                    Expression.compile(visitorCtx, bracketIndexContext.expression(), allowedKeyType)
            );

        } else if (indexCtx instanceof PMLParser.DotIndexContext dotIndexContext) {
            String id = dotIndexContext.key.ID().getText();
            return new ReferenceByDotIndex(varRef, id);

        }

        visitorCtx.errorLog().addError(indexCtx, "invalid index");

        return null;
    }
}
