package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.EntryReference;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.VariableReference;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.pml.statement.Expression;

import java.util.List;

public class VariableReferenceVisitor extends PMLBaseVisitor<VariableReference> {

    private final VisitorContext visitorCtx;

    public VariableReferenceVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    public VariableReference visitVariableReference(PMLParser.VariableReferenceContext ctx) {
        if (ctx instanceof PMLParser.ReferenceByIDContext referenceByIDContext) {
            return visitReferenceByID(referenceByIDContext);
        } else {
            return visitReferenceByEntry((PMLParser.ReferenceByEntryContext) ctx);
        }
    }

    @Override
    public VariableReference visitReferenceByID(PMLParser.ReferenceByIDContext ctx) {
        String name = ctx.ID().getText();

        // check variable name is in scope
        Type type = Type.any();
        try {
            Variable variable = visitorCtx.scope().getVariable(name);
            type = variable.type();
        } catch (UnknownVariableInScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());
        }

        return new VariableReference(name, type);
    }

    @Override
    public VariableReference visitReferenceByEntry(PMLParser.ReferenceByEntryContext ctx) {
        String name = ctx.entryReference().ID().getText();
        Type type = Type.any();
        try {
            Variable mapVar = visitorCtx.scope().getVariable(name);
            type = mapVar.type();
        } catch (UnknownVariableInScopeException e) {
            visitorCtx.errorLog().addError(ctx, e.getMessage());
        }

        VariableReference mapVarRef = new VariableReference(name, type);
        List<PMLParser.ExpressionContext> exprCtxs = ctx.entryReference().expression();
        boolean first = true;
        for (PMLParser.ExpressionContext exprCtx : exprCtxs) {
            // if the variable reference is not a map or array and this is not the first accessor processed, there is an error
            // if it is the first accessor than check if the variable name is a map
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
            Type valueType;
            if (mapVarRef.getType().isAny()) {
                allowed = Type.any();
                valueType = Type.any();
            } else if (mapVarRef.getType().isMap()) {
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
