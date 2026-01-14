package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.operation.SetNodePropertiesStatement;
import java.util.Map;


public class SetNodePropertiesStmtVisitor extends PMLBaseVisitor<SetNodePropertiesStatement> {

    public SetNodePropertiesStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public SetNodePropertiesStatement visitSetNodePropertiesStatement(PMLParser.SetNodePropertiesStatementContext ctx) {
        Expression<String> name = ExpressionVisitor.compile(visitorCtx, ctx.name, STRING_TYPE);
        Expression<Map<String, String>> props = ExpressionVisitor.compile(visitorCtx, ctx.properties, MapType.of(STRING_TYPE, STRING_TYPE));

        return new SetNodePropertiesStatement(name, props);
    }
}
