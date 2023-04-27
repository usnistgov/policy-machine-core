package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;

import java.util.ArrayList;
import java.util.List;

public class PolicyVisitor extends PMLBaseVisitor<List<PMLStatement>> {

    private final VisitorContext visitorCtx;

    public PolicyVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public List<PMLStatement> visitPml(PMLParser.PmlContext ctx) {
        List<PMLStatement> statements = new ArrayList<>();
        for (PMLParser.StatementContext stmtCtx : ctx.statement()) {
            StatementVisitor statementVisitor = new StatementVisitor(visitorCtx);
            PMLStatement statement = statementVisitor.visitStatement(stmtCtx);
            statements.add(statement);
        }
        return statements;
    }

}
