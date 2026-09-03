/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.compiler.visitor.operation;

import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.AdminOpDefinitionStatementContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.QueryOpDefinitionStatementContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.ResourceOpDefinitionStatementContext;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.PMLBaseVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.operation.PMLOperationSignature;
import gov.nist.ngac.pm.core.pap.pml.operation.admin.PMLStmtsAdminOperation;
import gov.nist.ngac.pm.core.pap.pml.operation.basic.PMLStmtsFunctionOperation;
import gov.nist.ngac.pm.core.pap.pml.operation.query.PMLStmtsQueryOperation;
import gov.nist.ngac.pm.core.pap.pml.operation.resource.PMLStmtsResourceOperation;
import gov.nist.ngac.pm.core.pap.pml.operation.routine.PMLStmtsRoutine;
import gov.nist.ngac.pm.core.pap.pml.statement.OperationDefinitionStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.FunctionDefinitionStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.AdminOpDefinitionStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.QueryOperationDefinitionStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.ResourceOpDefinitionStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.RoutineDefinitionStatement;

/**
 * Compiles a PML operation definition into the matching {@link OperationDefinitionStatement} subtype.
 */
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
            signature.getEventParameters(),
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
            resourceOpSignature.getEventParameters(),
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
            signature.getEventParameters(),
            signature.getReqCaps(),
            body
        ));
    }
}
