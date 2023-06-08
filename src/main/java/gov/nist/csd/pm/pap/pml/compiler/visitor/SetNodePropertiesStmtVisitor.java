package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.operation.SetNodePropertiesStatement;
import gov.nist.csd.pm.pap.pml.type.Type;

public class SetNodePropertiesStmtVisitor extends PMLBaseVisitor<SetNodePropertiesStatement> {

    public SetNodePropertiesStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public SetNodePropertiesStatement visitSetNodePropertiesStatement(PMLParser.SetNodePropertiesStatementContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.name, Type.string());
        Expression props = Expression.compile(visitorCtx, ctx.properties, Type.map(Type.string(), Type.string()));

        return new SetNodePropertiesStatement(name, props);
    }
}
