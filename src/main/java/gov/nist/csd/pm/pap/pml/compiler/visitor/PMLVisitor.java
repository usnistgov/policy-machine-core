package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationRuntimeException;
import gov.nist.csd.pm.pap.pml.executable.PMLExecutableSignature;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.statement.basic.CreateFunctionStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateOperationStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.CreateRoutineStatement;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class PMLVisitor extends PMLBaseVisitor<List<PMLStatement>> {

    public PMLVisitor(VisitorContext visitorCtx) {
        super(visitorCtx);
    }

    @Override
    public List<PMLStatement> visitPml(PMLParser.PmlContext ctx) {
        SortedStatements sortedStatements = sortStatements(ctx);

        CompiledExecutables executables = compileExecutables(
                sortedStatements.operationCtxs,
                sortedStatements.routineCtxs,
                sortedStatements.functionCtxs
        );

        List<PMLStatement> stmts = new ArrayList<>();
        stmts.addAll(executables.operations);
        stmts.addAll(executables.routines);
        stmts.addAll(executables.functions);
        stmts.addAll(compileStatements(sortedStatements.statementCtxs));

        return stmts;
    }

    private SortedStatements sortStatements(PMLParser.PmlContext ctx) {
        List<PMLParser.OperationDefinitionStatementContext> operationCtxs = new ArrayList<>();
        List<PMLParser.RoutineDefinitionStatementContext> routineCtxs = new ArrayList<>();
        List<PMLParser.FunctionDefinitionStatementContext> functionCtxs = new ArrayList<>();
        List<PMLParser.StatementContext> statementCtxs = new ArrayList<>();

        for (PMLParser.StatementContext stmtCtx : ctx.statement()) {
            PMLParser.BasicStatementContext programmingStatementContext = stmtCtx.basicStatement();
            PMLParser.OperationStatementContext policyStatementContext = stmtCtx.operationStatement();

            if (programmingStatementContext != null) {
                if (programmingStatementContext.functionDefinitionStatement() != null) {
                    functionCtxs.add(programmingStatementContext.functionDefinitionStatement());
                } else {
                    statementCtxs.add(stmtCtx);
                }
            } else if (policyStatementContext != null) {
                if (policyStatementContext.operationDefinitionStatement() != null) {
                    operationCtxs.add(policyStatementContext.operationDefinitionStatement());
                } else if (policyStatementContext.routineDefinitionStatement() != null) {
                    routineCtxs.add(policyStatementContext.routineDefinitionStatement());
                } else {
                    statementCtxs.add(stmtCtx);
                }
            }
        }

        return new SortedStatements(operationCtxs, routineCtxs, functionCtxs, statementCtxs);
    }

    private record SortedStatements(List<PMLParser.OperationDefinitionStatementContext> operationCtxs,
                                    List<PMLParser.RoutineDefinitionStatementContext> routineCtxs,
                                    List<PMLParser.FunctionDefinitionStatementContext> functionCtxs,
                                    List<PMLParser.StatementContext> statementCtxs) {}

    private CompiledExecutables compileExecutables(List<PMLParser.OperationDefinitionStatementContext> operationCtxs,
                                                   List<PMLParser.RoutineDefinitionStatementContext> routineCtxs,
                                                   List<PMLParser.FunctionDefinitionStatementContext> functionCtxs) {
        Map<String, PMLExecutableSignature> executables = new HashMap<>(visitorCtx.scope().getExecutables());

        // track the function definitions statements to be processed,
        // function signatures are compiled first in the event that one function calls another
        // any function with an error won't be processed but execution will continue inorder to find anymore errors
        Map<String, PMLParser.OperationDefinitionStatementContext> validOperationDefs = new HashMap<>();
        Map<String, PMLParser.RoutineDefinitionStatementContext> validRoutineDefs = new HashMap<>();
        Map<String, PMLParser.FunctionDefinitionStatementContext> validFunctionDefs = new HashMap<>();

        ExecutableDefinitionVisitor.SignatureVisitor signatureVisitor =
                new ExecutableDefinitionVisitor.SignatureVisitor(visitorCtx, true);

        // operations
        for (PMLParser.OperationDefinitionStatementContext operationCtx : operationCtxs) {
            PMLExecutableSignature signature = signatureVisitor.visitOperationSignature(operationCtx.operationSignature());
            processSignature(visitorCtx, operationCtx, signature, executables, (ctx) -> validOperationDefs.put(signature.getName(), operationCtx));
        }

        // routines
        for (PMLParser.RoutineDefinitionStatementContext routineCtx : routineCtxs) {
            PMLExecutableSignature signature = signatureVisitor.visitRoutineSignature(routineCtx.routineSignature());
            processSignature(visitorCtx, routineCtx, signature, executables, (ctx) -> validRoutineDefs.put(signature.getName(), routineCtx));
        }

        // functions
        for (PMLParser.FunctionDefinitionStatementContext functionCtx : functionCtxs) {
            PMLExecutableSignature signature = signatureVisitor.visitFunctionSignature(functionCtx.functionSignature());
            processSignature(visitorCtx, functionCtx, signature, executables, (ctx) -> validFunctionDefs.put(signature.getName(), functionCtx));
        }

        // compile all executable bodies now that all signatures are compiled
        ExecutableDefinitionVisitor executableDefinitionVisitor = new ExecutableDefinitionVisitor(visitorCtx, false);
        List<CreateOperationStatement> operations = compileExecutables(operationCtxs, executableDefinitionVisitor::visitOperationDefinitionStatement);
        List<CreateRoutineStatement> routines = compileExecutables(routineCtxs, executableDefinitionVisitor::visitRoutineDefinitionStatement);
        List<CreateFunctionStatement> functions = compileExecutables(functionCtxs, executableDefinitionVisitor::visitFunctionDefinitionStatement);

        return new CompiledExecutables(operations, routines, functions);
    }

    private <T, R> List<R> compileExecutables(List<T> contexts, Function<T, R> visitor) {
        List<R> results = new ArrayList<>();
        for (T context : contexts) {
            try {
                results.add(visitor.apply(context));
            } catch (PMLCompilationRuntimeException e) {
                visitorCtx.errorLog().addErrors(e.getErrors());
            }
        }

        return results;
    }

    private void processSignature(VisitorContext vistorCtx,
                                  ParserRuleContext statementCtx,
                                  PMLExecutableSignature signature,
                                  Map<String, PMLExecutableSignature> executables,
                                  Consumer<ParserRuleContext> consumer) {
        // visit the signature which will add to the scope, if an error occurs, log it and continue
        try {
            // check that the function isn't already defined in the pml or global scope
            if (executables.containsKey(signature.getName())) {
                visitorCtx.errorLog().addError(
                        statementCtx,
                        "executable '" + signature.getName() + "' already defined in scope"
                );

                return;
            }


            executables.put(signature.getName(), signature);
            consumer.accept(statementCtx);
        } catch (PMLCompilationRuntimeException e) {
            visitorCtx.errorLog().addErrors(e.getErrors());
        }
    }

    private record CompiledExecutables(List<CreateOperationStatement> operations,
                                       List<CreateRoutineStatement> routines,
                                       List<CreateFunctionStatement> functions) {}

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
