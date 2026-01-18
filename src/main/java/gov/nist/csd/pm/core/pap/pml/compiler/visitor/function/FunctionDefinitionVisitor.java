package gov.nist.csd.pm.core.pap.pml.compiler.visitor.function;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.AdminOpDefinitionStatementContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ResourceOpDefinitionStatementContext;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.function.PMLFunctionSignature;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLStmtsBasicFunction;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLStmtsAdminOperation;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLStmtsResourceOperation;
import gov.nist.csd.pm.core.pap.pml.function.routine.PMLRoutineSignature;
import gov.nist.csd.pm.core.pap.pml.function.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.core.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.basic.BasicFunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.AdminOpDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.ResourceOpDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.RoutineDefinitionStatement;

public class FunctionDefinitionVisitor extends PMLBaseVisitor<FunctionDefinitionStatement> {

    private final FunctionSignatureVisitor functionSignatureVisitor;

    public FunctionDefinitionVisitor(VisitorContext visitorCtx, FunctionSignatureVisitor functionSignatureVisitor) {
        super(visitorCtx);
        this.functionSignatureVisitor = functionSignatureVisitor;
    }

    @Override
    public AdminOpDefinitionStatement visitAdminOpDefinitionStatement(AdminOpDefinitionStatementContext ctx) {
        PMLOperationSignature signature = functionSignatureVisitor.visitAdminOpSignature(ctx.adminOpSignature());

        PMLStatementBlock body = StatementBlockParser.parseAdminOpStatementBlock(
            visitorCtx,
            ctx.adminOpStatementBlock(),
            signature.getReturnType(),
            signature.getFormalParameters()
        );

        return new AdminOpDefinitionStatement(new PMLStmtsAdminOperation<>(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalParameters(),
            body
        ));
    }

    @Override
    public ResourceOpDefinitionStatement visitResourceOpDefinitionStatement(ResourceOpDefinitionStatementContext ctx) {
        PMLOperationSignature resourceOpSignature =
            functionSignatureVisitor.visitResourceOpSignature(ctx.resourceOpSignature());

        PMLStatementBlock pmlStatementBlock = StatementBlockParser.parseResourceOpStatements(
            visitorCtx.copyBasicAndQueryOnly(),
            ctx.resourceOpStatementBlock(),
            resourceOpSignature.getReturnType(),
            resourceOpSignature.getFormalParameters()
        );

        return new ResourceOpDefinitionStatement(new PMLStmtsResourceOperation<>(
            resourceOpSignature.getName(),
            resourceOpSignature.getReturnType(),
            resourceOpSignature.getFormalParameters(),
            pmlStatementBlock
        ));
    }

    @Override
    public RoutineDefinitionStatement visitRoutineDefinitionStatement(PMLParser.RoutineDefinitionStatementContext ctx) {
        PMLRoutineSignature signature = functionSignatureVisitor.visitRoutineSignature(ctx.routineSignature());

        PMLStatementBlock body = StatementBlockParser.parseStatementBlock(
            visitorCtx,
            ctx.statementBlock(),
            signature.getReturnType(),
            signature.getFormalParameters()
        );

        return new RoutineDefinitionStatement(new PMLStmtsRoutine<>(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalParameters(),
            body
        ));
    }

    @Override
    public BasicFunctionDefinitionStatement visitBasicFunctionDefinitionStatement(PMLParser.BasicFunctionDefinitionStatementContext ctx) {
        PMLFunctionSignature signature = functionSignatureVisitor.visitBasicFunctionSignature(ctx.basicFunctionSignature());

        PMLStatementBlock body = StatementBlockParser.parseBasicStatementBlock(
            visitorCtx,
            ctx.basicStatementBlock(),
            signature.getReturnType(),
            signature.getFormalParameters()
        );

        return new BasicFunctionDefinitionStatement(new PMLStmtsBasicFunction<>(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalParameters(),
            body
        ));
    }




}
