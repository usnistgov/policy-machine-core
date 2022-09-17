package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.compiler.Variable;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Literal;
import gov.nist.csd.pm.policy.author.pal.model.expression.MapEntryReference;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.VariableReference;
import gov.nist.csd.pm.policy.author.pal.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;

public class VariableReferenceVisitor extends PALBaseVisitor<VariableReference> {

    private final VisitorContext visitorCtx;

    public VariableReferenceVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    public VariableReference visitVarRef(PALParser.VarRefContext ctx) {
        if (ctx instanceof PALParser.ReferenceByIDContext referenceByIDContext) {
            return visitReferenceByID(referenceByIDContext);
        } else {
            return visitMapEntryReference((PALParser.MapEntryReferenceContext) ctx);
        }
    }

    @Override
    public VariableReference visitReferenceByID(PALParser.ReferenceByIDContext ctx) {
        String id = ctx.IDENTIFIER().getText();

        // check variable id is in scope
        Type type = Type.any();
        try {
            Variable variable = visitorCtx.scope().getVariable(id);
            type = variable.type();
        } catch (UnknownVariableInScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());
        }

        return new VariableReference(id, type);
    }

    @Override
    public VariableReference visitMapEntryReference(PALParser.MapEntryReferenceContext ctx) {
        String mapName = ctx.mapEntryRef().IDENTIFIER().getText();
        Type mapType = Type.map(Type.any(), Type.any());
        try {
            Variable mapVar = visitorCtx.scope().getVariable(mapName);
            mapType = mapVar.type();
        } catch (UnknownVariableInScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());
        }

        VariableReference mapVarRef = new VariableReference(mapName, mapType);
        List<PALParser.ExpressionContext> exprCtxs = ctx.mapEntryRef().expression();
        boolean first = true;
        for (PALParser.ExpressionContext exprCtx : exprCtxs) {
            Expression expr = Expression.compile(visitorCtx, exprCtx, Type.string());
            // if the map variable reference is not a map and this is not the first accessor processed, there is an error
            // if it is the first accessor than check if the variable id is a map
            if (first) {
                Type type = Type.any();
                try {
                    type = visitorCtx.scope().getVariable(mapName).type();
                } catch (UnknownVariableInScopeException e) {
                    visitorCtx.errorLog().addError(ctx, e.getMessage());
                }
                if (!type.isMap()) {
                    visitorCtx.errorLog().addError(
                            exprCtx,
                            "expected map type"
                    );
                }
            } else {
                if (!mapVarRef.getType().isMap()) {
                    visitorCtx.errorLog().addError(
                            exprCtx,
                            "expected map type"
                    );
                }
            }
            Type type = mapVarRef.getType().getMapValueType();
            if (type == null) {
                type = Type.any();
            }
            mapVarRef = new VariableReference(new MapEntryReference(mapVarRef, expr), type);
            first = false;
        }

        return mapVarRef;
    }
}
