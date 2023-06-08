package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;

import java.util.ArrayList;
import java.util.List;

public class CheckStatementBlockVisitor extends PMLBaseVisitor<PMLStatementBlock>{

    public CheckStatementBlockVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public PMLStatementBlock visitCheckStatementBlock(PMLParser.CheckStatementBlockContext ctx) {
        List<PMLStatement> statements = new ArrayList<>();

        if (ctx == null) {
            return new PMLStatementBlock(statements);
        }

        CheckStatementVisitor checkVisitor = new CheckStatementVisitor(visitorCtx);

        for (PMLParser.CheckStatementContext checkStatementContext : ctx.checkStatement()) {
            statements.add(checkVisitor.visitCheckStatement(checkStatementContext));
        }

        return new PMLStatementBlock(statements);
    }
}
