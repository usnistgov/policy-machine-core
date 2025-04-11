package gov.nist.csd.pm.pap.pml.compiler.visitor.function;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;

import java.util.ArrayList;
import java.util.List;

public class CheckStatementBlockVisitor extends PMLBaseVisitor<PMLStatementBlock> {

    public CheckStatementBlockVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public PMLStatementBlock visitCheckStatementBlock(PMLParser.CheckStatementBlockContext ctx) {
        if (ctx == null) {
            return new PMLStatementBlock();
        }

        CheckStatementVisitor checkVisitor = new CheckStatementVisitor(visitorCtx);
        List<PMLStatement<?>> statements = new ArrayList<>();

        for (PMLParser.CheckStatementContext checkStatementContext : ctx.checkStatement()) {
            statements.add(checkVisitor.visitCheckStatement(checkStatementContext));
        }

        return new PMLStatementBlock(statements);
    }
}
