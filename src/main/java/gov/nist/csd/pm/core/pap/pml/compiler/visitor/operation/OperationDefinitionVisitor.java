package gov.nist.csd.pm.core.pap.pml.compiler.visitor.operation;

import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.AdminOpDefinitionStatementContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.QueryOpDefinitionStatementContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ResourceOpDefinitionStatementContext;
import gov.nist.csd.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.csd.pm.core.pap.pml.operation.admin.PMLStmtsAdminOperation;
import gov.nist.csd.pm.core.pap.pml.operation.basic.PMLStmtsFunctionOperation;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLStmtsQueryOperation;
import gov.nist.csd.pm.core.pap.pml.operation.resource.PMLStmtsResourceOperation;
import gov.nist.csd.pm.core.pap.pml.operation.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.core.pap.pml.statement.OperationDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.basic.FunctionDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.AdminOpDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.QueryOperationDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.ResourceOpDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.RoutineDefinitionStatement;

public class OperationDefinitionVisitor extends PMLBaseVisitor<OperationDefinitionStatement> {

    private final OperationSignatureVisitor operationSignatureVisitor;

    public OperationDefinitionVisitor(VisitorContext visitorCtx, OperationSignatureVisitor operationSignatureVisitor) {
        super(visitorCtx);
        this.operationSignatureVisitor = operationSignatureVisitor;
    }

    @Override
    public AdminOpDefinitionStatement visitAdminOpDefinitionStatement(AdminOpDefinitionStatementContext ctx) {
        PMLOperationSignature signature = operationSignatureVisitor.visitAdminOpSignature(ctx.adminOpSignature());

        PMLStatementBlock body = StatementBlockParser.parseStatementBlock(
            visitorCtx,
            ctx.statementBlock(),
            signature.getReturnType(),
            signature.getFormalParameters()
        );

        return new AdminOpDefinitionStatement(new PMLStmtsAdminOperation<>(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalParameters(),
            signature.getReqCaps(),
            body
        ));
    }

    @Override
    public ResourceOpDefinitionStatement visitResourceOpDefinitionStatement(ResourceOpDefinitionStatementContext ctx) {
        PMLOperationSignature resourceOpSignature =
            operationSignatureVisitor.visitResourceOpSignature(ctx.resourceOpSignature());

        PMLStatementBlock pmlStatementBlock;
        if (ctx.basicStatementBlock() != null) {
            pmlStatementBlock = StatementBlockParser.parseBasicStatementBlock(
                visitorCtx.copyFunctionsAndQueriesOnly(),
                ctx.basicStatementBlock(),
                resourceOpSignature.getReturnType(),
                resourceOpSignature.getFormalParameters(),
                true
            );
        } else {
            pmlStatementBlock = new PMLStatementBlock();
        }

        return new ResourceOpDefinitionStatement(new PMLStmtsResourceOperation<>(
            resourceOpSignature.getName(),
            resourceOpSignature.getReturnType(),
            resourceOpSignature.getFormalParameters(),
            resourceOpSignature.getReqCaps(),
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
    public FunctionDefinitionStatement visitFunctionDefinitionStatement(PMLParser.FunctionDefinitionStatementContext ctx) {
        PMLOperationSignature signature = operationSignatureVisitor.visitFunctionSignature(ctx.functionSignature());

        PMLStatementBlock body = StatementBlockParser.parseBasicStatementBlock(
            visitorCtx,
            ctx.basicStatementBlock(),
            signature.getReturnType(),
            signature.getFormalParameters(),
            false
        );

        return new FunctionDefinitionStatement(new PMLStmtsFunctionOperation<>(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalParameters(),
            body
        ));
    }

    @Override
    public QueryOperationDefinitionStatement visitQueryOpDefinitionStatement(QueryOpDefinitionStatementContext ctx) {
        PMLOperationSignature signature = operationSignatureVisitor.visitQueryOpSignature(ctx.queryOpSignature());

        PMLStatementBlock body = StatementBlockParser.parseBasicStatementBlock(
            visitorCtx.copyFunctionsAndQueriesOnly(),
            ctx.basicStatementBlock(),
            signature.getReturnType(),
            signature.getFormalParameters(),
            true
        );

        return new QueryOperationDefinitionStatement(new PMLStmtsQueryOperation<>(
            signature.getName(),
            signature.getReturnType(),
            signature.getFormalParameters(),
            signature.getReqCaps(),
            body
        ));
    }
}
