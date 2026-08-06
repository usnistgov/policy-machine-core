package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.SetNodePropertiesStatement;
import java.util.Map;


/**
 * Compiles a PML set properties of ... to ... statement into a {@link SetNodePropertiesStatement}.
 */
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
