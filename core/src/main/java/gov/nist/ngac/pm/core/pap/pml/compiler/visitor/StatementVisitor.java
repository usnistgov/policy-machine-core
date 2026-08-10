package gov.nist.ngac.pm.core.pap.pml.compiler.visitor;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.pml.PMLErrorHandler;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLLexer;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.FunctionDefinitionStatementContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.QueryOpDefinitionStatementContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.ResourceOpDefinitionStatementContext;
import gov.nist.ngac.pm.core.pap.pml.compiler.error.ErrorLog;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.operation.OperationDefinitionVisitor;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.operation.OperationSignatureVisitor;
import gov.nist.ngac.pm.core.pap.pml.compiler.visitor.operation.RequireStatementVisitor;
import gov.nist.ngac.pm.core.pap.pml.context.VisitorContext;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.ngac.pm.core.pap.pml.scope.NarrowCompileScope;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.query.OperationsQuery;
import java.util.function.Function;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * Compiles a single PML statement into a {@link PMLStatement} by dispatching to the visitor for its kind.
 */
public class StatementVisitor extends PMLBaseVisitor<PMLStatement<?>> {

    public StatementVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    /**
     * Recompiles a single already-persisted statement, using a {@link NarrowCompileScope} to avoid
     * loading the full symbol table.
     * @param operationsQuery the query to resolve cross-references against
     * @param input a single statement's PML text
     * @return the compiled statement
     * @throws PMException if the input fails to compile
     */
    public static PMLStatement<?> fromString(OperationsQuery operationsQuery, String input) throws PMException {
        PMLErrorHandler pmlErrorHandler = new PMLErrorHandler();

        PMLLexer lexer = new PMLLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(pmlErrorHandler);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PMLParser parser = new PMLParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(pmlErrorHandler);

        PMLParser.StatementContext stmtCtx = parser.statement();
        if (!pmlErrorHandler.getErrors().isEmpty()) {
            throw new PMLCompilationException(pmlErrorHandler.getErrors());
        }

        ErrorLog errorLog = new ErrorLog();
        VisitorContext visitorCtx = new VisitorContext(tokens, new NarrowCompileScope(operationsQuery), errorLog, pmlErrorHandler);
        StatementVisitor visitor = new StatementVisitor(visitorCtx);

        PMLStatement<?> statement = null;
        try {
            statement = visitor.visitStatement(stmtCtx);
        } catch (PMLCompilationRuntimeException e) {
            errorLog.addErrors(e.getErrors());
        }

        if (!errorLog.getErrors().isEmpty()) {
            throw new PMLCompilationException(errorLog.getErrors());
        }

        return statement;
    }

    /**
     * Like {@link #fromString(OperationsQuery, String)}, but casts the result to statementType and pulls
     * the domain object back out via extractor.
     * @param operationsQuery the query to resolve cross-references against
     * @param input a single statement's PML text
     * @param statementType the concrete statement type input is expected to compile to
     * @param extractor pulls the domain object out of the compiled statement
     * @return the extracted domain object
     * @throws PMException if the input fails to compile
     * @throws ClassCastException if statementType is wrong
     */
    public static <S, T> T fromString(OperationsQuery operationsQuery, String input, Class<S> statementType,
                                      Function<S, T> extractor) throws PMException {
        PMLStatement<?> statement = fromString(operationsQuery, input);
        return extractor.apply(statementType.cast(statement));
    }

