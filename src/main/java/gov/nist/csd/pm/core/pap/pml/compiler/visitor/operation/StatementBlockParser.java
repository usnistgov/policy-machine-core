package gov.nist.csd.pm.core.pap.pml.compiler.visitor.operation;

import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.operation.arg.type.VoidType;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.BasicAndCheckStatementBlockContext;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.StatementBlockVisitor;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.StatementBlockVisitor.Result;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import java.util.List;

public class StatementBlockParser {

    public static PMLStatementBlock parseBasicStatementBlock(VisitorContext visitorCtx,
                                                             PMLParser.BasicStatementBlockContext ctx,
                                                             Type<?> returnType,
                                                             List<FormalParameter<?>> formalArgs) {
        // create a new scope for the function body
        VisitorContext localVisitorCtx = initLocalVisitorCtx(visitorCtx, formalArgs);

        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitBasicStatementBlock(ctx);

        if (!result.allPathsReturned() && !returnType.equals(new VoidType())) {
            throw new PMLCompilationRuntimeException(ctx, "not all conditional paths return");
        }

        return result.stmts();
    }

    public static PMLStatementBlock parseOnStatementBlock(VisitorContext visitorCtx,
                                                             PMLParser.OnPatternBlockContext ctx,
                                                             Type<?> returnType,
                                                             List<FormalParameter<?>> formalArgs) {
        // create a new scope for the function body
        VisitorContext localVisitorCtx = initLocalVisitorCtx(visitorCtx, formalArgs);

        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitOnPatternBlock(ctx);

        if (!result.allPathsReturned() && !returnType.equals(new VoidType())) {
            throw new PMLCompilationRuntimeException(ctx, "not all conditional paths return");
        }

        return result.stmts();
    }

    public static PMLStatementBlock parseAdminOpStatementBlock(VisitorContext visitorCtx,
                                                               PMLParser.AdminOpStatementBlockContext ctx,
                                                               Type<?> returnType,
                                                               List<FormalParameter<?>> formalArgs) {
        // create a new scope for the function body
        VisitorContext localVisitorCtx = initLocalVisitorCtx(visitorCtx, formalArgs);
        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitAdminOpStatementBlock(ctx);

        if (!result.allPathsReturned() && !returnType.equals(new VoidType())) {
            throw new PMLCompilationRuntimeException(ctx, "not all conditional paths return");
        }

        return result.stmts();
    }

    public static PMLStatementBlock parseStatementBlock(VisitorContext visitorCtx,
                                                        PMLParser.StatementBlockContext statementBlockCtx,
                                                        Type<?> returnType,
                                                        List<FormalParameter<?>> formalArgs) {
        VisitorContext localVisitorCtx = initLocalVisitorCtx(visitorCtx, formalArgs);
        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitStatementBlock(statementBlockCtx);

        if (!result.allPathsReturned() && !returnType.equals(new VoidType())) {
            throw new PMLCompilationRuntimeException(statementBlockCtx, "not all conditional paths return");
        }

        return result.stmts();
    }

    public static PMLStatementBlock parseBasicOrCheckStatements(VisitorContext visitorCtx,
                                                                BasicAndCheckStatementBlockContext ctx,
                                                                Type<?> returnType,
                                                                List<FormalParameter<?>> formalArgs) {
        VisitorContext localVisitorCtx = initLocalVisitorCtx(visitorCtx, formalArgs);
        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        Result result = statementBlockVisitor.visitBasicAndCheckStatementBlock(ctx);

        if (!result.allPathsReturned() && !returnType.equals(new VoidType())) {
            throw new PMLCompilationRuntimeException(ctx, "not all conditional paths return");
        }

        return result.stmts();
    }

    private static VisitorContext initLocalVisitorCtx(VisitorContext visitorCtx, List<FormalParameter<?>> formalArgs) {
        // create a new scope for the function body
        VisitorContext localVisitorCtx = visitorCtx.copy();

        // add the args to the local scope, overwriting any variables with the same ID as the formal args
        for (FormalParameter<?> formalArg : formalArgs) {
            localVisitorCtx.scope().updateVariable(
                formalArg.getName(),
                new Variable(formalArg.getName(), formalArg.getType(), false)
            );
        }

        return localVisitorCtx;
    }

}
