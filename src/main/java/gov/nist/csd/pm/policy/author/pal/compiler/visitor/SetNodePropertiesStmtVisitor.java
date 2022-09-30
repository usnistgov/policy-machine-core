package gov.nist.csd.pm.policy.author.pal.compiler.visitor;

import gov.nist.csd.pm.policy.author.pal.antlr.PALBaseVisitor;
import gov.nist.csd.pm.policy.author.pal.antlr.PALParser;
import gov.nist.csd.pm.policy.author.pal.model.context.VisitorContext;
import gov.nist.csd.pm.policy.author.pal.statement.NameExpression;
import gov.nist.csd.pm.policy.author.pal.model.expression.Type;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.SetNodePropertiesStatement;

public class SetNodePropertiesStmtVisitor extends PALBaseVisitor<SetNodePropertiesStatement> {

    private final VisitorContext visitorCtx;

    public SetNodePropertiesStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public SetNodePropertiesStatement visitSetNodePropsStmt(PALParser.SetNodePropsStmtContext ctx) {
        NameExpression name = NameExpression.compile(visitorCtx, ctx.nameExpression());
        Expression props = Expression.compile(visitorCtx, ctx.properties, Type.map(Type.string(), Type.string()));

        return new SetNodePropertiesStatement(name, props);
    }
}
