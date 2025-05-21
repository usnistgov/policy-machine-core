package gov.nist.csd.pm.pap.pml.compiler.visitor.function;

import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.function.arg.type.VoidType;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.pap.pml.compiler.visitor.StatementBlockVisitor;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.pap.pml.function.basic.PMLStmtsBasicFunction;
import gov.nist.csd.pm.pap.pml.function.operation.PMLOperationSignature;
import gov.nist.csd.pm.pap.pml.function.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.function.operation.CheckAndStatementsBlock;
import gov.nist.csd.pm.pap.pml.function.routine.PMLRoutineSignature;
import gov.nist.csd.pm.pap.pml.function.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.basic.BasicFunctionDefinitionStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.OperationDefinitionStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.RoutineDefinitionStatement;


import java.util.*;

public class FunctionDefinitionVisitor extends PMLBaseVisitor<FunctionDefinitionStatement> {

    private FunctionSignatureVisitor functionSignatureVisitor;

    public FunctionDefinitionVisitor(VisitorContext visitorCtx, FunctionSignatureVisitor functionSignatureVisitor) {
        super(visitorCtx);
        this.functionSignatureVisitor = functionSignatureVisitor;
    }

    @Override
    public OperationDefinitionStatement visitOperationDefinitionStatement(PMLParser.OperationDefinitionStatementContext ctx) {
        PMLOperationSignature signature = functionSignatureVisitor.visitOperationSignature(ctx.operationSignature());

        CheckAndStatementsBlock body = parseBody(
            ctx.checkStatementBlock(),
            ctx.statementBlock(),
            signature.getReturnType(),
            signature.getFormalArgs()
        );

        return new OperationDefinitionStatement(new PMLStmtsOperation(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalArgs(),
            body
        ));
    }

    @Override
    public RoutineDefinitionStatement visitRoutineDefinitionStatement(PMLParser.RoutineDefinitionStatementContext ctx) {
        PMLRoutineSignature signature = functionSignatureVisitor.visitRoutineSignature(ctx.routineSignature());

        CheckAndStatementsBlock body = parseBody(
            null,
            ctx.statementBlock(),
            signature.getReturnType(),
            signature.getFormalArgs()
        );

        return new RoutineDefinitionStatement(new PMLStmtsRoutine(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalArgs(),
            body.getStatements()
        ));
    }

    @Override
    public BasicFunctionDefinitionStatement visitBasicFunctionDefinitionStatement(PMLParser.BasicFunctionDefinitionStatementContext ctx) {
        PMLFunctionSignature signature = functionSignatureVisitor.visitBasicFunctionSignature(ctx.basicFunctionSignature());

        CheckAndStatementsBlock body = parseBody(
            ctx.basicStatementBlock(),
            signature.getReturnType(),
            signature.getFormalArgs()
        );

        return new BasicFunctionDefinitionStatement(new PMLStmtsBasicFunction(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalArgs(),
            body.getStatements()
        ));
    }

    private CheckAndStatementsBlock parseBody(PMLParser.BasicStatementBlockContext ctx,
                                              Type<?> returnType,
                                              List<FormalParameter<?>> formalArgs) {
        // create a new scope for the function body
        VisitorContext localVisitorCtx = initLocalVisitorCtx(formalArgs);

        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitBasicStatementBlock(ctx);

        if (!result.allPathsReturned() && !returnType.equals(new VoidType())) {
            throw new PMLCompilationRuntimeException(ctx, "not all conditional paths return");
        }

        return new CheckAndStatementsBlock(new PMLStatementBlock(), result.stmts());
    }

    private CheckAndStatementsBlock parseBody(PMLParser.CheckStatementBlockContext checkStatementBlockCtx,
                                              PMLParser.StatementBlockContext statementBlockCtx,
                                              Type<?> returnType,
                                              List<FormalParameter<?>> formalArgs) {
        VisitorContext localVisitorCtx = initLocalVisitorCtx(formalArgs);
        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitStatementBlock(statementBlockCtx);

        if (!result.allPathsReturned() && !returnType.equals(new VoidType())) {
            throw new PMLCompilationRuntimeException(statementBlockCtx, "not all conditional paths return");
        }

        // get checks
        PMLStatementBlock checks = new PMLStatementBlock();
        if (checkStatementBlockCtx != null) {
            CheckStatementBlockVisitor checkStatementBlockVisitor = new CheckStatementBlockVisitor(localVisitorCtx);
            checks = checkStatementBlockVisitor.visitCheckStatementBlock(checkStatementBlockCtx);
        }

        return new CheckAndStatementsBlock(checks, result.stmts());
    }

    private VisitorContext initLocalVisitorCtx(List<FormalParameter<?>> formalArgs) {
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
