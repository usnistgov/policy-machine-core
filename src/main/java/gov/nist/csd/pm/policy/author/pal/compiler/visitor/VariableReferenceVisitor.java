package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.compiler.Variable;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.EntryReference;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.VariableReference;
import gov.nist.csd.pm.policy.author.pal.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;

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
            return visitEntryReference((PALParser.EntryReferenceContext) ctx);
        }
    }

    @Override
    public VariableReference visitReferenceByID(PALParser.ReferenceByIDContext ctx) {
        String id = ctx.VARIABLE_OR_FUNCTION_NAME().getText();

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
    public VariableReference visitEntryReference(PALParser.EntryReferenceContext ctx) {
        String name = ctx.entryRef().VARIABLE_OR_FUNCTION_NAME().getText();
        Type type = Type.any();
        try {
            Variable mapVar = visitorCtx.scope().getVariable(name);
            type = mapVar.type();
        } catch (UnknownVariableInScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());
        }

        VariableReference mapVarRef = new VariableReference(name, type);
        List<PALParser.ExpressionContext> exprCtxs = ctx.entryRef().expression();
        boolean first = true;
        for (PALParser.ExpressionContext exprCtx : exprCtxs) {
            // if the variable reference is not a map or array and this is not the first accessor processed, there is an error
            // if it is the first accessor than check if the variable id is a map
            if (first) {
                Type t = Type.any();

                try {
                    t = visitorCtx.scope().getVariable(name).type();
                } catch (UnknownVariableInScopeException e) {
                    visitorCtx.errorLog().addError(ctx, e.getMessage());
                }
                if (!t.isMap() && !t.isArray()) {
                    visitorCtx.errorLog().addError(
                            exprCtx,
                            "expected map or array type"
                    );
                }
            } else {
                if (!mapVarRef.getType().isMap() && !mapVarRef.getType().isArray()) {
                    visitorCtx.errorLog().addError(
                            exprCtx,
                            "expected map or array type"
                    );
                }
            }

            Type allowed;
            Type valueType = null;
            if (mapVarRef.getType().isMap()) {
                allowed = Type.string();
                valueType = mapVarRef.getType().getMapValueType();
                if (valueType == null) {
                    valueType = Type.any();
                }
            } else if (mapVarRef.getType().isArray()) {
                allowed = Type.number();
                valueType = mapVarRef.getType().getArrayType();
            } else {
                allowed = Type.any();
                valueType = Type.any();
            }

            Expression expr = Expression.compile(visitorCtx, exprCtx, allowed);
            mapVarRef = new VariableReference(new EntryReference(mapVarRef, expr), valueType);
            first = false;
        }

        return mapVarRef;
    }
}
