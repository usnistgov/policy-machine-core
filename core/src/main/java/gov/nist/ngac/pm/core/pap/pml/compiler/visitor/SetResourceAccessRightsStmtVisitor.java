package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.SetResourceAccessRightsStatement;
import java.util.List;


/**
 * Compiles a PML "set resource access rights ..." statement into a
 * {@link SetResourceAccessRightsStatement}.
 */
public class SetResourceAccessRightsStmtVisitor extends PMLBaseVisitor<SetResourceAccessRightsStatement> {

    public SetResourceAccessRightsStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public SetResourceAccessRightsStatement visitSetResourceAccessRightsStatement(PMLParser.SetResourceAccessRightsStatementContext ctx) {
        Expression<List<String>> expression = ExpressionVisitor.compile(visitorCtx, ctx.accessRightsArr, ListType.of(STRING_TYPE));

        return new SetResourceAccessRightsStatement(expression);
    }
}
