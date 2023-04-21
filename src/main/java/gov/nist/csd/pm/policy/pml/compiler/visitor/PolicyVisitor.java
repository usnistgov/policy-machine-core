package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.policy.pml.antlr.PMLBaseVisitor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.model.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.statement.PALStatement;

import java.util.ArrayList;
import java.util.List;

public class PolicyVisitor extends PMLBaseVisitor<List<PALStatement>> {

    private final VisitorContext visitorCtx;

    public PolicyVisitor(VisitorContext visitorCtx) {
        this.visitorCtx = visitorCtx;
    }

    @Override
    public List<PALStatement> visitPml(PMLParser.PmlContext ctx) {
        List<PALStatement> statements = new ArrayList<>();
        for (PMLParser.StmtContext stmtCtx : ctx.stmt()) {
            StatementVisitor statementVisitor = new StatementVisitor(visitorCtx);
            PALStatement statement = statementVisitor.visitStmt(stmtCtx);
            statements.add(statement);
        }
        return statements;
    }

}
