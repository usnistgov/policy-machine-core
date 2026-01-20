package gov.nist.csd.pm.core.pap.pml.compiler.visitor.operation;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.AdminOpDefinitionStatementContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.QueryOpDefinitionStatementContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ResourceOpDefinitionStatementContext;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.admin.PMLStmtsAdminOperation;
import gov.nist.csd.pm.core.pap.pml.operation.basic.PMLStmtsBasicOperation;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLStmtsQueryOperation;
import gov.nist.csd.pm.core.pap.pml.operation.resource.PMLStmtsResourceOperation;
import gov.nist.csd.pm.core.pap.pml.operation.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.core.pap.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.basic.BasicFunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.AdminOpDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.QueryOperationDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.ResourceOpDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.RoutineDefinitionStatement;

public class OperationDefinitionVisitor extends PMLBaseVisitor<FunctionDefinitionStatement> {

    private final OperationSignatureVisitor operationSignatureVisitor;

    public OperationDefinitionVisitor(VisitorContext visitorCtx, OperationSignatureVisitor operationSignatureVisitor) {
        super(visitorCtx);
        this.operationSignatureVisitor = operationSignatureVisitor;
    }

    @Override
    public AdminOpDefinitionStatement visitAdminOpDefinitionStatement(AdminOpDefinitionStatementContext ctx) {
        PMLOperationSignature signature = operationSignatureVisitor.visitAdminOpSignature(ctx.adminOpSignature());

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
            operationSignatureVisitor.visitResourceOpSignature(ctx.resourceOpSignature());

        PMLStatementBlock pmlStatementBlock = StatementBlockParser.parseBasicOrCheckStatements(
            visitorCtx.copyBasicAndQueryOnly(),
            ctx.basicAndCheckStatementBlock(),
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
        PMLOperationSignature signature = operationSignatureVisitor.visitRoutineSignature(ctx.routineSignature());

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
        PMLOperationSignature signature = operationSignatureVisitor.visitBasicFunctionSignature(ctx.basicFunctionSignature());

        PMLStatementBlock body = StatementBlockParser.parseBasicStatementBlock(
            visitorCtx,
            ctx.basicStatementBlock(),
            signature.getReturnType(),
            signature.getFormalParameters()
        );

        return new BasicFunctionDefinitionStatement(new PMLStmtsBasicOperation<>(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalParameters(),
            body
        ));
    }

    @Override
    public FunctionDefinitionStatement visitQueryOpDefinitionStatement(QueryOpDefinitionStatementContext ctx) {
        PMLOperationSignature signature = operationSignatureVisitor.visitQueryOpSignature(ctx.queryOpSignature());

        PMLStatementBlock body = StatementBlockParser.parseBasicOrCheckStatements(
            visitorCtx,
            ctx.basicAndCheckStatementBlock(),
            signature.getReturnType(),
            signature.getFormalParameters()
        );

        return new QueryOperationDefinitionStatement(new PMLStmtsQueryOperation<>(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalParameters(),
            body
        ));
    }
}
