package gov.nist.csd.pm.pap.pml.expression.reference;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.scope.PMLScopeException;
import gov.nist.csd.pm.pap.pml.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.pap.pml.type.Type;

public abstract class VariableReference extends Expression {

    public static VariableReference compileVariableReference(VisitorContext visitorCtx, PMLParser.VariableReferenceContext ctx) {
        if (ctx instanceof PMLParser.ReferenceByIDContext referenceByIDContext) {
            return visitReferenceByID(visitorCtx, referenceByIDContext);
        } else if (ctx instanceof PMLParser.ReferenceByIndexContext referenceByIndexContext) {
            return visitReferenceByIndex(visitorCtx, referenceByIndexContext);
        } else {
            throw new PMLCompilationRuntimeException(ctx, "invalid variable reference");
        }
    }

    private static ReferenceByID visitReferenceByID(VisitorContext visitorCtx, PMLParser.ReferenceByIDContext ctx) {
        String name = ctx.ID().getText();

        // check variable name is in scope
        try {
            visitorCtx.scope().getVariable(name);
        } catch (UnknownVariableInScopeException e) {
            throw new PMLCompilationRuntimeException(ctx, e.getMessage());
        }

        return new ReferenceByID(name);
    }

    private static ReferenceByIndex visitReferenceByIndex(VisitorContext visitorCtx, PMLParser.ReferenceByIndexContext ctx) {
        VariableReference mapVarRef = compileVariableReference(visitorCtx, ctx.variableReference());

        // check that the varref is a map
        Type t;
        try {
            t = mapVarRef.getType(visitorCtx.scope());
        } catch (PMLScopeException ex) {
            throw new PMLCompilationRuntimeException(ctx, ex.getMessage());
        }

        if (!t.isMap()) {
            throw new PMLCompilationRuntimeException(ctx, "expected type map but got " + t);
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

        throw new PMLCompilationRuntimeException(indexCtx, "invalid index");
    }
}
