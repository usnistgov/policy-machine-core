package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLParserBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.statement.DeassignStatement;


public class DeassignStmtVisitor extends PMLParserBaseVisitor<PMLStatement> {

    private final VisitorContext visitorCtx;

    public DeassignStmtVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public PMLStatement visitDeassignStatement(PMLParser.DeassignStatementContext ctx) {
        Expression name = Expression.compile(visitorCtx, ctx.childNode, Type.string());
        Expression parents = Expression.compile(visitorCtx, ctx.parentNodes, Type.array(Type.string()));

        return new DeassignStatement(name, parents);
    }
}