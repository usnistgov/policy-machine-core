package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateFunctionStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateOperationStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateRoutineStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PMLVisitor extends PMLBaseVisitor<List<PMLStatement>> {

    public PMLVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public List<PMLStatement> visitPml(PMLParser.PmlContext ctx) {
        SortedStatements sortedStatements = sortStatements(ctx);

        VisitorContext copy = visitorCtx.copy();
        CompiledExecutables executables = compileExecutables(copy, sortedStatements.functionCtxs);

        List<PMLStatement> stmts = new ArrayList<>();
        stmts.addAll(executables.operations);
        stmts.addAll(executables.routines);
        stmts.addAll(compileStatements(sortedStatements.statementCtxs));

        return stmts;
    }

    private SortedStatements sortStatements(PMLParser.PmlContext ctx) {
        List<PMLParser.FunctionDefinitionStatementContext> functionCtxs = new ArrayList<>();
        List<PMLParser.StatementContext> statementCtxs = new ArrayList<>();

        for (PMLParser.StatementContext stmtCtx : ctx.statement()) {
           if (stmtCtx.functionDefinitionStatement() != null) {
                functionCtxs.add(stmtCtx.functionDefinitionStatement());
           } else {
                statementCtxs.add(stmtCtx);
            }
        }

        return new SortedStatements(functionCtxs, statementCtxs);
    }

    private record SortedStatements(List<PMLParser.FunctionDefinitionStatementContext> functionCtxs,
                                    List<PMLParser.StatementContext> statementCtxs) {}

    private CompiledExecutables compileExecutables(VisitorContext visitorCtx, List<PMLParser.FunctionDefinitionStatementContext> functionSignatureCtxs) {
        Map<String, PMLExecutableSignature> executables = new HashMap<>(visitorCtx.scope().global().getExecutables());
        // track the function definitions statements to be processed,
        // any function with an error won't be processed but execution will continue inorder to find anymore errors
        Map<String, PMLParser.FunctionDefinitionStatementContext> validFunctionDefs = new HashMap<>();

        for (PMLParser.FunctionDefinitionStatementContext functionDefinitionStatementContext : functionSignatureCtxs) {
            boolean isOp = functionDefinitionStatementContext.functionSignature().OPERATION() != null;

            FunctionDefinitionVisitor.FunctionSignatureVisitor functionSignatureVisitor =
                    new FunctionDefinitionVisitor.FunctionSignatureVisitor(visitorCtx, isOp);

            // visit the signature which will add to the scope, if an error occurs, log it and continue
            try {
                PMLExecutableSignature signature = functionSignatureVisitor.visitFunctionSignature(
                        functionDefinitionStatementContext.functionSignature());

                // check that the function isn't already defined in the pml or global scope
                if (executables.containsKey(signature.getFunctionName())) {
                    visitorCtx.errorLog().addError(functionDefinitionStatementContext,
                                                   "function '" + signature.getFunctionName() + "' already defined in scope");
                    continue;
                }

                executables.put(signature.getFunctionName(), signature);
                validFunctionDefs.put(signature.getFunctionName(), functionDefinitionStatementContext);
            } catch (PMLCompilationRuntimeException e) {
                visitorCtx.errorLog().addErrors(e.getErrors());
            }
        }

        // store all function signatures for use in compiling function bodies
        visitorCtx.scope().global().addExecutables(executables);

        // compile function bodies
        FunctionDefinitionVisitor functionDefinitionVisitor = new FunctionDefinitionVisitor(visitorCtx);
        List<CreateOperationStatement> operations = new ArrayList<>();
        List<CreateRoutineStatement> routines = new ArrayList<>();

        for (PMLParser.FunctionDefinitionStatementContext functionDefinitionStatementContext : validFunctionDefs.values()) {
            // visit the definition which will return the statement with body
            try {
                CreateFunctionStatement funcStmt =
                        functionDefinitionVisitor.visitFunctionDefinitionStatement(functionDefinitionStatementContext);
                if (funcStmt instanceof CreateOperationStatement createOperationStatement) {
                    operations.add(createOperationStatement);
                } else if (funcStmt instanceof CreateRoutineStatement createRoutineStatement) {
                    routines.add(createRoutineStatement);
                }
            } catch (PMLCompilationRuntimeException e) {
                visitorCtx.errorLog().addErrors(e.getErrors());
            }
        }

        return new CompiledExecutables(operations, routines);
    }

    private record CompiledExecutables(List<CreateOperationStatement> operations, List<CreateRoutineStatement> routines) {}

    private List<PMLStatement> compileStatements(List<PMLParser.StatementContext> statementCtxs) {
        List<PMLStatement> statements = new ArrayList<>();
        for (PMLParser.StatementContext stmtCtx : statementCtxs) {
            StatementVisitor statementVisitor = new StatementVisitor(visitorCtx);

            try {
                PMLStatement statement = statementVisitor.visitStatement(stmtCtx);
                statements.add(statement);
            } catch (PMLCompilationRuntimeException e) {
                visitorCtx.errorLog().addErrors(e.getErrors());
            }
        }

        return statements;
    }

}
