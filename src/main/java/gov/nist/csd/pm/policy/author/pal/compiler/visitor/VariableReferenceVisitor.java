package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.compiler.Variable;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Literal;
import gov.nist.csd.pm.policy.author.pal.model.expression.MapEntryReference;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.model.expression.VariableReference;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;

import java.util.List;

public class VariableReferenceVisitor extends PALBaseVisitor<VariableReference> {

    private final VisitorContext visitorCtx;

    public VariableReferenceVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public VariableReference visitVariableReference(PALParser.VariableReferenceContext ctx) {
        PALParser.VarRefContext varRefCtx = ctx.varRef();
        if (varRefCtx instanceof PALParser.ReferenceByIDContext referenceByIDCtx) {
            String id = referenceByIDCtx.IDENTIFIER().getText();

            // check variable id is in scope
            Variable variable = visitorCtx.scope().getVariable(id);
            Type type = Type.any();
            if (variable == null) {
                visitorCtx.errorLog().addError(
                        ctx,
                        "unknown variable reference: " + id
                );
            } else {
                type = variable.type();
            }

            return new VariableReference(id, type);
        } else if (varRefCtx instanceof PALParser.MapEntryReferenceContext mapEntryCtx) {
            String mapName = mapEntryCtx.mapEntryRef().IDENTIFIER().getText();
            Variable mapVar = visitorCtx.scope().getVariable(mapName);
            Type mapType = Type.map(Type.any(), Type.any());
            // check that the map exists
            if (mapVar == null) {
                visitorCtx.errorLog().addError(
                        ctx,
                        "unknown map reference: " + mapName
                );
            } else {
                mapType = mapVar.type();
            }

            VariableReference mapVarRef = new VariableReference(mapName, mapType);
            List<PALParser.ExpressionContext> exprCtxs = mapEntryCtx.mapEntryRef().expression();
            boolean first = true;
            for (PALParser.ExpressionContext exprCtx : exprCtxs) {
                Expression expr = Expression.compile(visitorCtx, exprCtx, Type.string());
                // if the map variable reference is not a map and this is not the first accessor processed, there is an error
                // if it is the first accessor than check if the variable id is a map
                if (first) {
                    Type type = visitorCtx.scope().getVariable(mapName).type();
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
        } else {
            visitorCtx.errorLog().addError(
                    ctx,
                    "invalid variable reference"
            );

            return null;
        }
    }
}