    @Override
    public PMLStatement<?> visitCreatePolicyStatement(PMLParser.CreatePolicyStatementContext ctx) {
        return new CreatePolicyStmtVisitor(visitorCtx).visitCreatePolicyStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitCreateNonPCStatement(PMLParser.CreateNonPCStatementContext ctx) {
        return new CreateNonPCStmtVisitor(visitorCtx).visitCreateNonPCStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitCreateObligationStatement(PMLParser.CreateObligationStatementContext ctx) {
        return new CreateObligationStmtVisitor(visitorCtx).visitCreateObligationStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitCreateProhibitionStatement(PMLParser.CreateProhibitionStatementContext ctx) {
        return new CreateProhibitionStmtVisitor(visitorCtx).visitCreateProhibitionStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitSetNodePropertiesStatement(PMLParser.SetNodePropertiesStatementContext ctx) {
        return new SetNodePropertiesStmtVisitor(visitorCtx).visitSetNodePropertiesStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitAssignStatement(PMLParser.AssignStatementContext ctx) {
        return new AssignStmtVisitor(visitorCtx).visitAssignStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitDeassignStatement(PMLParser.DeassignStatementContext ctx) {
        return new DeassignStmtVisitor(visitorCtx).visitDeassignStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitAssociateStatement(PMLParser.AssociateStatementContext ctx) {
        return new AssociateStmtVisitor(visitorCtx).visitAssociateStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitDissociateStatement(PMLParser.DissociateStatementContext ctx) {
        return new DissociateStmtVisitor(visitorCtx).visitDissociateStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitSetResourceAccessRightsStatement(PMLParser.SetResourceAccessRightsStatementContext ctx) {
        return new SetResourceAccessRightsStmtVisitor(visitorCtx).visitSetResourceAccessRightsStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitDeleteStatement(PMLParser.DeleteStatementContext ctx) {
        return new DeleteStmtVisitor(visitorCtx).visitDeleteStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitVariableAssignmentStatement(PMLParser.VariableAssignmentStatementContext ctx) {
        return new VarStmtVisitor(visitorCtx).visitVariableAssignmentStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitVarDeclaration(PMLParser.VarDeclarationContext ctx) {
        return new VarStmtVisitor(visitorCtx).visitVarDeclaration(ctx);
    }

    @Override
    public PMLStatement<?> visitShortDeclaration(PMLParser.ShortDeclarationContext ctx) {
        return new VarStmtVisitor(visitorCtx).visitShortDeclaration(ctx);
    }

    @Override
    public PMLStatement<?> visitAdminOpDefinitionStatement(PMLParser.AdminOpDefinitionStatementContext ctx) {
        return new OperationDefinitionVisitor(visitorCtx, new OperationSignatureVisitor(visitorCtx, true))
            .visitAdminOpDefinitionStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitResourceOpDefinitionStatement(ResourceOpDefinitionStatementContext ctx) {
        return new OperationDefinitionVisitor(visitorCtx, new OperationSignatureVisitor(visitorCtx, true))
            .visitResourceOpDefinitionStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitRoutineDefinitionStatement(PMLParser.RoutineDefinitionStatementContext ctx) {
        return new OperationDefinitionVisitor(visitorCtx, new OperationSignatureVisitor(visitorCtx, true))
            .visitRoutineDefinitionStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitFunctionDefinitionStatement(FunctionDefinitionStatementContext ctx) {
        return new OperationDefinitionVisitor(visitorCtx, new OperationSignatureVisitor(visitorCtx, true))
            .visitFunctionDefinitionStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitQueryOpDefinitionStatement(QueryOpDefinitionStatementContext ctx) {
        return new OperationDefinitionVisitor(visitorCtx, new OperationSignatureVisitor(visitorCtx, true))
            .visitQueryOpDefinitionStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitReturnStatement(PMLParser.ReturnStatementContext ctx) {
        return new OperationReturnStmtVisitor(visitorCtx).visitReturnStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitOperationInvokeStatement(PMLParser.OperationInvokeStatementContext ctx) {
        return new OperationInvokeStmtVisitor(visitorCtx).visitOperationInvokeStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitForeachStatement(PMLParser.ForeachStatementContext ctx) {
        return new ForeachStmtVisitor(visitorCtx).visitForeachStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitBreakStatement(PMLParser.BreakStatementContext ctx) {
        return new BreakStmtVisitor(visitorCtx).visitBreakStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitContinueStatement(PMLParser.ContinueStatementContext ctx) {
        return new ContinueStmtVisitor(visitorCtx).visitContinueStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitIfStatement(PMLParser.IfStatementContext ctx) {
        return new IfStmtVisitor(visitorCtx).visitIfStatement(ctx);
    }

    @Override
    public PMLStatement<?> visitRequireStatement(PMLParser.RequireStatementContext ctx) {
        return new RequireStatementVisitor(visitorCtx).visitRequireStatement(ctx);
    }
}
