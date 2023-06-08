package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.expression.literal.ArrayLiteral;
import gov.nist.csd.pm.pap.pml.expression.literal.LiteralVisitor;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.statement.operation.SetResourceOperationsStatement;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class SetResourceOperationsStmtVisitor extends PMLBaseVisitor<SetResourceOperationsStatement> {

    public SetResourceOperationsStmtVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public SetResourceOperationsStatement visitSetResourceOperationsStatement(PMLParser.SetResourceOperationsStatementContext ctx) {
        Expression expression = Expression.compile(visitorCtx, ctx.accessRightsArr, Type.array(Type.string()));

        return new SetResourceOperationsStatement(expression);
    }
}
