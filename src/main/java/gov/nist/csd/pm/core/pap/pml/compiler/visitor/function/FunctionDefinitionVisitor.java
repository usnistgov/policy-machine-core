package gov.nist.csd.pm.core.pap.pml.compiler.visitor.function;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.function.arg.type.VoidType;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.AdminOpDefinitionStatementContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.CheckStatementContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ResourceOpDefinitionStatementContext;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.StatementBlockVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLStmtsBasicFunction;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLResourceOperation;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLStmtsAdminOperation;
import gov.nist.csd.pm.core.pap.pml.function.routine.PMLRoutineSignature;
import gov.nist.csd.pm.core.pap.pml.function.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.core.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.basic.BasicFunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.AdminOpDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CheckStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.ResourceOpDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.RoutineDefinitionStatement;


import java.util.*;

public class FunctionDefinitionVisitor extends PMLBaseVisitor<FunctionDefinitionStatement> {

    private FunctionSignatureVisitor functionSignatureVisitor;

    public FunctionDefinitionVisitor(VisitorContext visitorCtx, FunctionSignatureVisitor functionSignatureVisitor) {
        super(visitorCtx);
        this.functionSignatureVisitor = functionSignatureVisitor;
    }

    @Override
    public AdminOpDefinitionStatement visitAdminOpDefinitionStatement(AdminOpDefinitionStatementContext ctx) {
        PMLOperationSignature signature = functionSignatureVisitor.visitAdminOpSignature(ctx.adminOpSignature());

        PMLStatementBlock body = parseBody(
            ctx.adminOpStatementBlock(),
            signature.getReturnType(),
            signature.getFormalArgs()
        );

        return new AdminOpDefinitionStatement(new PMLStmtsAdminOperation(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalArgs(),
            body
        ));
    }

    @Override
    public ResourceOpDefinitionStatement visitResourceOpDefinitionStatement(ResourceOpDefinitionStatementContext ctx) {
        PMLOperationSignature resourceOpSignature =
            functionSignatureVisitor.visitResourceOpSignature(ctx.resourceOpSignature());

        PMLStatementBlock pmlStatementBlock = parseResourceOpCheckStatements(
            ctx.resourceOpStatementBlock().checkStatement(),
            resourceOpSignature.getFormalArgs()
        );

        return new ResourceOpDefinitionStatement(new PMLResourceOperation(
            resourceOpSignature.getName(),
            resourceOpSignature.getFormalArgs(),
            pmlStatementBlock
        ));
    }

    @Override
    public RoutineDefinitionStatement visitRoutineDefinitionStatement(PMLParser.RoutineDefinitionStatementContext ctx) {
        PMLRoutineSignature signature = functionSignatureVisitor.visitRoutineSignature(ctx.routineSignature());

        PMLStatementBlock body = parseBody(
            ctx.statementBlock(),
            signature.getReturnType(),
            signature.getFormalArgs()
        );

        return new RoutineDefinitionStatement(new PMLStmtsRoutine(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalArgs(),
            body
        ));
    }

    @Override
    public BasicFunctionDefinitionStatement visitBasicFunctionDefinitionStatement(PMLParser.BasicFunctionDefinitionStatementContext ctx) {
        PMLFunctionSignature signature = functionSignatureVisitor.visitBasicFunctionSignature(ctx.basicFunctionSignature());

        PMLStatementBlock body = parseBody(
            ctx.basicStatementBlock(),
            signature.getReturnType(),
            signature.getFormalArgs()
        );

        return new BasicFunctionDefinitionStatement(new PMLStmtsBasicFunction(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalArgs(),
            body
        ));
    }

    private PMLStatementBlock parseResourceOpCheckStatements(List<CheckStatementContext> checkStatementContexts, List<FormalParameter<?>> formalArgs) {
        VisitorContext localVisitorCtx = initLocalVisitorCtx(formalArgs);
        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, VOID_TYPE);

        List<PMLStatement<?>> checkStatements = new ArrayList<>();
        for (CheckStatementContext checkStatementContext : checkStatementContexts) {
            CheckStatementVisitor checkStatementBlockVisitor = new CheckStatementVisitor(localVisitorCtx);
            CheckStatement checkStatement = checkStatementBlockVisitor.visitCheckStatement(checkStatementContext);
            checkStatements.add(checkStatement);
        }

        return new PMLStatementBlock(checkStatements);
    }

    private PMLStatementBlock parseBody(PMLParser.BasicStatementBlockContext ctx,
                                              Type<?> returnType,
                                              List<FormalParameter<?>> formalArgs) {
        // create a new scope for the function body
        VisitorContext localVisitorCtx = initLocalVisitorCtx(formalArgs);

        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitBasicStatementBlock(ctx);

        if (!result.allPathsReturned() && !returnType.equals(new VoidType())) {
            throw new PMLCompilationRuntimeException(ctx, "not all conditional paths return");
        }

        return result.stmts();
    }

    private PMLStatementBlock parseBody(PMLParser.AdminOpStatementBlockContext ctx,
                                              Type<?> returnType,
                                              List<FormalParameter<?>> formalArgs) {
        // create a new scope for the function body
        VisitorContext localVisitorCtx = initLocalVisitorCtx(formalArgs);

        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitAdminOpStatementBlock(ctx);

        if (!result.allPathsReturned() && !returnType.equals(new VoidType())) {
            throw new PMLCompilationRuntimeException(ctx, "not all conditional paths return");
        }

        return result.stmts();
    }

    private PMLStatementBlock parseBody(PMLParser.StatementBlockContext statementBlockCtx,
                                              Type<?> returnType,
                                              List<FormalParameter<?>> formalArgs) {
        VisitorContext localVisitorCtx = initLocalVisitorCtx(formalArgs);
        StatementBlockVisitor statementBlockVisitor = new StatementBlockVisitor(localVisitorCtx, returnType);
        StatementBlockVisitor.Result result = statementBlockVisitor.visitStatementBlock(statementBlockCtx);

        if (!result.allPathsReturned() && !returnType.equals(new VoidType())) {
            throw new PMLCompilationRuntimeException(statementBlockCtx, "not all conditional paths return");
        }

        return result.stmts();
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
