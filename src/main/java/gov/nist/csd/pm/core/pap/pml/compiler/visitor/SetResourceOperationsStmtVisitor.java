package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;


import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import gov.nist.csd.pm.core.pap.pml.statement.operation.SetResourceOperationsStatement;
import java.util.List;


public class SetResourceOperationsStmtVisitor extends PMLBaseVisitor<SetResourceOperationsStatement> {

    public SetResourceOperationsStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public SetResourceOperationsStatement visitSetResourceOperationsStatement(PMLParser.SetResourceOperationsStatementContext ctx) {
        Expression<List<String>> expression = ExpressionVisitor.compile(visitorCtx, ctx.accessRightsArr, ListType.of(STRING_TYPE));

        return new SetResourceOperationsStatement(expression);
    }
}
