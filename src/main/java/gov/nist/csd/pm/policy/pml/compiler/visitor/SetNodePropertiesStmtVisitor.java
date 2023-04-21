package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.SetNodePropertiesStatement;

public class SetNodePropertiesStmtVisitor extends PMLBaseVisitor<SetNodePropertiesStatement> {

    private final VisitorContext visitorCtx;

    public SetNodePropertiesStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public SetNodePropertiesStatement visitSetNodePropsStmt(PMLParser.SetNodePropsStmtContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.name, Type.string());
        Expression props = Expression.compile(visitorCtx, ctx.properties, Type.map(Type.string(), Type.string()));

        return new SetNodePropertiesStatement(name, props);
    }
}
